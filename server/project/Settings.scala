import com.lucidchart.sbt.scalafmt.ScalafmtCorePlugin.autoImport._
import com.sksamuel.scapegoat.sbt.ScapegoatSbtPlugin.autoImport._
import sbt._
import sbt.Keys._
import com.typesafe.sbt.digest.SbtDigest.autoImport._
import com.typesafe.sbt.gzip.SbtGzip.autoImport._
import com.typesafe.sbt.packager.Keys._
import com.typesafe.sbt.packager.universal.UniversalPlugin.autoImport._
import com.typesafe.sbt.web.SbtWeb.autoImport._
import play.sbt.PlayImport.{PlayKeys, ws}
import play.sbt.routes.RoutesKeys
import play.twirl.sbt.Import.TwirlKeys
import sbt.internal.io.Source
import sbtbuildinfo.BuildInfoPlugin.autoImport._
import sbtrelease.ReleasePlugin.autoImport._

object Settings {
  val timestamp = new java.text.SimpleDateFormat("yyyyMMdd-HHmm").format(new java.util.Date())

  lazy val common = Seq(
    scalacOptions := Seq(
      "-encoding",
      "UTF-8",
      "-target:jvm-1.8",
      "-Ywarn-adapted-args",
      "-Ywarn-inaccessible",
      "-Ywarn-nullary-override",
      "-Ywarn-infer-any",
      "-Ywarn-dead-code",
      "-Ywarn-unused",
      "-Ywarn-unused-import",
      "-Ywarn-value-discard",
      "-Ywarn-macros:after",
      "-Ywarn-numeric-widen",
      "-Ypartial-unification",
      "-unchecked",
      "-deprecation",
      "-feature",
      "-g:vars",
      "-Xlint:_",
      "-opt:l:inline",
      "-opt-inline-from"
    ),
    // name dist with timestamp
    packageName in Universal := s"${name.value}-${version.value}-$timestamp",
    // skip scaladoc when running dist
    publishArtifact in (Compile, packageDoc) := false,
    publishArtifact in packageDoc := false,
    sources in (Compile, doc) := Seq.empty
  ) ++ scapegoatSettings ++ scalaFmtSettings

  lazy val commonPlay = common ++ Seq(
    buildInfoPackage := "sbt",
    buildInfoKeys := Seq[BuildInfoKey](
      name,
      version,
      scalaVersion,
      BuildInfoKey.constant("buildTime" -> timestamp)
    ),
    RoutesKeys.routesImport := Seq(),
    TwirlKeys.templateImports := Seq(),
    // dont include local.conf in dist
    mappings in Universal := {
      val origMappings = (mappings in Universal).value
      origMappings.filterNot { case (_, file) => file.endsWith("local.conf") }
    },
    libraryDependencies ++= Seq(
      ws,
      // enum utils
      Dependencies.enumeratum.core,
      Dependencies.enumeratum.play,
      // metrics
      Dependencies.kamon.core,
      // needed because ws depends on a shaded version of the async-http-client
      (Dependencies.kamon.influxdb).exclude("org.asynchttpclient", "async-http-client"),
      Dependencies.kamon.system,
      // Test dependencies
      Dependencies.scalatest.play % Test,
    ),
    releaseProcess := releaseSteps
  )

  lazy val commonPlayFront = commonPlay ++ Seq(
    // avoid unnecessary reloading for public files
    watchSources := {
      val publicDir = s"${baseDirectory.value}/public"
      watchSources.value.filterNot { source =>
        val sourceFile = getSourceFile(source)
        sourceFile.getAbsolutePath.contains(publicDir)
      }
    },
    PlayKeys.playMonitoredFiles := {
      val publicDir = s"${baseDirectory.value}/public"
      PlayKeys.playMonitoredFiles.value
        .filterNot(file => file.getAbsolutePath.contains(publicDir))
    },
    // asset pipeline
    pipelineStages := Seq(digest, gzip),
    includeFilter in digest := "*.js" || "*.css" || "*.html",
    includeFilter in gzip := "*.js" || "*.css" || "*.html",
    libraryDependencies ++= Seq(
      // i18n
      Dependencies.jsMessages.core,
      Dependencies.playI18nHocon.core,
    )
  )

  lazy val releaseSteps = {
    import ReleaseTransformations._
    Seq[ReleaseStep](
      runClean,
      checkSnapshotDependencies,
      inquireVersions,
      runTest,
      setReleaseVersion,
      commitReleaseVersion,
      tagRelease,
      setNextVersion,
      commitNextVersion,
      pushChanges
    )
  }

  lazy val scapegoatSettings = Seq(
    scapegoatVersion := "1.3.3",
    scapegoatReports := Seq("xml", "html"),
    scapegoatConsoleOutput := false
  )

  lazy val scalaFmtSettings = Seq(
    scalafmtVersion := "1.3.0",
    scalafmtOnCompile := true
  )

  def getSourceFile(source: Source): java.io.File = {
    val cl = classOf[Source]
    val baseField = cl.getDeclaredField("base")
    baseField.setAccessible(true)
    baseField.get(source).asInstanceOf[java.io.File]
  }
}