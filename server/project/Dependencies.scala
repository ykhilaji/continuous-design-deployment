import sbt._

object Dependencies {
  object enumeratum {
    val version = "1.5.12"

    val core = "com.beachape"  %% "enumeratum" % version
    val play = "com.beachape"  %% "enumeratum-play" % version
  }

  object playI18nHocon {
    val version = "1.0.1"
    val core = "com.github.marcospereira" %% "play-hocon-i18n" % version
  }

  object kamon {
    val version = "0.6.7"

    val core = "io.kamon" %% "kamon-core" % version
    val influxdb = "io.kamon" %% "kamon-influxdb" % version
    val system = "io.kamon" %% "kamon-system-metrics" % version
  }

  object scalatest {
    val version = "3.1.0"

    val play = "org.scalatestplus.play" %% "scalatestplus-play" % version
  }

  object jsMessages {
    val version = "3.0.0"

    val core = "org.julienrf" %% "play-jsmessages" % version
  }
}