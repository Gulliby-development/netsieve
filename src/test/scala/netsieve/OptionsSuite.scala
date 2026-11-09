package netsieve

class OptionsSuite extends RowSuite:
  val rows = Seq(
    (List("sort", "extra"), "", 2, "", "netsieve: unexpected argument extra\n"),
    (List("sort", "-"), "", 2, "", "netsieve: unexpected argument -\n"),
    (List("sort", ""), "", 2, "", "netsieve: unexpected argument \n"),
    (List("sort", "--bogus"), "", 2, "", "netsieve: unknown option --bogus\n"),
    (List("check", "--style", "cidr"), "", 2, "", "netsieve: option --style is not valid for check\n"),
    (List("sort", "--mode", "hosts"), "", 2, "", "netsieve: option --mode is not valid for sort\n"),
    (List("sort", "--style", "cidr", "--style", "cidr"), "", 2, "", "netsieve: given twice: --style\n"),
    (List("sort", "--style"), "", 2, "", "netsieve: missing value for --style\n"),
    (List("sort", "-x", "--style"), "", 2, "", "netsieve: unknown option -x\n"),
    (List("check", "--style"), "", 2, "", "netsieve: option --style is not valid for check\n"),
    (List("sort", "--style", "x", "--bogus"), "", 2, "", "netsieve: bad value for --style\n"),
    (List("count", "--style", "hex"), "", 2, "", "netsieve: option --style is not valid for count\n"),
    (List("merge", "--prefix", "8"), "", 2, "", "netsieve: option --prefix is not valid for merge\n"),
    (List("gaps"), "", 2, "", "netsieve: missing --within\n"),
    (List("split"), "", 2, "", "netsieve: missing --prefix\n"),
    (List("contains"), "", 2, "", "netsieve: missing --ip\n"),
    (List("gaps", "--style", "mask"), "", 2, "", "netsieve: missing --within\n"),
    (List("split", "--style", "hex", "--style", "mask"), "", 2, "", "netsieve: given twice: --style\n"),
    (List("merge", "--style", "range"), "10.0.0.0/25\n10.0.0.128/25\n", 0, "10.0.0.0-10.0.0.255\n", ""),
    (List("count", "--mode", "hosts"), "10.0.0.0/24\n", 0, "254\n", ""),
    (List("sort", "--style", "hex"), "10.0.0.0/8\n", 0, "0A000000/8\n", ""),
    (List("split", "--style", "range", "--prefix", "25"), "10.0.0.0/24\n", 0, "10.0.0.0-10.0.0.127\n10.0.0.128-10.0.0.255\n", ""),
    (List("gaps", "--style", "cidr", "--within", "10.0.0.0/23"), "10.0.0.0/24\n", 0, "10.0.1.0/24\n", ""),
    (List("contains", "--ip", "9.9.9.9"), "9.0.0.0/8\n", 0, "9.9.9.9 in 9.0.0.0/8\n", "")
  )

  test("rows")(checkRows(rows))
