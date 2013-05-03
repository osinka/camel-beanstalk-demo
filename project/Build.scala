import sbt._
import sbt.Keys._
import com.typesafe.sbt.SbtStartScript

object CamelDemoBuild extends Build {

  lazy val buildSettings = Seq(
    organization := "com.osinka.camel",
    version      := "1.5.0-SNAPSHOT",
    scalaVersion := "2.9.1"
  )

  override lazy val settings = super.settings ++ buildSettings

  lazy val defaultSettings = Project.defaultSettings ++ Seq(
    resolvers += "Typesafe Repo" at "http://repo.typesafe.com/typesafe/releases/",
    scalacOptions ++= Seq("-encoding", "UTF-8", "-deprecation", "-unchecked"),
    testOptions in Test += Tests.Argument("-oF")
  )

  val Camel             = "2.11.0"
  val TwitterUtil       = "6.2.3"
  val Slf4j             = "1.7.4"

  lazy val libDependencies = Seq(
    "org.apache.camel"    % "camel-core"              % Camel,
    "org.apache.camel"    % "camel-spring"            % Camel,
    "com.osinka.camel"    % "camel-beanstalk"         % "1.7.0",
    "com.typesafe"        % "config"                  % "1.0.0",
    "org.slf4j"           % "slf4j-simple"            % Slf4j
  )

  lazy val root = Project(
    id = "beanstalk-camel-demo",
    base = file("."),
    settings = defaultSettings ++ SbtStartScript.startScriptForClassesSettings ++ Seq(
      mainClass in Compile := Some("org.apache.camel.spring.Main"),
      libraryDependencies ++= libDependencies
    )
  )
}
