package cdd.controllers

import cdd.clients.GithubClient
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
  def getFile(owner: String, projectName: String, path: String, branch: Option[String]) = Action.async {
    ghc.file(owner, projectName, path, branch.getOrElse("master")).map(x => Ok(Json.toJson(x)))
  }
  def createBranch(owner: String, projectName: String, branchName: String) = Action.async {
    ghc.createBranch(owner, projectName, branchName).map(x => Ok(Json.toJson(x)))
  }
  def updateAsset(owner: String, projectName: String, path: String) = Action.async(parse.json[UpdateAsset]) {
    implicit request =>
      ghc
        .updateAsset(owner, projectName, path, request.body.branch, request.body.content)
        .map(x => Ok(Json.toJson(x)))
  }
}

case class UpdateAsset(branch: String, content: String)

object UpdateAsset {
  implicit val cfFormat: Format[UpdateAsset] = Json.format[UpdateAsset]
}
