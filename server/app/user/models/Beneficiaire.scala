package user.models

import java.util.UUID

import play.api.libs.json.{Format, Json}

case class Beneficiaire(
  id: UUID
)

object Beneficiaire {
  implicit val format: Format[Beneficiaire] = Json.format[Beneficiaire]
}
