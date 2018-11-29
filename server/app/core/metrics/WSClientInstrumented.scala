package core.metrics

import java.util.concurrent.atomic.AtomicInteger
import scala.concurrent.{ExecutionContext, Future}
import scala.util.{Failure, Success, Try}

import kamon.Kamon
import kamon.metric.instrument.{InstrumentFactory, Time}
import kamon.metric.{EntityRecorderFactory, GenericEntityRecorder}
import kamon.util.SameThreadExecutionContext
import play.api.Logger
import play.api.libs.ws.{StandaloneWSRequest, StandaloneWSResponse, WSClient, WSRequestExecutor, WSRequestFilter}

/** Wrapper for a WS Client that adds logging and metrics to the ws client */
object WSClientInstrumented {
  def apply(wsClient: WSClient): WSClient = new WSClient {
    val wsRequestFilter = new WSRequestFilterInstrumented(Logger("wsclient"), SameThreadExecutionContext)
    override def close() = wsClient.close()
    override def underlying[T] = wsClient.asInstanceOf[T]
    override def url(url: String) =
      wsClient.url(url).withRequestFilter(wsRequestFilter)
  }
}

class WSRequestFilterInstrumented(logger: Logger, ec: ExecutionContext) extends WSRequestFilter {
  val counter = new AtomicInteger(0)
  def apply(executor: WSRequestExecutor): WSRequestExecutor =
    new WSRequestExecutor {
      def apply(request: StandaloneWSRequest): Future[StandaloneWSResponse] = {
        val requestId = counter.incrementAndGet()
        val start = System.nanoTime
        val wsClientMetrics =
          Kamon.metrics.entity(WSClientMetrics, name = getHost(request))
        logRequest(requestId, request)

        val res = executor.apply(request)
        res.onComplete {
          case Success(response) =>
            wsClientMetrics.recordResponse(response.status, start)
            logResponse(requestId, request, response)
          case Failure(err) =>
            wsClientMetrics.recordError(start)
            logError(requestId, request, err)
        }(ec)
        res
      }
    }

  private def getHost(request: StandaloneWSRequest): String =
    Try {
      request.uri.getHost
    }.getOrElse(request.url)

  private def logRequest(requestId: Int, request: StandaloneWSRequest): Unit =
    logger.trace(s"WS id=${requestId}: ${request.method} ${request.url} starts")

  private def logResponse(requestId: Int, request: StandaloneWSRequest, response: StandaloneWSResponse): Unit =
    response.status match {
      case status if status >= 100 && status < 300 || status == 303 || status == 304 =>
        logger.trace(s"WS id=${requestId}: ${request.method} ${request.url} completed with status ${status}")
      case status if status >= 300 && status < 500 =>
        logger.warn(s"WS id=${requestId}: ${request.method} ${request.url} completed with a bad response: ${status}")
      case status =>
        logger.error(s"WS id=${requestId}: ${request.method} ${request.url} failed with status ${status}")
    }

  private def logError(requestId: Int, request: StandaloneWSRequest, error: Throwable): Unit =
    logger.error(s"WS id=${requestId}: ${request.method} ${request.url} encountered an exception", error)
}

class WSClientMetrics(instrumentFactory: InstrumentFactory) extends GenericEntityRecorder(instrumentFactory) {
  def recordError(start: Long): Unit = record("error", start)
  def recordResponse(status: Int, start: Long): Unit =
    record(status.toString, start)
  private def record(statusCode: String, start: Long): Unit = {
    val time = System.nanoTime() - start
    counter(statusCode).increment()
    histogram("processing-time", Time.Nanoseconds).record(time)
  }
}
object WSClientMetrics extends EntityRecorderFactory[WSClientMetrics] {
  val category: String = "ws-client"
  def createRecorder(instrumentFactory: InstrumentFactory): WSClientMetrics =
    new WSClientMetrics(instrumentFactory)
}
