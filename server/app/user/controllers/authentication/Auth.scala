package user.controllers.authentication

import scala.concurrent.{ExecutionContext, Future}

import akka.stream.Materializer
import core.utils.Settings
import core.api.ApiErrors
import core.controllers.utils.NActionBuilder
import play.api.i18n.{I18nSupport, MessagesApi}
import play.api.mvc.ControllerComponents
import user.services.ApiKeyService
import user.models.ApiKey

class Auth(
  settings: Settings,
  apiKeyService: ApiKeyService
)(implicit ec: ExecutionContext, mat: Materializer, val messagesApi: MessagesApi, cc: ControllerComponents)
    extends I18nSupport {

  /** Method to extract the api key from a request and to test its validity */
  val apiKeyChecker = NActionBuilder.fromPartial[ApiKey] {
    case request if request.headers.get(settings.apiKeyHeader).isDefined =>
      val apiKey = new ApiKey(request.headers.get(settings.apiKeyHeader).get)
      apiKeyService.isValid(apiKey).map {
        case true => Right(apiKey)
        case false =>
          implicit val messages = messagesApi.preferred(request)
          Left(ApiErrors.invalidAuth.toResult)
      }
  }

  val ForbiddenRecover = NActionBuilder.fromPartial[Nothing] {
    case request =>
      implicit val messages = messagesApi.preferred(request)
      Future.successful(Left(ApiErrors.noRights.toResult))
  }

  /** Action that will get the api key from the request or return a forbidden result */
  val withApiKey = apiKeyChecker orElse ForbiddenRecover
}
