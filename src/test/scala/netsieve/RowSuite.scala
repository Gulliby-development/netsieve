package netsieve

/** One row of a row file: the arguments, the standard input, the exit status, the standard output and the standard error. */
type Row = (List[String], String, Int, String, String)

/** The test helper of the row files. A row file is a class that extends `RowSuite` and holds one or two lists of literal rows, `val rows = Seq(...)` and, in the files of part 2, a second one, `val totals = Seq(...)`, each checked by a test that names every row that failed:
  *
  *   - `checkRows(rows)`: one command line through `Cli.run` per row;
  *   - `checkReads(rows)`: a row `(ARGS, EXPECTED)` says whether `Cli.readsStdin` is true for ARGS.
  */
abstract class RowSuite extends munit.FunSuite:
  def checkRows(rows: Seq[Row]): Unit =
    val failures = rows.zipWithIndex.flatMap { case ((args, stdin, status, out, err), index) =>
      val got = Cli.run(args, stdin)
      if got == Reply(status, out, err) then None
      else Some(s"row ${index + 1} $args: got $got, want ${Reply(status, out, err)}")
    }
    if failures.nonEmpty then fail(failures.mkString("\n"))

  def checkReads(rows: Seq[(List[String], Boolean)]): Unit =
    val failures = rows.zipWithIndex.flatMap { case ((args, expected), index) =>
      val got = Cli.readsStdin(args)
      if got == expected then None else Some(s"row ${index + 1} $args: readsStdin is $got, want $expected")
    }
    if failures.nonEmpty then fail(failures.mkString("\n"))
