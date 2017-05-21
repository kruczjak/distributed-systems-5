import akka.NotUsed
import akka.actor.SupervisorStrategy.{Restart, Stop}
import akka.actor.{Actor, OneForOneStrategy, SupervisorStrategy}
import akka.stream.{ActorMaterializer, OverflowStrategy, ThrottleMode}
import akka.stream.scaladsl.{Sink, Source}

import scala.concurrent.duration._
import scala.io.Source.fromFile

case class ReadBook(filename: String)
case class BookLine(line: String)

class ReadBookActor extends Actor {
  override def supervisorStrategy: SupervisorStrategy = OneForOneStrategy(maxNrOfRetries = 10, withinTimeRange = 1 minute) {
    case _: Exception => Restart
  }

  override def receive: Receive = {
    case ReadBook(filename) =>
      val materializer = ActorMaterializer.create(context)
      val sink = Source.actorRef(1000, OverflowStrategy.dropNew)
        .throttle(1, 1 second, 1, ThrottleMode.shaping)
        .to(Sink.actorRef(sender, NotUsed))
        .run()(materializer)
      val lines = fromFile(filename).getLines
      lines.foreach((line: String) => sink ! BookLine(line))
  }
}
