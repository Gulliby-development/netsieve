ThisBuild / scalaVersion := "3.3.7"
ThisBuild / version := "0.1.0"
ThisBuild / scalacOptions ++= Seq("-deprecation", "-feature", "-Werror", "-Wunused:all")

lazy val root = (project in file("."))
  .settings(
    name := "netsieve",
    libraryDependencies += "org.scalameta" %% "munit" % "1.0.4" % Test
  )

lazy val showClasspath = taskKey[Unit]("Prints the run-time class path")

showClasspath := Def.uncached {
  val conv = fileConverter.value
  println((Runtime / fullClasspath).value.map(a => conv.toPath(a.data).toString).mkString(java.io.File.pathSeparator))
}
