package core.utils

/* Class used to load and store required configuration for the application */
class Settings(conf: Configuration) {

  val apiKeyHeader = conf.getString("application.api-key.header").required
}
