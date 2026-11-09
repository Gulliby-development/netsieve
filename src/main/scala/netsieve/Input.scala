package netsieve

/** Reads the list of networks from the input text. */
object Input:
  private def cut(text: String): List[String] =
    if text.isEmpty then Nil
    else
      val parts = text.split("\n", -1).toList
      if parts.last.isEmpty then parts.init else parts

  private def item(line: String, n: Int): Option[Net] =
    val clean = if line.endsWith("\r") then line.dropRight(1) else line
    val words = clean.split("[ \t]").toList.filter(_.nonEmpty)
    if words.isEmpty || words.head.startsWith("#") then None
    else if words.length > 1 then throw Failure(1, s"line $n: expected 1 network")
    else
      val word = words.head
      Nets.network(word) match
        case None => throw Failure(1, s"line $n: bad network $word")
        case Some((first, len)) =>
          if !Nets.aligned(first, len) then throw Failure(1, s"line $n: host bits set $word")
          Some(Net(n, word, first, len))

  def readItems(text: String): List[Net] =
    val lines = cut(text)
    if lines.length > Registry.MaxLines then throw Failure(1, "input is too long (max 40 lines)")
    val items = lines.zipWithIndex.flatMap((line, index) => item(line, index + 1))
    if items.isEmpty then throw Failure(1, "no networks")
    items
