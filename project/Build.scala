import sbt._
import Keys._
import com.typesafe.sbt.SbtSite.site
import com.typesafe.sbteclipse.plugin.EclipsePlugin.EclipseKeys
import com.typesafe.sbteclipse.plugin.EclipsePlugin.EclipseCreateSrc
import com.github.retronym.SbtOneJar

/**
 * gpg --keyserver hkp://pool.sks-keyservers.net  --no-permission-warning --send-keys 331928A8
 */
object ServerDumper extends Build {
 
  val sprayVersion = "1.2-RC2"
  
  lazy val project = Project(id = "server-dumper", base = file("."), settings = 
    Project.defaultSettings ++ 
	SbtOneJar.oneJarSettings ++    
    publishSettings ++
    site.settings ++ 
    site.sphinxSupport() ++ site.includeScaladoc() ++
    Seq(
	mainClass in (Compile, run) := Some("server.dumper.Main"),
    sbtPlugin := false,
    publishMavenStyle := false,
    exportJars := true,      
    organization := "me",
    version := "0.0.1-SNAPSHOT",
    scalaVersion := "2.10.3",
    scalacOptions += "-deprecation",
    scalacOptions += "-unchecked",
    EclipseKeys.createSrc := EclipseCreateSrc.Default + EclipseCreateSrc.Resource,
    EclipseKeys.withSource := true,
    resolvers += "spray repo" at "http://repo.spray.io",
    resolvers += "Sonatype Snapshots" at "https://oss.sonatype.org/content/repositories/snapshots/",
    libraryDependencies += "org.scalatest" %% "scalatest" % "2.0" % "test" withSources(),
    libraryDependencies += "org.pegdown" % "pegdown" % "1.4.1" % "test" withSources(), // used by scalatest
    libraryDependencies += "com.typesafe.akka" %% "akka-actor" % "2.2.3" withSources(),
    libraryDependencies += "com.typesafe.akka" %% "akka-cluster" % "2.2.3" withSources(),
    libraryDependencies += "com.typesafe.akka" %% "akka-contrib" % "2.2.3" withSources(),
    libraryDependencies += "io.spray" % "spray-can" % sprayVersion withSources(),
    libraryDependencies += "io.spray" % "spray-client" % sprayVersion withSources(),
    libraryDependencies += "io.spray" %%  "spray-json" % "1.2.5" withSources(),
    libraryDependencies += "io.spray" % "spray-servlet" % sprayVersion withSources(),
    libraryDependencies += "io.spray" % "spray-routing" % sprayVersion withSources(),
    libraryDependencies += "io.spray" % "spray-testkit" % sprayVersion % "test" withSources(),
    libraryDependencies += "io.spray" % "spray-caching" % sprayVersion withSources(),
    libraryDependencies += "org.scalastuff" %% "esclient" % "0.20.3" withSources(),
    libraryDependencies += "org.clapper" %% "grizzled-slf4j" % "1.0.1" withSources(),
    libraryDependencies += "org.clapper" %% "argot" % "1.0.1" withSources(),
    libraryDependencies +=  "ch.qos.logback" % "logback-classic" % "1.0.13" withSources(),
    libraryDependencies += "com.typesafe" % "config" % "1.0.0" withSources(),
    libraryDependencies += "joda-time" % "joda-time" % "2.3" withSources(),
    libraryDependencies += "org.joda" % "joda-convert" % "1.5" withSources(), // for class file error in joda-time
    libraryDependencies += "com.google.guava" % "guava" % "14.0.1" withSources(),
    libraryDependencies += "com.google.code.findbugs" % "jsr305" % "2.0.1" // for class file error in guava
))


  def publishSettings = Seq(
    licenses := Seq("The Apache Software Licence, Version 2.0" -> url("http://www.apache.org/licenses/LICENSE-2.0")),
    homepage := Some(url("https://github.com/cristiboariu/server-dumper")),
    pomIncludeRepository := { _ => false },
    publishMavenStyle := true,
    publishArtifact in Test := false,
    credentials += Credentials(Path.userHome / ".ivy2" / ".credentials"),
    pomExtra := <scm>
                  <connection>scm:git:git@github.com:cristiboariu/server-dumper.git</connection>
                  <url>https://github.com/cristiboariu/server-dumper</url>
                </scm>
                <developers>
                  <developer>
                    <id>cristiboariu</id>
                    <name>Cristi Boariu</name>
                    <url>https://github.com/cristiboariu</url>
                  </developer>
                </developers>,
    publishTo <<= version { (v: String) =>
          val nexus = "https://oss.sonatype.org/"
          if (v.trim.endsWith("SNAPSHOT")) 
            Some("snapshots" at nexus + "content/repositories/snapshots") 
          else
            Some("releases"  at nexus + "service/local/staging/deploy/maven2")
        })
                
}



