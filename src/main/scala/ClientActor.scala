import akka.actor.Actor

class ClientActor extends Actor {
  override def receive: Receive = {
    case OrderSuccess => println("Order completed")
    case BookSearchSuccess(titles, prices) => {
      println("Wyniki zostały zwrócone:")
      for (i <- titles.indices) {
        println(s"${titles(i)} - ${prices(i)}")
      }
    }
    case BookSearchFailure(message) => println(message)
    case BookLine(line) => {
      println(line)
    }
  }
}
