package netsieve

/** A network: the line number (counted from 1, skipped lines included; 0 for a word that is not on a line), the word as written, the first address as a number and the prefix length 0 to 32.
  */
final case class Net(line: Int, word: String, first: Long, len: Int)

/** The options: the value of `--style` ("cidr" when absent), of `--mode` ("addresses" when absent), of `--within` and `--ip` ("" when absent) and of `--prefix` (None when absent).
  */
final case class Options(style: String, mode: String, within: String, ip: String, prefix: Option[Int])

/** The reply of a run: the exit status, the whole standard output text and the standard error text. */
final case class Reply(status: Int, out: String, err: String)

/** The version, the limits, the usage text, the styles, the modes and the command table. A command is a function `(Options, List[Net]) => Reply`; it throws `Failure` when it fails.
  */
object Registry:
  val Version = "netsieve 0.1.0"
  val MaxLines = 40
  val MaxSplit = 256

  val Styles = List("cidr", "range", "mask", "hex")
  val Modes = List("addresses", "hosts")

  val Usage: String =
    "usage: netsieve check\n" +
      "       netsieve sort [--style STYLE]\n" +
      "       netsieve merge [--style STYLE]\n" +
      "       netsieve count [--mode MODE]\n" +
      "       netsieve gaps --within NET [--style STYLE]\n" +
      "       netsieve contains --ip IP\n" +
      "       netsieve split --prefix N [--style STYLE]\n"

  /** One entry per command, in the order of the usage text: its word, the options it accepts, the option that must be given (or "") and its function.
    */
  final case class Command(
      name: String,
      allowed: List[String],
      required: String,
      run: (Options, List[Net]) => Reply
  )

  val Commands: List[Command] = List(
    Command("check", Nil, "", CheckCommand.run),
    Command("sort", List("--style"), "", SortCommand.run),
    Command("merge", List("--style"), "", MergeCommand.run),
    Command("count", List("--mode"), "", CountCommand.run),
    Command("gaps", List("--within", "--style"), "--within", GapsCommand.run),
    Command("contains", List("--ip"), "--ip", ContainsCommand.run),
    Command("split", List("--prefix", "--style"), "--prefix", SplitCommand.run)
  )

  def find(name: String): Option[Command] = Commands.find(_.name == name)
