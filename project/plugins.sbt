// Comment to get more information during initialization
logLevel := Level.Warn

// The Typesafe repository 
resolvers += "Typesafe repository" at "http://repo.typesafe.com/typesafe/releases/"

// Use the Play sbt plugin for Play projects
addSbtPlugin("com.typesafe.play" % "sbt-plugin" % "2.2.1")

libraryDependencies ++= Seq(
  "net.sf.barcode4j" % "barcode4j" % "2.0" ,
  "org.scalautils" % "scalautils_2.10" % "2.0"
)
