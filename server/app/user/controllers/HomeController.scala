package user.controllers

import core.utils.Logging
import jsmessages.JsMessages
import play.api.i18n.I18nSupport
import play.api.libs.json.Json
import play.api.mvc._

class HomeController(cc: ControllerComponents, messages: JsMessages)
    extends AbstractController(cc) with Logging with I18nSupport {

  def index(path: String) = Action {
    val initData = Json.obj("helloWorld" -> "World")
    Ok(user.views.html.index(initData))
  }

  val jsMessages = Action { implicit request =>
    Ok(messages(Some("i18n")))
  }

  def about = Action {
    Ok(sbt.BuildInfo.toString)
  }
}
