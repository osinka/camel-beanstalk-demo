organization := "com.osinka.camel"

name := "camel-beanstalk-demo"

version := "1.5.0-SNAPSHOT"

scalaVersion := "2.10.4"

libraryDependencies ++= Seq(
  "org.apache.camel"    % "camel-core"              % "2.14.0",
  "org.apache.camel"    % "camel-spring"            % "2.14.0",
  "org.apache.camel"    % "camel-disruptor"         % "2.14.0",
  "com.osinka.camel"    % "camel-beanstalk"         % "1.7.0",
  "com.typesafe"        % "config"                  % "1.2.0",
  "org.slf4j"           % "slf4j-simple"            % "1.7.7"
)

resolvers ++= Seq(
  "Typesafe Releases" at "http://repo.typesafe.com/typesafe/releases/"
)

scalacOptions ++= Seq("-encoding", "UTF-8", "-deprecation", "-unchecked")

testOptions in Test += Tests.Argument("-oD")

parallelExecution := false

mainClass in Compile := Some("org.apache.camel.spring.Main")
