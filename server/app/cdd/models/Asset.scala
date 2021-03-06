package cdd.models

import play.api.libs.json.{Format, Json}

case class Asset(id: String, url: String, name: String) {
  def toPushable: PushableAsset = PushableAsset(name.replace(' ', '-').toLowerCase, "png", "images", url)
}

object Asset {
  implicit val format: Format[Asset] = Json.format[Asset]
}

case class AssetResponse(id: String, url: String)

object AssetResponse {
  implicit val format: Format[AssetResponse] = Json.format[AssetResponse]
}

case class UpdateAsset(branch: String, content: String)

object UpdateAsset {
  implicit val cfFormat: Format[UpdateAsset] = Json.format[UpdateAsset]
}

case class PushableAsset(name: String, extension: String, path: String, url: String)

object PushableAsset {
  implicit val paf: Format[PushableAsset] = Json.format[PushableAsset]
}

case class Id(id: String)

object Id {
  implicit val idf: Format[Id] = Json.format[Id]
}
