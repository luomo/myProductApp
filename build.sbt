name := "myProductApp"

version := "1.0-SNAPSHOT"

resolvers += "Typesafe repository" at "http://repo.typesafe.com/typesafe/releases/"

resolvers += "Local Maven Repository" at "file:///"+Path.userHome.absolutePath+"/.m2/repository"

resolvers += "Local ivy rep" at "file:///usr/local/Cellar/play/2.2.1/libexec/repository/cache"



libraryDependencies ++= Seq(
  jdbc,
  anorm,
  cache,
  "postgresql" % "postgresql" % "9.1-901.jdbc4",
  "net.sf.barcode4j" % "barcode4j" % "2.0" ,
  "org.scalautils" % "scalautils_2.10" % "2.0",
  "org.apache.derby" % "derby" % "10.4.1.3",
  "javax.xml.bind" % "jaxb-api" % "2.2.2",
  "org.squeryl" %% "squeryl" % "0.9.5-6"
)

libraryDependencies += "mysql" % "mysql-connector-java" % "5.1.27"

play.Project.playScalaSettings

