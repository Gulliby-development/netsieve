package netsieve

/** The contains command: one line `IP in NET` for each network of the list that holds the address. */
object ContainsCommand:
  def run(options: Options, items: List[Net]): Reply =
    Nets.address(options.ip) match
      case None => throw Failure(2, "bad value for --ip")
      case Some(ip) =>
        val holding = items.filter(n => n.first <= ip && ip <= Nets.last(n.first, n.len))
        if holding.isEmpty then Reply(1, options.ip + " is not covered\n", "")
        else Reply(0, holding.map(n => options.ip + " in " + Report.render("cidr", n.first, n.len) + "\n").mkString, "")
