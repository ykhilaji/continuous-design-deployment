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
}
