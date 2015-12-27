package com.dwolla.sbt.berks

import sbt._

trait BerksPublisherKeys {
  lazy val Berks = config("berks")

  lazy val packageName = settingKey[String]("pathless filename for Berks package") in Berks
  lazy val s3Bucket = settingKey[String]("S3 bucket where the Berks package should be uploaded") in Berks
  lazy val s3Key = settingKey[String]("S3 key where the Berks package should be uploaded") in Berks
}
