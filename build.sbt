import java.lang.System._

import sbt.Keys._

lazy val artifactoryBase = "http://artifactory.dwolla.net:8081/artifactory"

lazy val buildVersion = {
  val mainVersion = "1.0"
  val minorVersion = Option(getenv("BUILD_NUMBER"))
  minorVersion match {
    case Some(v: String) ⇒ mainVersion + "." + v
    case None ⇒ mainVersion + "-SNAPSHOT"
  }
}

lazy val buildSettings = Seq(
  organization := "com.dwolla.sbt",
  name := "Berks Publisher",
  homepage := Some(url("https://stash.dwolla.net/projects/SBT/repos/sbt-berks-publisher/browse")),
  version := buildVersion,
  scalaVersion := "2.10.6",
  sbtPlugin := true,
  resolvers += "artifactory" at s"$artifactoryBase/repo"
)

lazy val publishSettings = Seq(
  credentials += Credentials(Path.userHome / ".ivy2" / ".credentials"),
  publishArtifact in(Compile, packageBin) := true,
  publishArtifact in(Compile, packageDoc) := false,
  publishArtifact in(Compile, packageSrc) := true,
  publishTo := {
    if (buildVersion.trim.endsWith("SNAPSHOT"))
      Some("snapshots" at s"$artifactoryBase/libs-snapshot-local")
    else
      Some("releases" at s"$artifactoryBase/libs-release-local")
  }
)

lazy val pipeline = TaskKey[Unit]("pipeline", "Runs the full build pipeline: compile, test, integration tests")
pipeline <<= (test in Test)

val berksPublisher = (project in file("."))
  .settings(buildSettings: _*)
  .settings(publishSettings: _*)
