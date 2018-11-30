package cdd.services

import play.api.libs.ws.WSClient
import play.api.libs.json.{Format, JsObject, Json}

import scala.concurrent.{ExecutionContext, Future}
import core.utils.Configuration
import cdd.models.{Asset, AssetResponse, Id}

class FigmaService(
  ws: WSClient,
  config: Configuration
) {
  private val figmaURL = "https://api.figma.com/v1"
  private val assetsCache = scala.collection.mutable.Map[(String, Seq[Document], Seq[Id], Int, Int), Seq[Asset]]()
  private val docsCache = scala.collection.mutable.Map[String, Seq[Document]]()
  def assetsDocuments(fileKey: String)(implicit ec: ExecutionContext): Future[Seq[Document]] = {
    docsCache.get(fileKey) match {
      case Some(docs) => {
        println("DOCS FROM CACHE")
        Future.successful(docs)
      }
      case None => {
        ws.url(s"${figmaURL}/files/${fileKey}")
          .addHttpHeaders("X-Figma-Token" -> config.getString("cdd.figma.token").required)
          .get()
          .map { res =>
            val docs = (res.json \ "document")
              .validate[Document]
              .asOpt
              .map { doc =>
                parseDocument(doc)
              }
              .getOrElse(Seq.empty)
            docsCache += (fileKey -> docs)
            docs
          }
      }
    }
  }

  def parseDocument(document: Document): Seq[Document] =
    document.children match {
      case Some(children) => Seq(document) ++: children.flatMap(doc => parseDocument(doc))
      case None           => Seq(document)
    }

  def assets(fileKey: String, documents: Seq[Document], ids: Seq[Id], from: Int = 0, to: Int = 20)(
    implicit ec: ExecutionContext
  ): Future[Seq[Asset]] = {
    assetsCache.get((fileKey, documents, ids, from, to)) match {
      case Some(assets) => {
        println("ASSETS FROM CACHE")
        Future.successful(assets)
      }
      case None => {
        val strIds = ids.map(_.id)
        val idsComas = documents.map(_.id).filter(x => strIds.contains(x)).distinct.slice(from, to).mkString(",")
        val url = s"${figmaURL}/images/${fileKey}?ids=${idsComas}"
        ws.url(url)
          .addHttpHeaders("X-Figma-Token" -> config.getString("cdd.figma.token").required)
          .get()
          .map(res => {
            val assetsResponse: Seq[AssetResponse] = (res.json \ "images").as[JsObject].fields.flatMap {
              case (fieldName, value) => {
                value.validate[String].asOpt match {
                  case Some(url) => Seq(AssetResponse(fieldName, url))
                  case None      => Seq.empty
                }
              }
            }
            val assets = assetsResponse.map { assetResp =>
              Asset(
                assetResp.id,
                assetResp.url,
                documents.find(doc => doc.id == assetResp.id).map(doc => doc.name).getOrElse("")
              )
            }
            assetsCache += ((fileKey, documents, ids, from, to) -> assets)
            assets
          })
      }
    }
  }

  def fetchAssets(
    fileKey: String,
    ids: Seq[String],
    scale: Option[Double] = None,
    format: Option[String] = None
  )(implicit ec: ExecutionContext) = {
    val idsComas = ids.mkString(",")
    val baseUrl = s"${figmaURL}/images/${fileKey}?ids=${idsComas}"
    val url = (scale, format) match {
      case (Some(s), Some(f)) => s"${baseUrl}&scale=${s}&format=${f}"
      case (Some(s), _)       => s"${baseUrl}&scale=${s}"
      case (_, Some(f))       => s"${baseUrl}&format=${f}"
      case _                  => baseUrl
    }

    ws.url(url)
      .addHttpHeaders("X-Figma-Token" -> config.getString("cdd.figma.token").required)
      .get()
      .map(res => {
        (res.json \ "images")
          .as[JsObject]
          .fields
          .flatMap {
            case (fieldName, value) => {
              value.validate[String].asOpt match {
                case Some(url) => Seq(AssetResponse(fieldName, url))
                case None      => Seq.empty
              }

            }
          }
      })
  }

  def documentTree(fileKey: String)(
    implicit ec: ExecutionContext
  ): Future[Option[Document]] = {
    ws.url(s"${figmaURL}/files/${fileKey}")
      .addHttpHeaders("X-Figma-Token" -> config.getString("cdd.figma.token").required)
      .get()
      .map { res =>
        (res.json \ "document")
          .validate[Document]
          .asOpt
      }
  }
}

case class Document(name: String, id: String, `type`: Option[String], children: Option[Seq[Document]])

object Document {
  implicit val format: Format[Document] = Json.format[Document]
}
