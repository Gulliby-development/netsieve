package netsieve

class ValuesSuite extends RowSuite:
  val rows = Seq(
    (List("sort", "--style", "Cidr"), "", 2, "", "netsieve: bad value for --style\n"),
    (List("sort", "--style", ""), "", 2, "", "netsieve: bad value for --style\n"),
    (List("count", "--mode", "all"), "", 2, "", "netsieve: bad value for --mode\n"),
    (List("split", "--prefix", "33"), "", 2, "", "netsieve: bad value for --prefix\n"),
    (List("split", "--prefix", "05"), "", 2, "", "netsieve: bad value for --prefix\n"),
    (List("split", "--prefix", "-1"), "", 2, "", "netsieve: bad value for --prefix\n"),
    (List("split", "--prefix", "2.5"), "", 2, "", "netsieve: bad value for --prefix\n"),
    (List("split", "--prefix", ""), "", 2, "", "netsieve: bad value for --prefix\n"),
    (List("gaps", "--within", "10.0.0.1/24"), "", 2, "", "netsieve: bad value for --within\n"),
    (List("gaps", "--within", "300.0.0.0/8"), "", 2, "", "netsieve: bad value for --within\n"),
    (List("gaps", "--within", "10.0.0.0/"), "", 2, "", "netsieve: bad value for --within\n"),
    (List("gaps", "--within", "10.0.0.0/33"), "", 2, "", "netsieve: bad value for --within\n"),
    (List("contains", "--ip", "1.2.3"), "", 2, "", "netsieve: bad value for --ip\n"),
    (List("contains", "--ip", "1.2.3.4/5"), "", 2, "", "netsieve: bad value for --ip\n"),
    (List("contains", "--ip", "01.2.3.4"), "", 2, "", "netsieve: bad value for --ip\n"),
    (List("count", "--mode", "addresses"), "10.0.0.0/30\n", 0, "4\n", ""),
    (List("count", "--mode", "hosts"), "10.0.0.0/31\n10.0.0.2/32\n", 0, "3\n", ""),
    (List("split", "--prefix", "31", "--style", "range"), "10.0.0.0/30\n", 0, "10.0.0.0-10.0.0.1\n10.0.0.2-10.0.0.3\n", "")
  )

  test("rows")(checkRows(rows))
