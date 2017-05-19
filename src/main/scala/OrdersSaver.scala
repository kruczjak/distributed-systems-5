import java.io.PrintWriter

class OrdersSaver {
  def addOrder(title: String): Unit = {
    this.synchronized {
      new PrintWriter("orders.txt") {
        write(s"$title\n")
        close()
      }
    }
  }
}
