import akka.actor.{ActorSystem, Props}
import com.typesafe.config.{Config, ConfigFactory}

object ServerStarter extends App {
  val ordersSaver = new OrdersSaver()

  val config: Config = ConfigFactory.load()
  val system = ActorSystem("server", config.getConfig("server").withFallback(config))
  system.actorOf(Props(classOf[OrderActor], ordersSaver), "order")

  println("Server started")
}
