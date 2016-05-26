# Berks Publisher Plugin
[![Travis](https://img.shields.io/travis/Dwolla/sbt-berks-publisher.svg?style=flat-square)](https://travis-ci.org/Dwolla/sbt-berks-publisher)
[![Bintray](https://img.shields.io/bintray/v/dwolla/sbt-plugins/berks-publisher.svg?style=flat-square)](https://bintray.com/dwolla/sbt-plugins/berks-publisher/view)
[![license](https://img.shields.io/github/license/Dwolla/sbt-berks-publisher.svg?style=flat-square)]()

SBT Plugin that adds tasks to package and publish [Berkshelf](http://berkshelf.com) dependencies.

Requires the [`berks`](http://berkshelf.com) and [`aws`](https://aws.amazon.com/cli/) tools to be on the path (although a future version may use the AWS SDK instead of the AWS CLI tools).

## Installation and Enabling

In `project/plugins.sbt`, add the following:

    addSbtPlugin("com.dwolla.sbt" % "berks-publisher" % "***VERSION***")

    resolvers ++= Seq(
      Resolver.bintrayIvyRepo("dwolla", "sbt-plugins"),
      Resolver.bintrayIvyRepo("dwolla", "maven")
    )


The plugin will be automatically enabled.

## `berks:package` Task

Runs `berks package {output-path}`, placing the packaged tarball in the build’s output directory.

 - `packageName` is set to `cookbooks.tar.gz` by default, but can be overridden:

        packageName in Berks := "anything-you-want.tgz"

 - The build output directory is set to `target/` by default, but can be overridden:

        target := file("build")

## `berks:publish` Task

Publishes the output of `berks:package` to S3. By default, the tarball will be placed in `s3://{s3Bucket}/{normalized-project-name}/{project-version}/{packageName}`.

 - `projectName` is set to the normalized name of the project by default, but can be overridden if case matters:

        projectName in Berks := "MyProject"

 - `s3Bucket` must be configured:

        s3Bucket in Berks := "my-bucket"

 - `s3Key` is set to `s"${projectName.value}/${version.value}/${packageName.value}"` by default, but can be overridden:

        s3Key in Berks := "key-name"
