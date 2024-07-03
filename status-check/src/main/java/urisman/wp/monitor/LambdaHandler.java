package urisman.wp.monitor;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.ScheduledEvent;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.time.Duration;
import java.time.Instant;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

public class LambdaHandler implements Config {

  String handleRequest(ScheduledEvent event, Context context) {
    final var failedCount = new AtomicInteger(); // Just to make final
    final var emailBody = new StringBuilder();
      for(String url: sites) {
        final var checkResult = checkSite(url);
        checkResult.rc.ifLeftOrIfRight(
          t -> {
            failedCount.incrementAndGet();
            emailBody.append("\n" + url + " threw exception:\n  " + t.getMessage());
          },
          rc -> {
            emailBody.append("\n" + url +": rc = " + rc + ", latency = " + checkResult.latency());
          }
        );
    };
    final String emailSubject =
      "Wordpress sites check: " + (failedCount.get() == 0 ? "OK" : failedCount.get() + " Sites Failed");

    context.getLogger().log(emailSubject);
    Email.send(emailSubject, emailBody.toString());
    return emailSubject;
  }

  private record CheckResult(Either<Throwable, Integer> rc, Long latency) {}

  private CheckResult checkSite(String url) {
    final Instant start = Instant.now();
    try {
      final HttpURLConnection conn = (HttpURLConnection) new URL(url).openConnection();
      return new CheckResult(new Right(conn.getResponseCode()), Duration.between(start, Instant.now()).toMillis());
    }
    catch (Throwable t) {
      return new CheckResult(new Left(t), Duration.between(start, Instant.now()).toMillis());
    }
  }
}
