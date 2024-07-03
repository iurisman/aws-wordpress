package urisman.wp.ping

trait Config {
  val fromEmail = "igor.urisman@gmail.com"
  val toEmail = fromEmail
  val sites = List("https://urisman.net/", "https://getvariant.com")
  val networkTimeoutMillis = 2000
}
