package user.controllers

import play.api.test.FakeRequest
import play.api.test.Helpers._

class HomeControllerTest extends test.AppTest {

  val controller = new user.controllers.HomeController(components.controllerComponents, null)

  "Application page about" should {
    "contain the application version" in {
      val result = controller.about().apply(FakeRequest())
      val bodyText = contentAsString(result)
      bodyText should include(sbt.BuildInfo.version)
    }
  }
}
