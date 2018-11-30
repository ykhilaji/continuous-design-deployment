package cdd.controllers

import play.api.mvc._

import cdd.services.FigmaService

import play.api.libs.json.Json

import scala.concurrent.{ExecutionContext, Future}

class FigmaController(
  cc: ControllerComponents,
  figmaService: FigmaService
)(implicit ec: ExecutionContext)
    extends AbstractController(cc) {

  def listImages(fileKey: String, from: Option[Int], to: Option[Int]) = Action.async { implicit request =>
    for {
      documents <- figmaService.assetsDocuments(fileKey)
      assets <- figmaService.assets(fileKey, documents, from.getOrElse(0), to.getOrElse(20))
    } yield Ok(Json.toJson(assets))
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
