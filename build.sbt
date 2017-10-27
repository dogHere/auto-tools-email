name := "scala-email"

version := "0.2.2"

scalaVersion := "2.12.3"

libraryDependencies ++= Seq(
  "org.scala-lang" % "scala-compiler" % "2.12.3"
)
// https://mvnrepository.com/artifact/javax.mail/mail
libraryDependencies += "javax.mail" % "mail" % "1.4.7"
