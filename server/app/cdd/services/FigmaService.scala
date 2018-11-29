package cdd.services

import play.api.libs.ws.WSClient
import play.api.libs.json.{Format, JsObject, JsString, JsValue, Json}

import scala.concurrent.{ExecutionContext, Future}

import core.utils.Configuration
import cdd.models.{Asset, AssetResponse}

class FigmaService(
  ws: WSClient,
  config: Configuration
) {
  private val figmaURL = "https://api.figma.com/v1"

  def assetsDocuments(fileKey: String)(implicit ec: ExecutionContext): Future[Seq[Document]] =
    ws.url(s"${figmaURL}/files/${fileKey}")
      .addHttpHeaders("X-Figma-Token" -> config.getString("cdd.figma.token").required)
      .get()
      .map(res => parseDocument((res.json \ "document").as[Document]))

  def parseDocument(document: Document): Seq[Document] =
    document.children match {
      case Some(children) => Seq(document) ++: children.flatMap(doc => parseDocument(doc))
      case None           => Seq(document)
    }

  def assets(fileKey: String, documents: Seq[Document], from: Int = 0, to: Int = 20)(
    implicit ec: ExecutionContext
  ): Future[Seq[Asset]] = {
    val idsComas = documents.map(_.id).slice(from, to).mkString(",")
    val url = s"${figmaURL}/images/${fileKey}?ids=${idsComas}"
    ws.url(url)
      .addHttpHeaders("X-Figma-Token" -> config.getString("cdd.figma.token").required)
      .get()
      .map(res => {
        val assetsResponse: Seq[AssetResponse] = (res.json \ "images").as[JsObject].fields.map {
          case (fieldName, value) => AssetResponse(fieldName, value.as[String])
        }
        assetsResponse.map(
          assetResp =>
            Asset(
              assetResp.id,
              assetResp.url,
              documents.find(doc => doc.id == assetResp.id).map(doc => doc.name).getOrElse("")
          )
        )
      })
  }
}

case class Document(name: String, id: String, children: Option[Seq[Document]])

object Document {
  implicit val format: Format[Document] = Json.format[Document]
}
