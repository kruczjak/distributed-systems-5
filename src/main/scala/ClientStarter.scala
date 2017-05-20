import akka.actor.{ActorSystem, Props}
import com.typesafe.config.{Config, ConfigFactory}

object ClientStarter extends App {
  val config: Config = ConfigFactory.load()
  val system = ActorSystem("client", config.getConfig("client").withFallback(config))

  val clientActor = system.actorOf(Props(classOf[ClientActor]))
  val orderActor = system.actorSelection("akka.tcp://server@127.0.0.1:2553/user/order")
  val searchActor = system.actorSelection("akka.tcp://server@127.0.0.1:2553/user/search")
  val readBookActor = system.actorSelection("akka.tcp://server@127.0.0.1:2553/user/read")

  while(true) {
    println("Wybierz opcje:")
    println("1. Wyszukaj książkę")
    println("2. Zamów ksiązkę")
    println("3. Czytaj książkę")
    println("q - quit")

    val line = readLine()

    if (line.startsWith("1")) {
      println("Podaj fragment tytułu książki:")
      val fragment = readLine()
      searchActor.tell(BookSearch(fragment), clientActor)
    } else if (line.startsWith("2")) {
      println("Podaj tytuł książki:")
      val title = readLine()
      orderActor.tell(OrderBook(title), clientActor)
    } else if (line.startsWith("3")) {
      println("Podaj tytuł książki:")
      val title = readLine()
      readBookActor.tell(ReadBook(title), clientActor)
    } else if (line.startsWith("q")) {
      System.exit(0)
    }
  }
}
