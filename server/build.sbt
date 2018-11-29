organization in ThisBuild := "io.zengularity"
scalaVersion in ThisBuild := "2.12.4"

name := """new-project"""

val reactiveMongoVersion = "0.12.3"

lazy val root = (project in file("."))
  .enablePlugins(PlayScala, BuildInfoPlugin, SbtWeb)
  .settings(Settings.commonPlayFront: _*)
  .settings(
    libraryDependencies ++= Seq(
      )
  )

scapegoatVersion in ThisBuild := "1.3.3"

resolvers += Resolver.sbtPluginRepo("releases")
