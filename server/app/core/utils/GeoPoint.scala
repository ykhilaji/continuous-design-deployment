package core.utils

import play.api.libs.json.{Json, Reads, Writes}

case class GeoPoint(lat: Double, lon: Double)
object GeoPoint {
  implicit val geoPointReader: Reads[GeoPoint] = Json.reads[GeoPoint]
  implicit val geoPointWriter: Writes[GeoPoint] = Json.writes[GeoPoint]
}
