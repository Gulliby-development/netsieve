package netsieve

/** The sort command: writes the networks ordered by first address, then by prefix length. */
object SortCommand:
  def run(options: Options, items: List[Net]): Reply =
    Reply(0, items.sortBy(n => (n.first, n.len)).map(n => Report.render(options.style, n.first, n.len) + "\n").mkString, "")
