package test

import scala.concurrent.Await
import scala.concurrent.duration._

import org.scalatest.concurrent.ScalaFutures
import org.scalatest.{BeforeAndAfterAll, Matchers, WordSpec}
import core.utils.Logging
import play.api.inject.{ApplicationLifecycle, DefaultApplicationLifecycle}
import play.api.mvc.{ControllerComponents, DefaultControllerComponents, EssentialFilter}
import play.api.{BuiltInComponents, Configuration, Environment}
import play.core.{DefaultWebCommands, SourceMapper, WebCommands}

trait UnitTest extends WordSpec with ScalaFutures with Matchers with Logging {
  val env = Environment.simple()
  val conf = Configuration.load(env)
}

trait AppTest extends UnitTest with BeforeAndAfterAll {
  val components = TestComponents.load()

  override def afterAll(): Unit = {
    logger.info("shutting down application")
    Await.result(components.application.stop(), 10.seconds)
    super.afterAll()
  }
}

trait TestComponents extends BuiltInComponents {
  def controllerComponents: ControllerComponents
}

object TestComponents {
  def load(): TestComponents = {
    val env = Environment.simple()
    val conf = Configuration.load(env)
    new TestComponents {
      val router = null
      val environment: Environment = env
      val sourceMapper: Option[SourceMapper] = None
      val webCommands: WebCommands = new DefaultWebCommands()
      val configuration: Configuration = conf
      val applicationLifecycle: ApplicationLifecycle = new DefaultApplicationLifecycle()
      val httpFilters: Seq[EssentialFilter] = Seq.empty

      val controllerComponents = DefaultControllerComponents(
        defaultActionBuilder,
        playBodyParsers,
        messagesApi,
        langs,
        fileMimeTypes,
        executionContext
      )
    }
  }
}
