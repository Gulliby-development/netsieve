package netsieve

/** The gaps command: the addresses of --within that the list does not cover, as the fewest networks. */
object GapsCommand:
  def run(options: Options, items: List[Net]): Reply =
    Nets.network(options.within) match
      case None => throw Failure(2, "bad value for --within")
      case Some((lo, len)) =>
        val hi = Nets.last(lo, len)
        val clipped = Merge.intervals(items).filter((a, b) => b >= lo && a <= hi)
        val (cur, found) = clipped.foldLeft((lo, List.empty[(Long, Long)])) { case ((at, acc), (a, b)) =>
          val next = if a > at then (at, a - 1L) :: acc else acc
          (math.max(at, b + 1L), next)
        }
        val gaps = (if cur <= hi then (cur, hi) :: found else found).reverse
        val text = gaps.flatMap((a, b) => Merge.decompose(a, b)).map((f, l) => Report.render(options.style, f, l) + "\n").mkString
        if text.isEmpty then Reply(0, "none\n", "") else Reply(0, text, "")
