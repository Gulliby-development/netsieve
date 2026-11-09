package netsieve

/** An error that carries the exit status of the program: 2 for the command line, 1 for the list and for a command. `Cli.run` turns it into the reply `Reply(status, "", "netsieve: " + message + "\n")`.
  */
final class Failure(val status: Int, message: String) extends RuntimeException(message)
