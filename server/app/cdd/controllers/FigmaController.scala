package cdd.controllers

import play.api.mvc._

import cdd.services.FigmaService

import play.api.libs.json.Json

import scala.concurrent.ExecutionContext

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

}
