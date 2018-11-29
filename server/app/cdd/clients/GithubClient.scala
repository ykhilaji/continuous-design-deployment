package cdd.clients

import scala.concurrent.Future
import play.api.libs.json.JsValue
import play.api.libs.ws.WSClient

import scala.concurrent.ExecutionContext.Implicits.global

class GithubClient(ws: WSClient, githubToken: String) {
  val token = githubToken
  val baseUrl = "https://api.github.com"

  private def githubAuthenticatedGet(path: String) = {
    ws.url(s"${baseUrl}${path}")
      .addHttpHeaders("Authorization" -> s"Bearer $token")
      .get()
  }
  def repos: Future[JsValue] =
    githubAuthenticatedGet("/user/repos")
      .map(res => res.json)

  def getRepo(owner: String, projectName: String): Future[JsValue] =
    githubAuthenticatedGet(s"/repos/$owner/$projectName")
      .map(res => res.json)

  def branches(owner: String, projectName: String) =
    githubAuthenticatedGet(s"/repos/$owner/$projectName/branches")
      .map(res => res.json)

}
