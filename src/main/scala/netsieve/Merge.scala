package netsieve

/** The union of networks as sorted disjoint ranges and as the fewest aligned blocks. */
object Merge:
  def decompose(low: Long, high: Long): List[(Long, Int)] = loop(low, high, Nil).reverse

  def intervals(items: List[Net]): List[(Long, Long)] =
    val ranges = items.sortBy(_.first).map(n => (n.first, Nets.last(n.first, n.len)))
    val joined = ranges.foldLeft(List.empty[(Long, Long)]) { (acc, next) =>
      acc match
        case (lo, hi) :: rest if next._1 <= hi + 1L => (lo, math.max(hi, next._2)) :: rest
        case _ => next :: acc
    }
    joined.reverse

  def blocks(items: List[Net]): List[(Long, Int)] = intervals(items).flatMap((lo, hi) => decompose(lo, hi))

  @scala.annotation.tailrec
  private def loop(cur: Long, high: Long, acc: List[(Long, Int)]): List[(Long, Int)] =
    if cur > high then acc
    else
      val len = (0 to 32).find(l => Nets.aligned(cur, l) && Nets.last(cur, l) <= high).get
      loop(cur + Nets.size(len), high, (cur, len) :: acc)
