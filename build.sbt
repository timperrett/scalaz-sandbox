
name := "scalaz7-sandbox"

scalaVersion := "2.10.0"

libraryDependencies += "org.scalaz" %% "scalaz-core" % "7.0.0-M7"

resolvers += "sontatype.snapshots" at "https://oss.sonatype.org/content/repositories/releases/"

// mainClass := Some("demo.PartyTime")

scalacOptions ++= Seq("-deprecation")
// scalacOptions ++= Seq("-Xprint:jvm", "-optimise")
