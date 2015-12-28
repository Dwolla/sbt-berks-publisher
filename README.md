# Berks Publisher Plugin

SBT Plugin that adds tasks to package and publish [Berkshelf](http://berkshelf.com) dependencies.

Requires the [`berks`](http://berkshelf.com) and [`aws`](https://aws.amazon.com/cli/) tools to be on the path (although a future version may use the AWS SDK instead of the AWS CLI tools).

## Installation and Enabling

In `project/plugins.sbt`, add the following:

    addSbtPlugin("com.dwolla.sbt" % "berks-publisher" % "1.0.2")

    resolvers += "artifactory" at "http://artifactory.dwolla.net:8081/artifactory/repo"

The plugin will be automatically enabled.

## `berks:package` Task

Runs `berks package {output-path}`, placing the packaged tarball in the build’s output directory.

 - `packageName` is set to `cookbooks.tar.gz` by default, but can be overridden:

        packageName in Berks := "anything-you-want.tgz"

 - The build output directory is set to `target/` by default, but can be overridden:

        target := file("build")

## `berks:publish` Task

Publishes the output of `berks:package` to S3. By default, the tarball will be placed in `s3://dwolla-code/{normalized-project-name}/{project-version}/{packageName}`.

 - `s3Bucket` is set to `dwolla-code` by default, but can be overridden:

        s3Bucket in Berks := "my-bucket"

 - `s3Key` is set to `s"${normalizedName.value}/${version.value}/${packageName.value}"` by default, but can be overridden:

        s3Key in Berks := "key-name"
