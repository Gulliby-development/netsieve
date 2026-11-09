package netsieve

/** The text helpers: dotted addresses, networks in a style and counts with their noun. */
object Report:
  def dotted(value: Long): String =
    List(24, 16, 8, 0).map(shift => ((value >> shift) & 255L).toString).mkString(".")

  def render(style: String, first: Long, len: Int): String =
    style match
      case "cidr" => dotted(first) + "/" + len
      case "range" => dotted(first) + "-" + dotted(Nets.last(first, len))
      case other => Style.render(first, len, other)

  def plural(n: Long, noun: String): String =
    if n == 1L then s"$n $noun" else s"$n ${noun}s"
