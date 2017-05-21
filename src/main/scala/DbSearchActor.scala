import akka.actor.SupervisorStrategy.Stop
import akka.actor.{Actor, OneForOneStrategy, SupervisorStrategy}
import scala.concurrent.duration._

import scala.io.Source._

case class DbSearchResponse(lines: Array[String])

class DbSearchActor(filename: String) extends Actor  {
  override def supervisorStrategy: SupervisorStrategy = OneForOneStrategy(maxNrOfRetries = 10, withinTimeRange = 1 minute) {
    case _: Exception => Stop
  }

  override def receive: Receive = {
    case BookSearch(title) => {
      val lines = fromFile(filename).getLines
      sender ! DbSearchResponse(lines.filter((s: String) => s.contains(title)).toArray)
    }
  }
}
