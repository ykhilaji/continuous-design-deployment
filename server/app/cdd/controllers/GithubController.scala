package cdd.controllers

import cdd.clients.GithubClient
import cdd.models.PushableAsset
import play.api.libs.json.{Format, Json}
import play.api.mvc.{AbstractController, ControllerComponents}

import scala.concurrent.ExecutionContext.Implicits.global

class GithubController(cc: ControllerComponents, ghc: GithubClient) extends AbstractController(cc) {
  def listRepos = Action.async {
    ghc.repos.map(x => Ok(Json.toJson(x)))
  }
  def getRepo(owner: String, projectName: String) = Action.async {
    ghc.getRepo(owner, projectName).map(x => Ok(Json.toJson(x)))
  }
  def getBranches(owner: String, projectName: String) = Action.async {
    ghc.branches(owner, projectName).map(x => Ok(Json.toJson(x)))
  }
  def doAssetsPR(owner: String, projectName: String) = Action.async(parse.json[List[PushableAsset]]) {
    implicit request =>
      println(request.body)
      ghc.doAssetsPR(owner, projectName, request.body).map(x => Ok(Json.toJson(x)))
  }
}
//    ghc
//      .pushAssetFromUrl(
//        "zengularity",
//        "continuous-design-deployment",
//        "images/darealdondoudouaaaaaaa.png",
//        "test",
//        "https://pngimage.net/wp-content/uploads/2018/06/rondoudou-png-3.png"
//      )
//      .map(x => Ok(x))
