package netsieve

import scala.annotation.unused

/** The check command: reports how many networks the list holds. */
object CheckCommand:
  def run(@unused options: Options, items: List[Net]): Reply =
    Reply(0, "ok, " + Report.plural(items.size, "network") + "\n", "")
