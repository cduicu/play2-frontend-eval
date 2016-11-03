name := """buildhub"""
version := "1.0-SNAPSHOT"
scalaVersion := "2.11.7"
val sigmaRepo = "http://artifactory:8081/artifactory/"
val ngVersion="2.1.1" // angular2 version

incOptions := incOptions.value.withNameHashing(true)
updateOptions := updateOptions.value.withCachedResolution(true)

lazy val root = (project in file(".")).enablePlugins(PlayJava, SbtWeb)

libraryDependencies ++= Seq(
  cache, javaWs,
  "org.julienrf" %% "play-jsmessages" % "2.0.0",
  "org.webjars" %% "webjars-play" % "2.5.0",
  "org.webjars" % "jquery" % "3.1.1",
  "org.webjars.bower" % "materialize" % "0.97.7",
  "org.webjars.bower" % "momentjs" % "2.15.1", // show duration in human readable format

  // *****************************************************************
  // for reactJS
  "org.webjars.bower" % "react" % "15.3.2",
  "org.webjars.bower" % "babel-standalone" % "6.10.3",
  "org.webjars.npm" % "react-materialize" % "0.14.4",
  // *****************************************************************

  // *****************************************************************
  //angular2 dependencies
  "org.webjars.npm" % "angular__common" % ngVersion,
  "org.webjars.npm" % "angular__compiler" % ngVersion,
  "org.webjars.npm" % "angular__core" % ngVersion,
  "org.webjars.npm" % "angular__forms" % ngVersion,
  "org.webjars.npm" % "angular__http" % ngVersion,
  "org.webjars.npm" % "angular__platform-browser" % ngVersion,
  "org.webjars.npm" % "angular__platform-browser-dynamic" % ngVersion,
  "org.webjars.npm" % "angular__upgrade" % ngVersion,
  //"org.webjars.npm" % "angular__router" % "3.1.2",
  "org.webjars.npm" % "angular-in-memory-web-api" % "0.1.13",
  "org.webjars.npm" % "systemjs" % "0.19.39",
  "org.webjars.npm" % "rxjs" % "5.0.0-beta.12",
  "org.webjars.npm" % "es6-promise" % "3.1.2",
  "org.webjars.npm" % "es6-shim" % "0.35.1",
  "org.webjars.npm" % "reflect-metadata" % "0.1.8",
  "org.webjars.npm" % "zone.js" % "0.6.26",
  "org.webjars.npm" % "core-js" % "2.4.1",
  "org.webjars.npm" % "symbol-observable" % "1.0.1",
  // typescript
  "org.webjars.npm" % "typescript" % "2.0.6"
  //tslint dependency
  //"org.webjars.npm" % "tslint-eslint-rules" % "2.1.0",
  //"org.webjars.npm" % "codelyzer" % "0.0.28"
  //"org.webjars.npm" % "types__jasmine" % "2.2.26-alpha" % "test"
  // *****************************************************************

)

// the typescript typing information is by convention in the typings directory
// It provides ES6 implementations. This is required when compiling to ES5.
typingsFile := Some(baseDirectory.value / "typings" / "index.d.ts")

// use the webjars npm directory (target/web/node_modules ) for resolution of module imports of angular2/core etc
resolveFromWebjarsNodeModulesDir := true

// use the combined tslint and eslint rules plus ng2 lint rules
//(rulesDirectories in tslint) := Some(List(
//  tslintEslintRulesDir.value,
//  ng2LintRulesDir.value
//))

resolvers ++= Seq(Resolver.mavenLocal, "Sigma Releases Repository" at sigmaRepo + "libs-release")

// only generate accessors for models
sources in(Compile, playEnhancerGenerateAccessors) := {
  ((javaSource in Compile).value / "models" ** "*.java").get
}

