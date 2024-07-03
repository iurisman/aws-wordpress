package urisman.wp.monitor;

import com.amazonaws.regions.Regions;
import com.amazonaws.services.simpleemail.AmazonSimpleEmailServiceClientBuilder;
import com.amazonaws.services.simpleemail.model.*;

public class Email implements Config {
  public static void send(String subject, String body) {

    final var client =
      AmazonSimpleEmailServiceClientBuilder.standard()
        // Replace US_WEST_2 with the AWS Region you're using for Amazon SES.
        .withRegion(Regions.US_WEST_2).build();

    final var request = new SendEmailRequest()
      .withDestination(new Destination().withToAddresses(toEmail))
      .withMessage(
        new Message()
          .withBody(
            new Body()
              .withText(new Content().withCharset("UTF-8").withData(body))
          )
          .withSubject(new Content().withCharset("UTF-8").withData(subject))
      )
      .withSource(fromEmail);
    client.sendEmail(request);
  }

}
