package server.dumper

import spray.routing.SimpleRoutingApp
import akka.actor.ActorSystem
import scala.io.Source
import java.io.File
import com.typesafe.config.ConfigFactory

object Main extends App with SimpleRoutingApp {
  implicit val system = ActorSystem("my-system")

  lazy val config = ConfigFactory.load.getConfig(getClass.getPackage.getName)
  
  startServer(interface = config.getString("interface"), port = config.getInt("port")) {
    path("hello") {
      get {
        complete {
          <h1>Say hello to spray</h1>
        }
      }
    } ~
      path("dump") {
        parameters('toFile) { file =>
          post {
            entity(as[Array[Byte]]) { data =>
              val pw = new java.io.PrintWriter(new File(file))
              try pw.write(new String(data)) finally pw.close()
              complete {
                ""
              }
            }
          }
        }
      }
  }
}