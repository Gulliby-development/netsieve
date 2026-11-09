package netsieve

/** The entry point. Only this object reads or writes a stream: it reads standard input only when `Cli.readsStdin` says the command line is a valid use of a command, and every other reply needs no input.
  */
object Main:
  def main(args: Array[String]): Unit =
    val words = args.toList
    val text = if Cli.readsStdin(words) then String(System.in.readAllBytes(), "UTF-8") else ""
    val reply = Cli.run(words, text)
    System.out.write(reply.out.getBytes("UTF-8"))
    System.out.flush()
    System.err.write(reply.err.getBytes("UTF-8"))
    System.err.flush()
    System.exit(reply.status)
