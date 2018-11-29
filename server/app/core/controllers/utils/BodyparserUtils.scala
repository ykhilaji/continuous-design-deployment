package core.controllers.utils

import core.api.ApiErrors
import play.api.libs.json._
import play.api.mvc._

trait BodyparserUtils { self: BaseController =>

  def parseJson[A](implicit reader: Reads[A]): BodyParser[A] =
    BodyParser("json reader") { request =>
      implicit val messages = messagesApi.preferred(request)
      self.parse.json
        .apply(request)
        .map {
          case Left(_) => Left(ApiErrors.invalidFormat.toResult)
          case Right(jsValue) =>
            jsValue.validate(reader) map { a =>
              Right(a)
            } recoverTotal { jsError =>
              Left(ApiErrors.validationError(jsError).toResult)
            }
        }(self.defaultExecutionContext)
    }

}
