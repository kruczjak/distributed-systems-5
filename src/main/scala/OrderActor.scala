import akka.actor.SupervisorStrategy.{Escalate, Stop}
import akka.actor.{Actor, OneForOneStrategy, SupervisorStrategy}
import scala.concurrent.duration._

case class OrderBook(title: String)
case class OrderSuccess()

class OrderActor(ordersSaver: OrdersSaver) extends Actor {

  override def supervisorStrategy: SupervisorStrategy = OneForOneStrategy(maxNrOfRetries = 10, withinTimeRange = 1 minute) {
    case _: Exception => Escalate
  }

  override def preStart(): Unit = {
    println(self.path)
  }

  def makeOrder(title: String): Unit = {
    ordersSaver.addOrder(title)
  }

  def receive = {
    case OrderBook(title) => {
      makeOrder(title)
      sender ! OrderSuccess
    }
  }
}
