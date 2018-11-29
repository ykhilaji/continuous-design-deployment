package user.services

import scala.concurrent.Future

import user.models.ApiKey

class ApiKeyService {
  def isValid(key: ApiKey): Future[Boolean] = Future.successful(true)
}
