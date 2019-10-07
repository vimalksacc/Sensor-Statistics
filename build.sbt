name := "SensorStatisticsTask"

version := "0.1"

scalaVersion := "2.13.1"

libraryDependencies += "com.typesafe.akka" %% "akka-stream" % "2.5.25"
libraryDependencies += "com.typesafe.akka" %% "akka-stream-testkit" % "2.5.25" % Test
libraryDependencies += "org.scalactic" %% "scalactic" % "3.0.8"
libraryDependencies += "org.scalatest" %% "scalatest" % "3.0.8" % "test"
