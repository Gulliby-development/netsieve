package netsieve

/** The merge command: writes the fewest networks that cover the same addresses. */
object MergeCommand:
  def run(options: Options, items: List[Net]): Reply =
    Reply(0, Merge.blocks(items).map((f, l) => Report.render(options.style, f, l) + "\n").mkString, "")
