package netsieve

class ReadsSuite extends RowSuite:
  val rows = Seq(
    (List("check"), true),
    (List("sort"), true),
    (List("sort", "--style", "range"), true),
    (List("merge"), true),
    (List("count"), true),
    (List("count", "--mode", "hosts"), true),
    (List("gaps", "--within", "10.0.0.0/8"), true),
    (List("gaps", "--within", "10.0.0.0/8", "--style", "mask"), true),
    (List("contains", "--ip", "1.2.3.4"), true),
    (List("split", "--prefix", "24"), true),
    (List("split", "--style", "hex", "--prefix", "8"), true),
    (List(), false),
    (List("help"), false),
    (List("-h"), false),
    (List("--help"), false),
    (List("--version"), false),
    (List("frob"), false),
    (List("check", "--style", "cidr"), false),
    (List("sort", "--style", "x"), false),
    (List("gaps"), false),
    (List("gaps", "--within", "10.0.0.1/8"), false),
    (List("split", "--prefix", "33"), false),
    (List("contains", "--ip", "1.2.3"), false),
    (List("Check"), false)
  )

  test("rows")(checkReads(rows))
