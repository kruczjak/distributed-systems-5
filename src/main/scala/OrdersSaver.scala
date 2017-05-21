import java.io.FileWriter

class OrdersSaver {
  def addOrder(title: String): Unit = {
    this.synchronized {
      new FileWriter("orders.txt", true) {
        write(s"$title\n")
        close()
      }
    }
  }
}
