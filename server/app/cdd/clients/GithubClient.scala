package cdd.clients

import scala.concurrent.Future
import play.api.libs.json.JsValue
import play.api.libs.ws.WSClient

import scala.concurrent.ExecutionContext.Implicits.global

trait RepositoriesClient {
  val token: String
  def repos: Future[JsValue]
  def getRepo(owner: String, projectName: String): Future[JsValue]
}

class GithubClient(ws: WSClient, githubToken: String) extends RepositoriesClient {
  val token = githubToken

  def repos: Future[JsValue] =
    ws.url("https://api.github.com/user/repos")
      .addHttpHeaders("Authorization" -> s"Bearer $token")
      .get()
      .map(res => {
        res.json
      })

  def getRepo(owner: String, projectName: String): Future[JsValue] =
    ws.url(s"https://api.github.com/repos/$owner/$projectName")
      .addHttpHeaders("Authorization" -> s"Bearer $token")
      .get()
      .map(res => {
        res.json
      })
}
