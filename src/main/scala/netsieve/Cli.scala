package netsieve

import scala.util.Try

/** The command line: help, version, the option rules, the reading of the list and the call of the command. */
object Cli:
  private val OptionNames = List("--style", "--mode", "--within", "--ip", "--prefix")

  private def bad(option: String): Nothing = throw Failure(2, "bad value for " + option)

  private def store(option: String, value: String, opts: Options): Options = option match
    case "--style" => if Registry.Styles.contains(value) then opts.copy(style = value) else bad(option)
    case "--mode" => if Registry.Modes.contains(value) then opts.copy(mode = value) else bad(option)
    case "--within" =>
      Nets.network(value) match
        case Some((first, len)) if Nets.aligned(first, len) => opts.copy(within = value)
        case _ => bad(option)
    case "--ip" => if Nets.address(value).isDefined then opts.copy(ip = value) else bad(option)
    case _ =>
      Nets.prefix(value) match
        case Some(p) => opts.copy(prefix = Some(p))
        case None => bad(option)

  private def options(command: Registry.Command, words: List[String]): Options =
    def loop(rest: List[String], opts: Options, seen: List[String]): Options = rest match
      case Nil =>
        if command.required.nonEmpty && !seen.contains(command.required) then throw Failure(2, "missing " + command.required)
        opts
      case word :: tail =>
        if !word.startsWith("-") || word == "-" then throw Failure(2, "unexpected argument " + word)
        if !OptionNames.contains(word) then throw Failure(2, "unknown option " + word)
        if !command.allowed.contains(word) then throw Failure(2, "option " + word + " is not valid for " + command.name)
        if seen.contains(word) then throw Failure(2, "given twice: " + word)
        tail match
          case Nil => throw Failure(2, "missing value for " + word)
          case value :: more => loop(more, store(word, value, opts), word :: seen)
    loop(words, Options("cidr", "addresses", "", "", None), Nil)

  def run(args: List[String], text: String): Reply =
    args match
      case Nil => Reply(2, "", Registry.Usage)
      case ("help" | "-h" | "--help") :: _ => Reply(0, Registry.Usage, "")
      case "--version" :: _ => Reply(0, Registry.Version + "\n", "")
      case name :: rest =>
        try
          val command = Registry.find(name).getOrElse(throw Failure(2, "unknown command " + name))
          val opts = options(command, rest)
          command.run(opts, Input.readItems(text))
        catch case f: Failure => Reply(f.status, "", "netsieve: " + f.getMessage + "\n")

  def readsStdin(args: List[String]): Boolean = args match
    case name :: rest => Registry.find(name).exists(c => Try(options(c, rest)).isSuccess)
    case Nil => false
