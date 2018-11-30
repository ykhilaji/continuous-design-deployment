package cdd.clients

import java.nio.charset.StandardCharsets
import java.util.Base64

import cdd.models.PushableAsset
import play.api.libs.json._
import play.api.libs.ws.WSClient

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import scala.util.Random

class GithubClient(ws: WSClient, githubToken: String) {
  val token = githubToken
  val baseUrl = "https://api.github.com"

  private def generateBranchName: String = {
    val randomString: String = Random.alphanumeric.take(10).mkString
    s"assets-${java.time.LocalDate.now}_$randomString"
  }

  private def githubAuthenticated(path: String) = {
    ws.url(s"${baseUrl}${path}")
      .addHttpHeaders(
        "Authorization" -> s"Bearer $token",
        "accept" -> "application/vnd.github.v3+json",
        "Content-Type" -> "application/json",
        "charset" -> "utf-8"
      )
  }

  private def githubAuthenticatedGet(path: String) = githubAuthenticated(path).get()
  private def githubAuthenticatedPost(path: String, payload: JsValue) = githubAuthenticated(path).post(payload)
  private def githubAuthenticatedPut(path: String, payload: JsValue) = githubAuthenticated(path).put(payload)

  private def getMasterBranchRef(owner: String, projectName: String): Future[String] =
    githubAuthenticatedGet(s"/repos/$owner/$projectName/git/refs/heads/master")
      .map(res => (res.json \ "object" \ "sha").as[String])

  private def createBranch(owner: String, projectName: String, branchName: String): Future[JsValue] =
    getMasterBranchRef(owner, projectName).flatMap(masterRef => {
      githubAuthenticatedPost(
        s"/repos/$owner/$projectName/git/refs",
        JsObject(Seq("ref" -> JsString(s"refs/heads/$branchName"), "sha" -> JsString(masterRef)))
      ).map(_.json)
    })

//  private def file(owner: String, projectName: String, path: String, branchName: String): Future[JsValue] =
//    githubAuthenticatedGet(s"/repos/$owner/$projectName/contents/$path?ref=$branchName")
//      .map(res => {
//        (res.json \ "content").validate[String] match {
//          case JsSuccess(x, _) =>
//            JsObject(Seq("content" -> JsString(new String(Base64.getDecoder.decode(x.replaceAll("\n", ""))))))
//          case JsError(_) => res.json
//        }
//      })

  private def updateAsset(
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

  private def openAndMergePR(owner: String, projectName: String, branchName: String): Future[JsValue] = {
    githubAuthenticatedPost(
      s"/repos/$owner/$projectName/pulls",
      JsObject(
        Seq(
          "title" -> JsString(s"Assets branch $branchName into master-test"),
          "head" -> JsString(branchName),
          "base" -> JsString("master-test")
        )
      )
    ).flatMap(res => {
        githubAuthenticatedPut(
          s"/repos/$owner/$projectName/pulls/${(res.json \ "number").as[Int]}/merge",
          JsObject(
            Seq(
              "merge_method" -> JsString("squash")
            )
          )
        )
      })
      .map(_.json)
  }

  private def pushAssetFromUrl(
    owner: String,
    projectName: String,
    path: String,
    branch: String,
    assetUrl: String
  ) = getEncodedImageFromUrl(assetUrl).flatMap(img => updateAsset(owner, projectName, path, branch, img))

  private def getEncodedImageFromUrl(url: String): Future[String] = {
    ws.url(url)
      .get()
      .map(img => {
        Base64.getEncoder.encodeToString(img.body.getBytes(StandardCharsets.UTF_8))
      })
  }

  private def seqFutures[T, U](items: TraversableOnce[T])(yourfunction: T => Future[U]): Future[List[U]] = {
    items.foldLeft(Future.successful[List[U]](Nil)) { (f, item) =>
      f.flatMap { x =>
        yourfunction(item).map(_ :: x)
      }
    } map (_.reverse)
  }

  def repos =
    githubAuthenticatedGet("/user/repos")
      .map(_.json)

  def getRepo(owner: String, projectName: String) =
    githubAuthenticatedGet(s"/repos/$owner/$projectName")
      .map(_.json)

  def branches(owner: String, projectName: String) =
    githubAuthenticatedGet(s"/repos/$owner/$projectName/branches")
      .map(_.json)

  def doAssetsPR(owner: String, projectName: String, assets: List[PushableAsset]): Future[JsValue] = {
    val branchName = generateBranchName
    createBranch(owner, projectName, branchName)
      .flatMap(res => {
        seqFutures(assets)(
          asset => {
            pushAssetFromUrl(
              owner,
              projectName,
              s"${asset.path}/${asset.name}.${asset.extension}",
              branchName,
              asset.url
            )
          }
        )
      })
      .flatMap(x => openAndMergePR(owner, projectName, branchName))
  }
}
