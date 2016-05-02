package com.dwolla.sbt.berks

import java.lang.{ProcessBuilder ⇒ JProcessBuilder}

import sbt.Keys._
import sbt._

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
    packageInBerks <<= (target, packageName) map { (target, filename) ⇒
      val targetPath = s"$target/$filename"
      berksPackageProcess(targetPath) ! match {
        case 0 ⇒ file(targetPath)
        case exitCode ⇒ throw new Error(s"Berkshelf packaging failed with exit value $exitCode")
      }
    },

    publish in Berks <<= (packageInBerks, s3Bucket, s3Key) map { (packagedFile, s3Bucket, s3Key) ⇒
      val s3Url = s"s3://$s3Bucket/$s3Key"
      val exitCode: Int = awsS3CpProcess(packagedFile.getPath, s3Url) !

      if (exitCode != 0)
        throw new Error(s"S3 publishing failed with exit value $exitCode")
    }
  )

  lazy val berksPublisher = defaults ++ tasks

  override lazy val projectSettings = berksPublisher

  private def berksPackageProcess(filename: String) = new JProcessBuilder("berks", "package", filename)
  private def awsS3CpProcess(source: String, destination: String) = new JProcessBuilder("aws", "s3", "cp", source, destination)
}
