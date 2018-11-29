package user.models

import java.util.UUID

import play.api.libs.json.{Format, Json}

case class Cagnotte(
  id: UUID,
  solde: BigDecimal
)

object Cagnotte {
  implicit val format: Format[Cagnotte] = Json.format[Cagnotte]
}
