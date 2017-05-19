import akka.actor.Actor

case class ReadBook(title: String)
case class BookLine(line: String)

class ReadBookActor extends Actor {
  override def receive: Receive = {
    case ReadBook => {

    }
  }
}
