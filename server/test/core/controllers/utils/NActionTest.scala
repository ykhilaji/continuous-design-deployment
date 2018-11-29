package core.controllers.utils

import scala.concurrent.Future

import play.api.mvc._
import play.api.test.Helpers._
import play.api.test._

class NActionTest extends test.AppTest {
  implicit val ec = components.executionContext
  val system = components.actorSystem
  implicit val mat = components.materializer
  implicit val cc = components.controllerComponents

  def testActionBuilder(
    builder: NActionBuilder[String],
    request: FakeRequest[_],
    expected: String,
    expectedStatus: Int = 200
  ) = {
    val action = builder(components.playBodyParsers.empty) { request =>
      Results.Ok(request.tag)
    }
    val result = action.apply(request).run()
    contentAsString(result) shouldBe expected
    status(result) shouldBe expectedStatus
  }

  def testActionBuilderException(builder: NActionBuilder[String], request: FakeRequest[_]) = {
    val action = builder(components.playBodyParsers.empty) { request =>
      Results.Ok(request.tag)
    }
    val result = action.apply(request).run()
    an[RuntimeException] must be thrownBy {
      contentAsString(result)
      ()
    }
  }

  "NActionBuilder" should {
    val okCheck = new NActionBuilder[String] {
      def partial(rh: RequestHeader) = Future.successful(CheckResult.Ok("tag"))
    }
    val forbiddenCheck = new NActionBuilder[String] {
      def partial(rh: RequestHeader) = Future.successful(CheckResult.Invalid(Results.Forbidden("forbidden")))
    }
    val notApplicableCheck = new NActionBuilder[String] {
      def partial(rh: RequestHeader) = Future.successful(CheckResult.NotApplicable)
    }

    "work for simple actions" in {
      testActionBuilder(okCheck, FakeRequest(), "tag")
    }

    "send results in case of invalid result" in {
      testActionBuilder(forbiddenCheck, FakeRequest(), "forbidden", FORBIDDEN)
    }

    "get errors if not applicable" in {
      testActionBuilderException(notApplicableCheck, FakeRequest())
    }

    "be able to compose actions with or" in {
      testActionBuilder(okCheck orElse forbiddenCheck, FakeRequest(), "tag")
      testActionBuilder(notApplicableCheck orElse okCheck, FakeRequest(), "tag")
      testActionBuilder(forbiddenCheck orElse okCheck, FakeRequest(), "forbidden", FORBIDDEN)
    }

    "be able to compose actions with and" in {
      val actionBuilder1 = okCheck.andThenCheck[String]((_, _) => Future.successful(CheckResult.Ok("tag2")))
      testActionBuilder(actionBuilder1, FakeRequest(), "tag2")
      val actionBuilder2 =
        okCheck.andThenCheck((_, _) => Future.successful(CheckResult.Invalid(Results.Forbidden("forbidden"))))
      testActionBuilder(actionBuilder2, FakeRequest(), "forbidden", FORBIDDEN)
    }

    val X_API_KEY = "X-API-KEY"
    "be able to compose complex checks" in {
      val checkA = new NActionBuilder[String] {
        override def partial(rh: RequestHeader) =
          if (rh.headers.get(X_API_KEY).isDefined) {
            Future.successful(CheckResult.Ok(rh.headers.get(X_API_KEY).get))
          } else {
            Future.successful(CheckResult.NotApplicable)
          }
      }
      val checkB = (apiKey: String, request: RequestHeader) => {
        if (apiKey == "secret") Future.successful(CheckResult.Ok("ok-checkB"))
        else Future.successful(CheckResult.Invalid(Results.Forbidden("forbidden-checkB")))
      }
      val actionBuilder = (checkA andThenCheck checkB) orElse forbiddenCheck

      testActionBuilder(actionBuilder, FakeRequest(), "forbidden", FORBIDDEN)
      testActionBuilder(
        actionBuilder,
        FakeRequest().withHeaders(X_API_KEY -> "bad-secret"),
        "forbidden-checkB",
        FORBIDDEN
      )
      testActionBuilder(actionBuilder, FakeRequest().withHeaders(X_API_KEY -> "secret"), "ok-checkB")
    }
  }
}
