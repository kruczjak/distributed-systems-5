import akka.actor.Actor

class ClientActor extends Actor {
  override def receive: Receive = {
    case OrderSuccess => println("Order completed")
  }
}
