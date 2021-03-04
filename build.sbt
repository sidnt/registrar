ThisBuild / scalaVersion     := "2.13.5"
ThisBuild / version          := "0.1.0-SNAPSHOT"
ThisBuild / organization     := "com.example"
ThisBuild / organizationName := "example"

lazy val root = (project in file("."))
  .settings(
    name := "registrar",
    libraryDependencies ++= Seq(
      "dev.zio" %% "zio" % "1.0.4-2",
      "dev.zio" %% "zio-test" % "1.0.4-2" % Test,
      "com.github.ghostdogpr" %% "caliban" % "0.9.5"
    ),
    testFrameworks += new TestFramework("zio.test.sbt.ZTestFramework")
  )
