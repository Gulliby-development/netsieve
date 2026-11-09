package netsieve

/** The styles mask (`A.B.C.D NETMASK`) and hex (`HHHHHHHH/LEN`) of a block. */
object Style:
  def render(first: Long, len: Int, style: String): String =
    if style == "mask" then Report.dotted(first) + " " + Masks.of(len)
    else
      val digits = java.lang.Long.toHexString(first).toUpperCase(java.util.Locale.ROOT)
      "0" * (8 - digits.length) + digits + "/" + len
