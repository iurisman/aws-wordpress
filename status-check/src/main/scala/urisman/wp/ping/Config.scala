package urisman.wp.ping

trait Config {
  val fromEmail = "igor.urisman@gmail.com"
  val toEmail = fromEmail
  val sites = List("https://getvariant.com", "https://urisman.net/")
  val networkTimeoutMillis = 2000
}
