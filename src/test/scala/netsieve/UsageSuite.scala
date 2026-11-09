package netsieve

class UsageSuite extends RowSuite:
  val rows = Seq(
    (List("help"), "", 0, "usage: netsieve check\n       netsieve sort [--style STYLE]\n       netsieve merge [--style STYLE]\n       netsieve count [--mode MODE]\n       netsieve gaps --within NET [--style STYLE]\n       netsieve contains --ip IP\n       netsieve split --prefix N [--style STYLE]\n", ""),
    (List("-h"), "", 0, "usage: netsieve check\n       netsieve sort [--style STYLE]\n       netsieve merge [--style STYLE]\n       netsieve count [--mode MODE]\n       netsieve gaps --within NET [--style STYLE]\n       netsieve contains --ip IP\n       netsieve split --prefix N [--style STYLE]\n", ""),
    (List("--help", "--bogus"), "", 0, "usage: netsieve check\n       netsieve sort [--style STYLE]\n       netsieve merge [--style STYLE]\n       netsieve count [--mode MODE]\n       netsieve gaps --within NET [--style STYLE]\n       netsieve contains --ip IP\n       netsieve split --prefix N [--style STYLE]\n", ""),
    (List(), "", 2, "", "usage: netsieve check\n       netsieve sort [--style STYLE]\n       netsieve merge [--style STYLE]\n       netsieve count [--mode MODE]\n       netsieve gaps --within NET [--style STYLE]\n       netsieve contains --ip IP\n       netsieve split --prefix N [--style STYLE]\n"),
    (List("--version"), "", 0, "netsieve 0.1.0\n", ""),
    (List("--version", "x"), "", 0, "netsieve 0.1.0\n", ""),
    (List("frob"), "", 2, "", "netsieve: unknown command frob\n"),
    (List(""), "", 2, "", "netsieve: unknown command \n"),
    (List("Check"), "", 2, "", "netsieve: unknown command Check\n"),
    (List("-H"), "", 2, "", "netsieve: unknown command -H\n")
  )

  test("rows")(checkRows(rows))
