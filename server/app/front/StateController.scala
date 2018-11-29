package front

import core.utils.Logging
import play.api.libs.json.{JsObject, Json}
import play.api.mvc._

class StateController(cc: ControllerComponents) extends AbstractController(cc) with Logging {

  def index(path: String) = Action {
    Ok(views.html.index())
  }

  def globalState() = Action {
    Ok(
      Json.obj(
        "9e" -> 200,
        "10e" -> 430,
        "11e" -> 170,
        "12e" -> 217,
        "13e" -> 5,
      )
    )
  }
}
