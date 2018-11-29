package user.models

import java.util.UUID

import play.api.libs.json.{Format, Json}

case class Commercant(
  id: UUID,
  solde: BigDecimal,
  id_cagnotte: UUID,
  geoloc: (Double, Double)
)

object Commercant {
  implicit val format: Format[Commercant] = Json.format[Commercant]
}
