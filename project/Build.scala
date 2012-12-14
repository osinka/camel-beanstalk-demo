import sbt._
import sbt.Keys._
import com.typesafe.sbt.SbtStartScript

object MiddleBuild extends Build {

  lazy val buildSettings = Seq(
    organization := "com.osinka.camel",
    version      := "1.5.0-SNAPSHOT",
    scalaVersion := "2.9.1"
  )

  override lazy val settings = super.settings ++ buildSettings

  lazy val defaultSettings = Project.defaultSettings ++ Seq(
    resolvers += "Typesafe Repo" at "http://repo.typesafe.com/typesafe/releases/",
    resolvers += "Twitter Public Repo" at "http://maven.twttr.com/",
    resolvers += "Sonatype Repo" at "https://oss.sonatype.org/content/groups/public",
    resolvers += "Local Maven Repository" at ("file://" + Path.userHome.absolutePath + "/.m2/repository"),

    // compile options
    scalacOptions ++= Seq("-encoding", "UTF-8", "-deprecation", "-unchecked"),

    // show full stack traces
    testOptions in Test += Tests.Argument("-oF"),
    // for packaging in jar
    exportJars := true
  )

  val Camel             = "2.10.3"
  val TwitterUtil       = "6.0.1"
  val Slf4j             = "1.7.2"

  lazy val libDependencies = Seq(
    "org.apache.camel"    % "camel-core"              % Camel,
    "com.osinka.camel"    % "camel-beanstalk"         % "1.5.1",
    "com.twitter"         % "util-eval"               % TwitterUtil,
    "org.slf4j"           % "slf4j-simple"            % Slf4j
  )

  lazy val root = Project(
    id = "beanstalk-camel-demo",
    base = file("."),
    settings = defaultSettings ++ SbtStartScript.startScriptForClassesSettings ++ Seq(
      mainClass in run in Compile := Some("com.osinka.beanstalk.camel.demo.Main"),
      libraryDependencies ++= libDependencies
    )
  )
}
