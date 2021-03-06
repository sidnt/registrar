package registrar

object userQuery {

  val query: String =
    """
      |{
      | readUser(userId: "1") {
      |   name
      |   email
      | }
      |}
      |""".stripMargin
}
