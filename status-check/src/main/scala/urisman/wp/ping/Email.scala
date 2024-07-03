package urisman.wp.ping

import com.amazonaws.regions.Regions
import com.amazonaws.services.simpleemail.AmazonSimpleEmailServiceClientBuilder
import com.amazonaws.services.simpleemail.model.*

import java.time.LocalTime

/**
 * Send email via SES
 */
object Email extends Config {

	/** The okay notification goes out only at midnight */
	def sendOk(subject: String, body: String): Unit = {
		val now = LocalTime.now()
//		if (now.isAfter(LocalTime.of(23, 0)) && now.isBefore(LocalTime.of(0,0))) {
			send(subject, body)
//		}
	}

	/** Error notification goes out on each call */
	def sendError(subject: String, body: String): Unit = {
		send(subject, body);
	}

	/** The actual send */
	def send(subject: String, body: String): Unit = {
		//if (subject.contains("FAILED"))

		val client =
			AmazonSimpleEmailServiceClientBuilder.standard()
      // Replace US_WEST_2 with the AWS Region you're using for Amazon SES.
			.withRegion(Regions.US_WEST_2).build();

		val request = new SendEmailRequest()
			.withDestination(new Destination().withToAddresses(toEmail))
			.withMessage(
				new Message()
					.withBody(
						new Body()
							.withText(new Content().withCharset("UTF-8").withData(body))
					)
					.withSubject(new Content().withCharset("UTF-8").withData(subject))
			)
			.withSource(fromEmail)
		client.sendEmail(request);
	}

	/*
	/**
	 * Send HTML email
	 */
	def sendHtml(to: String, from: String, subject: String, body: String) = {

		val client =
			AmazonSimpleEmailServiceClientBuilder.standard()
      // Replace US_WEST_2 with the AWS Region you're using for
      // Amazon SES.
			.withRegion(Regions.US_WEST_2).build();

		val request = new SendEmailRequest()
			.withDestination(
				new Destination().withToAddresses(to))
			.withMessage(new Message()
				.withBody(
					new Body()
					.withHtml(new Content().withCharset("UTF-8").withData(body))
				)
				.withSubject(new Content().withCharset("UTF-8").withData(subject)))
			.withSource(from);

		client.sendEmail(request);
	}
	*/
}