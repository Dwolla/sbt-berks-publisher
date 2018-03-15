lazy val buildSettings = Seq(
  organization := "com.dwolla.sbt",
  name := "berks-publisher",
  homepage := Some(url("https://github.com/Dwolla/sbt-berks-publisher")),
  description := "SBT plugin to add Chef Berkshelf tasks to sbt, so they can more easily be integrated into the Scala build process.",
  licenses += ("MIT", url("http://opensource.org/licenses/MIT")),
  crossSbtVersions := Vector("1.1.1", "0.13.17"),
  sbtPlugin := true,
  startYear := Option(2016),
)

lazy val releaseSettings = {
  import ReleaseTransformations._
  import sbtrelease.Version.Bump._
  Seq(
    releaseVersionBump := Minor,
    releaseProcess := Seq[ReleaseStep](
      checkSnapshotDependencies,
      inquireVersions,
      runClean,
      releaseStepCommandAndRemaining("^ test"),
      setReleaseVersion,
      commitReleaseVersion,
      tagRelease,
      releaseStepCommandAndRemaining("^ publish"),
      setNextVersion,
      commitNextVersion,
      pushChanges
    )
  )
}

lazy val bintraySettings = Seq(
  bintrayVcsUrl := Some("https://github.com/Dwolla/sbt-berks-publisher"),
  publishMavenStyle := false,
  bintrayRepository := "sbt-plugins",
  bintrayOrganization := Option("dwolla"),
  pomIncludeRepository := { _ ⇒ false }
)

val berksPublisher = (project in file("."))
  .settings(buildSettings ++ bintraySettings: _*)
