import akka.actor.SupervisorStrategy.Resume
import akka.actor.{Actor, OneForOneStrategy, SupervisorStrategy}
import scala.concurrent.duration._

class ClientActor extends Actor {
  override def supervisorStrategy: SupervisorStrategy = OneForOneStrategy(maxNrOfRetries = 10, withinTimeRange = 1 minute) {
    case _: Exception => Resume
  }

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
