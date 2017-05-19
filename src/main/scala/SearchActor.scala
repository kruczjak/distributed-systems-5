import akka.actor.Actor

case class BookSearch(title: String)

class SearchActor extends Actor {
  override def receive: Receive = ???
}
