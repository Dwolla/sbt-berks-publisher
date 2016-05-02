import java.lang.System._

lazy val buildVersion = {
  val mainVersion = "1.1"
  val minorVersion = Option(getenv("TRAVIS_BUILD_NUMBER"))
  minorVersion match {
    case Some(v: String) ⇒ s"$mainVersion.$v"
    case None ⇒ mainVersion + "-SNAPSHOT"
  }
}

lazy val buildSettings = Seq(
  organization := "com.dwolla.sbt",
  name := "berks-publisher",
  homepage := Some(url("https://github.com/Dwolla/sbt-berks-publisher")),
  description := "SBT plugin to add Chef Berkshelf tasks to sbt, so they can more easily be integrated into the Scala build process.",
  licenses += ("MIT", url("http://opensource.org/licenses/MIT")),
  version := buildVersion,
  scalaVersion := "2.10.6",
  sbtPlugin := true,
  startYear := Option(2016)
)

lazy val bintraySettings = Seq(
  bintrayVcsUrl := Some("https://github.com/Dwolla/sbt-docker-containers"),
  publishMavenStyle := false,
  bintrayRepository := "sbt-plugins",
  bintrayOrganization := Option("dwolla"),
  pomIncludeRepository := { _ ⇒ false }
)

lazy val pipeline = TaskKey[Unit]("pipeline", "Runs the full build pipeline: compile, test, integration tests")
pipeline <<= (test in Test)

val berksPublisher = (project in file("."))
  .settings(buildSettings ++ bintraySettings: _*)
