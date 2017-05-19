import akka.actor.{ActorSystem, Props}
import akka.stream.ActorMaterializer
import com.typesafe.config.{Config, ConfigFactory}

object ServerStarter extends App {
  val ordersSaver = new OrdersSaver()

  val config: Config = ConfigFactory.load()
  val system = ActorSystem("server", config.getConfig("server").withFallback(config))
  val materializer = ActorMaterializer.create(system)

  system.actorOf(Props(classOf[OrderActor], ordersSaver), "order")
  system.actorOf(Props(classOf[SearchActor]), "search")
  system.actorOf(Props(classOf[ReadBookActor]), "read")

  println("Server started")
}
