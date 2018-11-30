package cdd.clients

import java.util.Base64

import play.api.libs.json._
import play.api.libs.ws.WSClient

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

class GithubClient(ws: WSClient, githubToken: String) {
  val token = githubToken
  val baseUrl = "https://api.github.com"

  private def githubAuthenticated(path: String) = {
    ws.url(s"${baseUrl}${path}")
      .addHttpHeaders("Authorization" -> s"Bearer $token", "accept" -> "application/vnd.github.v3+json")
  }

  private def githubAuthenticatedGet(path: String) = githubAuthenticated(path).get()
  private def githubAuthenticatedPost(path: String, payload: JsValue) = githubAuthenticated(path).post(payload)
  private def githubAuthenticatedPut(path: String, payload: JsValue) = githubAuthenticated(path).put(payload)

  def repos =
    githubAuthenticatedGet("/user/repos")
      .map(_.json)

  def getRepo(owner: String, projectName: String) =
    githubAuthenticatedGet(s"/repos/$owner/$projectName")
      .map(_.json)

  def branches(owner: String, projectName: String) =
    githubAuthenticatedGet(s"/repos/$owner/$projectName/branches")
      .map(_.json)

  private def getMasterBranchRef(owner: String, projectName: String): Future[String] =
    githubAuthenticatedGet(s"/repos/$owner/$projectName/git/refs/heads/master")
      .map(res => (res.json \ "object" \ "sha").as[String])

  def createBranch(owner: String, projectName: String, branchName: String): Future[JsValue] =
    getMasterBranchRef(owner, projectName).flatMap(masterRef => {
      githubAuthenticatedPost(
        s"/repos/$owner/$projectName/git/refs",
        JsObject(Seq("ref" -> JsString(s"refs/heads/$branchName"), "sha" -> JsString(masterRef)))
      ).map(_.json)
    })

  def file(owner: String, projectName: String, path: String, branchName: String): Future[JsValue] =
    githubAuthenticatedGet(s"/repos/$owner/$projectName/contents/$path?ref=$branchName")
      .map(res => {
        (res.json \ "content").validate[String] match {
          case JsSuccess(x, _) =>
            JsObject(Seq("content" -> JsString(new String(Base64.getDecoder.decode(x.replaceAll("\n", ""))))))
          case JsError(_) => res.json
        }
      })

  def updateAsset(
    owner: String,
    projectName: String,
    path: String,
    branch: String,
    content: String
  ): Future[JsValue] = {
    githubAuthenticatedGet(s"/repos/$owner/$projectName/contents/$path?ref=$branch").flatMap(res => {
      (res.json \ "sha").validate[String] match {
        case JsSuccess(sha, _) => {
          githubAuthenticatedPut(
            s"/repos/$owner/$projectName/contents/$path",
            JsObject(
              Seq(
                "message" -> JsString(s"Updated asset $path"),
                "branch" -> JsString(branch),
                "content" -> JsString(content),
                "sha" -> JsString(sha)
              )
            )
          ).map(_.json)
        }
        case JsError(_) =>
          githubAuthenticatedPut(
            s"/repos/$owner/$projectName/contents/$path",
            JsObject(
              Seq(
                "message" -> JsString(s"Created asset $path"),
                "branch" -> JsString(branch),
                "content" -> JsString(content)
              )
            )
          ).map(_.json)
      }
    })
  }
}
