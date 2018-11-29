package cdd.models

import play.api.libs.json.{Format, JsObject, Json}

//TODO

case class GithubRepository(url: String)

object GithubRepository {
  implicit val ghFormat: Format[GithubRepository] = Json.format[GithubRepository]
}
