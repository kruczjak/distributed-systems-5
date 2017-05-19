import akka.actor.{ActorSystem, Props}
import com.typesafe.config.{Config, ConfigFactory}

object ClientStarter extends App {
  val config: Config = ConfigFactory.load()
  val system = ActorSystem("client", config.getConfig("client").withFallback(config))

  val clientActor = system.actorOf(Props(classOf[ClientActor]))
  val orderActor = system.actorSelection("akka.tcp://server@127.0.0.1:2553/user/order")
  val searchActor = null
  val readBookActor = null

  while(true) {
    println("Wybierz opcje:")
    println("1. Wyszukaj książkę")
    println("2. Zamów ksiązkę")
    println("3. Czytaj książkę")

    val line = readLine()

    if (line.startsWith("1")) {

    } else if (line.startsWith("2")) {
      println("Podaj tytuł książki:")
      val title = readLine()
      orderActor.tell(OrderBook(title), clientActor)
    } else if (line.startsWith("3")) {

    }
  }
}
