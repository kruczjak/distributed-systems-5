import akka.actor.SupervisorStrategy.Restart
import akka.actor.{Actor, ActorRef, OneForOneStrategy, Props, SupervisorStrategy}
import akka.pattern.ask
import akka.util.Timeout

import scala.concurrent.duration._
import scala.util.{Failure, Success}

case class BookSearch(title: String)
case class BookSearchSuccess(titles: Array[String], prices: Array[String])
case class BookSearchFailure(filename: String)

class SearchActor extends Actor {
  import context.dispatcher
  implicit val timeout = Timeout(15 seconds)
  var positionFound = false

  override def supervisorStrategy: SupervisorStrategy = OneForOneStrategy(maxNrOfRetries = 10, withinTimeRange = 1 minute) {
    case _: Exception => Restart
  }

  override def receive: Receive  = {
    case BookSearch(title) => {
      positionFound = false
      val db1 = context.actorOf(Props(classOf[DbSearchActor], "books1.txt"))
      val db2 = context.actorOf(Props(classOf[DbSearchActor], "books2.txt"))
      val db1Future = db1 ? BookSearch(title)
      val db2Future = db2 ? BookSearch(title)

      val originalSender = sender

      db1Future.onComplete {
        case Success(result) => onCompleteSuccess(originalSender, result)
        case Failure(result) => originalSender ! BookSearchFailure("Search failed in books1.txt")
      }

      db2Future.onComplete {
        case Success(result) => onCompleteSuccess(originalSender, result)
        case Failure(result) => originalSender ! BookSearchFailure("Search failed in books2.txt")
      }
    }
  }

  private def onCompleteSuccess(originalSender: ActorRef, result: Any) = {
    val dbSearchResponse: DbSearchResponse = result.asInstanceOf[DbSearchResponse]
    println(s"Input array: ${dbSearchResponse.lines.mkString(" and ")}")

    positionFound.synchronized {
      if (!positionFound) {
        if (dbSearchResponse.lines.length > 0) {
          positionFound = true
        }
        var titles = Array[String]()
        var prices = Array[String]()
        for (line <- dbSearchResponse.lines) {
          println(s"Line $line")
          val splitted = line.split(";")
          titles = titles :+ splitted(0)
          prices = prices :+ splitted(1)
        }

        originalSender ! BookSearchSuccess(titles, prices)
      }
    }
  }
}
