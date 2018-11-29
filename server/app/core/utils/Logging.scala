package core.utils

trait Logging {
  val logger = {
    val name = this.getClass.getName.stripSuffix("$")
    play.api.Logger(name)
  }
}
