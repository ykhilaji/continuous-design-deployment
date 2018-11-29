package core.api

import play.api.libs.json.JsError
import play.api.mvc.Results._
import play.api.i18n.Messages

object ApiErrors {
  private val keyRoot = "apierror"

  def invalidAuth(implicit messages: Messages) =
    ApiError(Unauthorized, key = s"$keyRoot.unauthorized")

  def noRights(implicit messages: Messages) =
    ApiError(Unauthorized, key = s"$keyRoot.no-rights")

  def badRequest(arg: String)(implicit messages: Messages) =
    ApiError(BadRequest, key = s"$keyRoot.bad-request", arg)

  def invalidFormat(implicit messages: Messages) =
    ApiError(BadRequest, key = s"$keyRoot.bad-format")

  def invalidFormatFormData(implicit messages: Messages) =
    ApiError(BadRequest, key = s"$keyRoot.bad-format-form-data")

  def forbidden(arg: String)(implicit messages: Messages) =
    ApiError(Forbidden, key = s"$keyRoot.forbidden", arg)

  def payloadTooLarge(maxSize: Long)(implicit messages: Messages) =
    ApiError(EntityTooLarge, key = s"$keyRoot.entity-too-large", humanReadableByteSize(maxSize))

  private def humanReadableByteSize(fileSize: Long): String = {
    if (fileSize <= 0)
      "0 B"
    else {
      val units: Array[String] = Array("B", "kB", "MB", "GB", "TB", "PB", "EB", "ZB", "YB")
      val digitGroup: Int = (Math.log10(fileSize.toDouble) / Math.log10(1024.toDouble)).toInt
      f"${fileSize / Math.pow(1024.toDouble, digitGroup.toDouble)}%3.2f ${units(digitGroup)}"
    }
  }

  def notFound(implicit messages: Messages) =
    ApiError(NotFound, key = s"$keyRoot.not-found")

  def routeNotFound(method: String, path: String)(implicit messages: Messages) =
    ApiError(NotFound, key = s"$keyRoot.route-not-found", method, path)

  def validationError(errors: JsError)(implicit messages: Messages) =
    ApiError(BadRequest, key = s"$keyRoot.validation-errors", errors)

  def internalServerError(implicit messages: Messages) =
    ApiError(InternalServerError, key = s"$keyRoot.internal-server-error")
}
