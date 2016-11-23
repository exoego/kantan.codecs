import sbtunidoc.Plugin.UnidocKeys._



// - Dependency versions -----------------------------------------------------------------------------------------------
// ---------------------------------------------------------------------------------------------------------------------
val catsVersion                = "0.8.1"
val disciplineVersion          = "0.7.2"
val impVersion                 = "0.3.0"
val jodaConvertVersion         = "1.8.1"
val jodaVersion                = "2.9.6"
val scalacheckVersion          = "1.13.4"
val scalacheckShapelessVersion = "1.1.4"
val scalatestVersion           = "3.0.1"
val scalazVersion              = "7.2.7"
val shapelessVersion           = "2.3.2"


kantanProject in ThisBuild := "codecs"


// - root projects -----------------------------------------------------------------------------------------------------
// ---------------------------------------------------------------------------------------------------------------------
lazy val root = Project(id = "kantan-codecs", base = file("."))
  .settings(moduleName := "root")
  .enablePlugins(UnpublishedPlugin)
  .aggregate(core, laws, catsLaws, scalazLaws, shapelessLaws, cats, scalaz, shapeless, jodaTime, jodaTimeLaws, docs,
    tests)
  .aggregate(ifJava8[ProjectReference](java8, java8Laws):_*)
  .dependsOn(core)

lazy val tests = project
  .enablePlugins(UnpublishedPlugin)
  .dependsOn(core, laws, catsLaws, scalazLaws, jodaTimeLaws, shapelessLaws)
  .aggregate(ifJava8[ProjectReference](java8Tests):_*)
  .settings(libraryDependencies += "org.scalatest" %% "scalatest" % scalatestVersion % "test")

lazy val docs = project
  .settings(unidocProjectFilter in (ScalaUnidoc, unidoc) :=
    inAnyProject -- inProjects(ifNotJava8[ProjectReference](java8, java8Laws, java8Tests):_*)
  )
  .enablePlugins(DocumentationPlugin)
  .dependsOn(core, laws, catsLaws, scalazLaws, shapelessLaws, cats, scalaz, shapeless, jodaTime, jodaTimeLaws)
  .dependsOn(ifJava8[ClasspathDep[ProjectReference]](java8, java8Laws):_*)



// - core projects -----------------------------------------------------------------------------------------------------
// ---------------------------------------------------------------------------------------------------------------------
lazy val core = project
  .settings(
    moduleName := "kantan.codecs",
    name       := "core"
  )
  .settings(libraryDependencies += "org.spire-math" %% "imp" % impVersion)
  .enablePlugins(PublishedPlugin)

lazy val laws = project
  .settings(
    moduleName := "kantan.codecs-laws",
    name       := "laws"
  )
  .enablePlugins(BoilerplatePlugin)
  .enablePlugins(PublishedPlugin)
  .dependsOn(core)
  .settings(libraryDependencies ++= Seq(
    "org.scalacheck" %% "scalacheck" % scalacheckVersion,
    "org.typelevel"  %% "discipline" % disciplineVersion
  ))


// - cats projects -----------------------------------------------------------------------------------------------------
// ---------------------------------------------------------------------------------------------------------------------
lazy val cats = project
  .settings(
    moduleName := "kantan.codecs-cats",
    name       := "cats"
  )
  .enablePlugins(PublishedPlugin)
  .dependsOn(core)
  .settings(libraryDependencies += "org.typelevel" %% "cats" % catsVersion)

lazy val catsLaws = Project(id = "cats-laws", base = file("cats-laws"))
  .settings(
    moduleName := "kantan.codecs-cats-laws",
    name       := "cats-laws"
  )
  .enablePlugins(PublishedPlugin)
  .dependsOn(core, laws, cats)
  .settings(libraryDependencies ++= Seq(
    "org.typelevel" %% "cats"      % catsVersion,
    "org.typelevel" %% "cats-laws" % catsVersion
  ))



// - joda-time projects ------------------------------------------------------------------------------------------------
// ---------------------------------------------------------------------------------------------------------------------
lazy val jodaTime = Project(id = "joda-time", base = file("joda-time"))
  .settings(
    moduleName := "kantan.codecs-joda-time",
    name       := "joda-time"
  )
  .enablePlugins(PublishedPlugin)
  .dependsOn(core)
  .settings(libraryDependencies ++= Seq(
    "joda-time" % "joda-time"    % jodaVersion,
    "org.joda"  % "joda-convert" % jodaConvertVersion
  ))

lazy val jodaTimeLaws = Project(id = "joda-time-laws", base = file("joda-time-laws"))
  .settings(
    moduleName := "kantan.codecs-joda-time-laws",
    name       := "joda-time-laws"
  )
  .enablePlugins(PublishedPlugin)
  .dependsOn(core, laws, jodaTime)



// - java8 projects ----------------------------------------------------------------------------------------------------
// ---------------------------------------------------------------------------------------------------------------------
lazy val java8 = project
  .settings(
    moduleName    := "kantan.codecs-java8",
    name          := "java8"
  )
  .enablePlugins(PublishedPlugin)
  .dependsOn(core)

lazy val java8Laws = Project(id = "java8-laws", base = file("java8-laws"))
  .settings(
    moduleName    := "kantan.codecs-java8-laws",
    name          := "java8-laws"
  )
  .enablePlugins(PublishedPlugin)
  .dependsOn(core, laws, java8)

lazy val java8Tests = Project(id = "java8-tests", base = file("java8-tests"))
  .enablePlugins(UnpublishedPlugin)
  .dependsOn(java8, java8Laws)
  .settings(libraryDependencies += "org.scalatest" %% "scalatest" % scalatestVersion % "test")




// - scalaz projects ---------------------------------------------------------------------------------------------------
// ---------------------------------------------------------------------------------------------------------------------
lazy val scalaz = project
  .settings(
    moduleName := "kantan.codecs-scalaz",
    name       := "scalaz"
  )
  .enablePlugins(PublishedPlugin)
  .dependsOn(core)
  .settings(libraryDependencies += "org.scalaz" %% "scalaz-core" % scalazVersion)

lazy val scalazLaws = Project(id = "scalaz-laws", base = file("scalaz-laws"))
  .settings(
    moduleName := "kantan.codecs-scalaz-laws",
    name       := "scalaz-laws"
  )
  .enablePlugins(PublishedPlugin)
  .dependsOn(core, laws, scalaz)
  .settings(libraryDependencies ++= Seq(
    "org.scalaz"    %% "scalaz-core"               % scalazVersion,
    "org.scalaz"    %% "scalaz-scalacheck-binding" % (scalazVersion + "-scalacheck-1.13"),
    "org.scalatest" %% "scalatest"                 % scalatestVersion % "optional"
  ))



// - shapeless projects ------------------------------------------------------------------------------------------------
// ---------------------------------------------------------------------------------------------------------------------
lazy val shapeless = project
  .settings(
    moduleName := "kantan.codecs-shapeless",
    name       := "shapeless"
  )
  .enablePlugins(PublishedPlugin)
  .dependsOn(core)
  .settings(libraryDependencies += "com.chuusai" %% "shapeless" % shapelessVersion)

lazy val shapelessLaws = Project(id = "shapeless-laws", base = file("shapeless-laws"))
  .settings(
    moduleName := "kantan.codecs-shapeless-laws",
    name       := "shapeless-laws"
  )
  .settings(
    libraryDependencies += "com.github.alexarchambault" %% "scalacheck-shapeless_1.13" % scalacheckShapelessVersion
  )
  .enablePlugins(PublishedPlugin)
  .dependsOn(core, laws, shapeless)
