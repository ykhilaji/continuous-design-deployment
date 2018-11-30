package cdd.controllers

import cdd.clients.GithubClient
import cdd.models.Id
import play.api.mvc._
import cdd.services.FigmaService
import play.api.libs.json.Json

import scala.concurrent.{ExecutionContext, Future}

class FigmaController(
  cc: ControllerComponents,
  figmaService: FigmaService,
  githubClient: GithubClient
)(implicit ec: ExecutionContext)
    extends AbstractController(cc) {

  def listImages(fileKey: String, from: Option[Int], to: Option[Int]) = Action.async(parse.json[Seq[Id]]) {
    implicit request =>
      for {
        documents <- figmaService.assetsDocuments(fileKey)
        assets <- figmaService.assets(fileKey, documents, request.body, from.getOrElse(0), to.getOrElse(20))
        pr <- githubClient.doAssetsPR("zengularity", "continuous-design-deployment", assets.toList.map(_.toPushable))
      } yield Ok(Json.toJson(pr))
  }

  def documentTree(fileKey: String) = Action.async { implicit request =>
    figmaService.documentTree(fileKey).map { tree =>
      Ok(Json.toJson(tree))
    }
  }

  def assets(
    fileKey: String,
    assetsIds: Option[Seq[String]],
    scale: Option[Double],
    format: Option[String]
  ) = Action.async { implicit request =>
    assetsIds match {
      case Some(ids) if !ids.isEmpty =>
        figmaService
          .fetchAssets(fileKey, ids, scale, format)
          .map(resp => Ok(Json.toJson(resp)))
      case _ => Future.successful(BadRequest(Json.obj(("errors", "empty ids"))))
    }
  }

}
