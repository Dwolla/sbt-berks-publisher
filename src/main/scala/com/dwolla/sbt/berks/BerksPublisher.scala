package com.dwolla.sbt.berks

import sbt.{AutoPlugin, Keys, file}
import sbt.Keys._
import scala.sys.process._

import scala.language.postfixOps

object BerksPublisher extends AutoPlugin {

  override def trigger = allRequirements

  object autoImport extends BerksPublisherKeys

  import autoImport._

  lazy val defaults = Seq(
    projectName := normalizedName.value,
    packageName := "cookbooks.tar.gz",
    s3Key := s"${projectName.value}/${version.value}/${packageName.value}"
  )

  private val packageInBerks = Keys.`package` in Berks

  lazy val tasks = Seq(
    packageInBerks := {
      val filename = packageName.value
      val targetPath = s"${target.value}/$filename"
      berksPackageProcess(targetPath) ! match {
        case 0 ⇒ file(targetPath)
        case exitCode ⇒ throw new Error(s"Berkshelf packaging failed with exit value $exitCode")
      }
    },

    publish in Berks := {
      val s3Url = s"s3://${s3Bucket.value}/${s3Key.value}"
      val exitCode: Int = awsS3CpProcess(packageInBerks.value.getPath, s3Url) !

      if (exitCode != 0)
        throw new Error(s"S3 publishing failed with exit value $exitCode")
    }
  )

  lazy val berksPublisher = defaults ++ tasks

  override lazy val projectSettings = berksPublisher

  private def berksPackageProcess(filename: String): ProcessBuilder = Process("berks" :: "package" :: filename :: Nil)
  private def awsS3CpProcess(source: String, destination: String): ProcessBuilder = Process("aws" :: "s3" :: "cp" :: source :: destination :: Nil)
}
