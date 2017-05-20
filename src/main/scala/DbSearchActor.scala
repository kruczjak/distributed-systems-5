import akka.actor.Actor
import scala.io.Source._

case class DbSearchResponse(lines: Array[String])

class DbSearchActor(filename: String) extends Actor  {
  override def receive: Receive = {
    case BookSearch(title) => {
      val lines = fromFile(filename).getLines
      sender ! DbSearchResponse(lines.filter((s: String) => s.contains(title)).toArray)
    }
  }
}
