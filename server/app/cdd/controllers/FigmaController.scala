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

  def listImages(fileKey: String) = Action.async { implicit request =>
    for {
      documents <- figmaService.assetsDocuments(fileKey)
      assets <- figmaService.assets(fileKey, documents)
    } yield Ok(Json.toJson(assets))
  }

}
