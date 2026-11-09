package netsieve

/** The count command: writes the number of covered addresses, or of hosts of the merged networks. */
object CountCommand:
  def run(options: Options, items: List[Net]): Reply =
    val total =
      if options.mode == "hosts" then Merge.blocks(items).map((_, len) => if len <= 30 then Nets.size(len) - 2L else Nets.size(len)).sum
      else Merge.intervals(items).map((lo, hi) => hi - lo + 1L).sum
    Reply(0, total.toString + "\n", "")
