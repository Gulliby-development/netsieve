package netsieve

/** The split command: every network of the list cut into its networks of the prefix length N. */
object SplitCommand:
  def run(options: Options, items: List[Net]): Reply =
    val n = options.prefix.getOrElse(throw Failure(2, "missing --prefix"))
    items.find(_.len > n).foreach(item => throw Failure(1, s"line ${item.line}: cannot split ${item.word} into /$n"))
    val total = items.map(item => 1L << (n - item.len)).sum
    if total > Registry.MaxSplit then throw Failure(1, "too many subnets (max 256)")
    val lines =
      for
        item <- items
        k <- 0L until (1L << (n - item.len))
      yield Report.render(options.style, item.first + k * Nets.size(n), n) + "\n"
    Reply(0, lines.mkString, "")
