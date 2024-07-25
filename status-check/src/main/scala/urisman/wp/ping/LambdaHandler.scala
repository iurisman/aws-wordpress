package urisman.wp.ping

import com.amazonaws.services.lambda.runtime.{Context, RequestHandler}
import com.amazonaws.services.lambda.runtime.events.ScheduledEvent

import java.net.{HttpURLConnection, URL}
import java.time.{Duration, Instant}
import scala.concurrent.{Future, Await}
import scala.io.Source
import scala.concurrent.duration.{MILLISECONDS, Duration as ScalaDuration}
import scala.util.{Failure, Success, Try}

class LambdaHandler extends RequestHandler[ScheduledEvent, String] with Config {

	def handleRequest(event: ScheduledEvent, context: Context): String = {
		val (failedCount, emailBody) =
			sites.foldLeft((0, "")) {  // (failure count, body)
			(acc, url) =>
				val (either, latency) = checkSite(url)
				either match {
					case Left(e) =>
						val failCount = acc._1 + 1
						val body = acc._2 + s"\n$url threw exception:\n  $e\n${e.getStackTrace.mkString("\n")}"
						(failCount, body)
					case Right(rc) =>
						val failed = latency > networkTimeoutMillis || rc != 200
						val failCount = acc._1 + (if (failed) 1 else 0)
						val body = acc._2 + s"\n$url returned $rc in $latency ms"
						(failCount, body)
				}

		}
		val emailSubject =
			"Wordpress sites check: " +
				(if (failedCount == 0) "OK" else s"$failedCount Sites Failed")

		context.getLogger.log(emailSubject)
		Email.send(emailSubject, emailBody)
		emailSubject
	}

	/** @return (return-code, latency-millis)
	 */
	private def checkSite(url: String): (Either[Throwable, Int], Long) = {
		import concurrent.ExecutionContext.Implicits.global
		val start = Instant.now()
		val conn = new URL(url).openConnection().asInstanceOf[HttpURLConnection]
		conn.setRequestMethod ("GET")
		val rc = Try {
			Await.result(Future(conn.getResponseCode), ScalaDuration(networkTimeoutMillis, MILLISECONDS))
			} match {
			case Failure(e) => Left(e)
			case Success(rc) => Right(rc)
		}
		(rc, Duration.between(start, Instant.now()).toMillis)
	}
}
