package core.metrics

import scala.concurrent.Future

import akka.stream.Materializer
import core.utils.Configuration
import kamon.Kamon
import kamon.metric.instrument.{InstrumentFactory, Time}
import kamon.metric.{EntityRecorderFactory, GenericEntityRecorder}
import kamon.util.SameThreadExecutionContext
import play.api.mvc._

class MetricsFilter()(implicit val mat: Materializer) extends Filter {
  val httpServerMetrics =
    Kamon.metrics.entity(HttpServerMetrics, name = "metrics-filter")

  def apply(next: RequestHeader => Future[Result])(request: RequestHeader): Future[Result] = {
    val normalizedPath = HttpTraceNameGenerator.generateTraceName(request)
    val tags = Map(
      "path" -> normalizedPath,
      "method" -> request.method
    )
    val httpPathMetrics = Kamon.metrics.entity(HttpPathMetrics, name = "metrics-filter", tags = tags)
    val start = System.nanoTime()

    httpServerMetrics.queueSize.increment()
    val result = next(request).map { response =>
      httpServerMetrics.queueSize.decrement()
      httpPathMetrics.recordResponse(response, start)
      response
    }(SameThreadExecutionContext)

    result.failed.foreach { exception =>
      httpServerMetrics.queueSize.decrement()
      httpPathMetrics.recordError(start)
    }(SameThreadExecutionContext)

    result
  }
}

object MetricsFilter {
  def apply(config: Configuration)(implicit mat: Materializer): Seq[Filter] =
    if (config.getBoolean("metrics.enabled").required) {
      Seq(new MetricsFilter())
    } else {
      Seq.empty
    }
}

class HttpPathMetrics(instrumentFactory: InstrumentFactory) extends GenericEntityRecorder(instrumentFactory) {
  def recordError(start: Long): Unit = record("500", start)
  def recordResponse(result: Result, start: Long): Unit =
    record(result.header.status.toString, start)
  private def record(status: String, start: Long): Unit = {
    val time = System.nanoTime() - start
    counter(status).increment()
    histogram("processing-time", Time.Nanoseconds).record(time)
  }
}

object HttpPathMetrics extends EntityRecorderFactory[HttpPathMetrics] {
  def category: String = "http-server"
  def createRecorder(instrumentFactory: InstrumentFactory): HttpPathMetrics =
    new HttpPathMetrics(instrumentFactory)
}

class HttpServerMetrics(instrumentFactory: InstrumentFactory) extends GenericEntityRecorder(instrumentFactory) {
  val queueSize = minMaxCounter("queue-size")
}

object HttpServerMetrics extends EntityRecorderFactory[HttpServerMetrics] {
  def category: String = "http-server"
  def createRecorder(instrumentFactory: InstrumentFactory): HttpServerMetrics =
    new HttpServerMetrics(instrumentFactory)
}

object HttpTraceNameGenerator {
  import scala.collection.concurrent.TrieMap

  import play.api.routing.Router

  private val cache = TrieMap.empty[String, String]
  private val normalizePattern = """\$([^<]+)<[^>]+>""".r

  // from the request, it will get the router path and method and generate a traceName
  def generateTraceName(request: RequestHeader): String =
    request.attrs
      .get(Router.Attrs.HandlerDef)
      .map { handlerDef =>
        cache.getOrElseUpdate(
          handlerDef.path, {
            // converts path of form /foo/bar/$paramname<regexp>/blah to /foo/bar/paramname/blah
            normalizePattern.replaceAllIn(handlerDef.path, ":$1")
          }
        )
      }
      .getOrElse("not-found")
}
