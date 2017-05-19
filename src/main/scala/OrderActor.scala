import akka.actor.Actor

case class OrderBook(title: String)
case class OrderSuccess()

class OrderActor(ordersSaver: OrdersSaver) extends Actor {


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
