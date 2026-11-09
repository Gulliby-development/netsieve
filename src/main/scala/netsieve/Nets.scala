package netsieve

/** The reading of addresses, prefix lengths and network words, and the size, last address and alignment of a network. */
object Nets:
  private def number(text: String, maxDigits: Int, max: Long): Option[Long] =
    val digits = text.nonEmpty && text.length <= maxDigits && text.forall(c => c >= '0' && c <= '9')
    val plain = text.length == 1 || text.headOption.contains('0') == false
    if digits && plain && text.toLong <= max then Some(text.toLong) else None

  def address(word: String): Option[Long] =
    val parts = word.split("\\.", -1).toList
    val numbers = parts.flatMap(number(_, 3, 255L))
    if parts.length == 4 && numbers.length == 4 then Some(numbers.foldLeft(0L)((acc, n) => acc * 256L + n)) else None

  def prefix(text: String): Option[Int] = number(text, 2, 32L).map(_.toInt)

  def network(word: String): Option[(Long, Int)] =
    val slash = word.indexOf('/')
    if slash < 0 then address(word).map(a => (a, 32))
    else
      for
        a <- address(word.substring(0, slash))
        len <- prefix(word.substring(slash + 1))
      yield (a, len)

  def size(len: Int): Long = 1L << (32 - len)

  def last(first: Long, len: Int): Long = first + size(len) - 1L

  def aligned(first: Long, len: Int): Boolean = (first & (size(len) - 1L)) == 0L
