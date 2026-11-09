package netsieve

class InputSuite extends RowSuite:
  val rows = Seq(
    (List("check"), "", 1, "", "netsieve: no networks\n"),
    (List("check"), "\n", 1, "", "netsieve: no networks\n"),
    (List("check"), "# only\n\n   \n", 1, "", "netsieve: no networks\n"),
    (List("check"), "10.0.0.0/8", 0, "ok, 1 network\n", ""),
    (List("check"), "10.0.0.0/8\n11.0.0.0/8", 0, "ok, 2 networks\n", ""),
    (List("check"), "10.0.0.0/8\r\n11.0.0.0/8\r\n", 0, "ok, 2 networks\n", ""),
    (List("check"), "10.0.0.0/8\n10.0.0.0/8 11.0.0.0/8\n", 1, "", "netsieve: line 2: expected 1 network\n"),
    (List("check"), "\n# c\n10.0.0.0/8\n10.0.0.0 /8\n", 1, "", "netsieve: line 4: expected 1 network\n"),
    (List("check"), "  10.0.0.0/8  \n\t11.0.0.0/8\t\n", 0, "ok, 2 networks\n", ""),
    (List("check"), "10.0.0.0/8\n   # note\n11.0.0.0/8\n#12.0.0.0/8\n", 0, "ok, 2 networks\n", ""),
    (List("check"), "10.0.0.0/8\nfrob\n11.0.0.0/8\nbad\n", 1, "", "netsieve: line 2: bad network frob\n"),
    (List("check"), "10.0.0.0/8\nx\n1 2\n", 1, "", "netsieve: line 2: bad network x\n"),
    (List("check"), "10.0.0.0/8\n10.0.0.0/8 #c\n", 1, "", "netsieve: line 2: expected 1 network\n"),
    (List("check"), "10.0.0.0/8\n\n\n1.2.3\n", 1, "", "netsieve: line 4: bad network 1.2.3\n"),
    (List("sort"), "10.0.0.0/8\n\t\n", 0, "10.0.0.0/8\n", "")
  )

  test("rows")(checkRows(rows))
