package core.api

import play.api.libs.json._
import play.api.mvc._
import play.api.i18n.Messages

case class ApiError(status: Results.Status, key: String, jsError: JsError, args: Seq[Any] = Seq.empty)(
  implicit messages: Messages
) {

  private def pathDotted(jsPath: JsPath): String = jsPath.toString() match {
    case path if path.nonEmpty => path.tail.replace("/", ".")
    case _                     => ""
  }

  private def jsonError(): JsObject = {
    val hasError = jsError.errors.headOption.exists(_._2.nonEmpty)
    val errors = hasError match {
      case false => Json.obj()
      case true =>
        jsError.errors.foldLeft(Json.obj()) {
          case (acc, (jsPath, seqValidationErrors)) =>
            acc + (pathDotted(jsPath) -> JsArray(
              seqValidationErrors.flatMap { err =>
                err.messages.map(key => Json.obj("key" -> key, "message" -> Messages(key, err.args: _*)))
              }
            ))
        }
    }
    Json.obj(
      "key" -> key,
      "message" -> Messages(key, args: _*),
      "errors" -> errors,
      "args" -> Json.toJson(args)(ApiError.argsWrites)
    )
  }

  def toResult: Result = status(jsonError())
}

object ApiError {
  def apply(status: Results.Status, key: String)(implicit messages: Messages) =
    new ApiError(status, key, JsError())

  def apply(status: Results.Status, key: String, args: Any*)(implicit messages: Messages) =
    new ApiError(status, key, JsError(), args)

  private val anyWrites = Writes[Any] {
    case s: String                    => JsString(s)
    case nb: Int                      => JsNumber(nb)
    case nb: Short                    => JsNumber(nb)
    case nb: Long                     => JsNumber(nb)
    case nb: Double                   => JsNumber(nb)
    case nb: Float                    => JsNumber(nb)
    case b: Boolean                   => JsBoolean(b)
    case js: JsValue                  => js
    case (key: String, value: String) => Json.obj(key -> value)
    case x                            => JsString(x.toString)
  }
  private val argsWrites = Writes.traversableWrites[Any](anyWrites)
}
