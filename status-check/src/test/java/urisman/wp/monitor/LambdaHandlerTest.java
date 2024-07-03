package urisman.wp.monitor;

import com.amazonaws.services.lambda.runtime.ClientContext;
import com.amazonaws.services.lambda.runtime.CognitoIdentity;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.LambdaLogger;
import com.amazonaws.services.lambda.runtime.events.ScheduledEvent;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
public class LambdaHandlerTest {

  Context context = new Context() {

    @Override
    public String getAwsRequestId() {
      return null;
    }

    @Override
    public String getLogGroupName() {
      return null;
    }

    @Override
    public String getLogStreamName() {
      return null;
    }

    @Override
    public String getFunctionName() {
      return null;
    }

    @Override
    public String getFunctionVersion() {
      return null;
    }

    @Override
    public String getInvokedFunctionArn() {
      return "Local";
    }

    @Override
    public CognitoIdentity getIdentity() {
      return null;
    }

    @Override
    public ClientContext getClientContext() {
      return null;
    }

    @Override
    public int getRemainingTimeInMillis() {
      return 0;
    }

    @Override
    public int getMemoryLimitInMB() {
      return 0;
    }

    @Override
    public LambdaLogger getLogger() {
      return new LambdaLogger() {
        @Override public void log(String message) {
          System.out.println(message);
        }
        @Override public void log(byte[] bytes) {
          log(new String(bytes));
        }
      };
    }
  };

  @Test
  public void test() {
    String resp = new LambdaHandler().handleRequest(new ScheduledEvent(), context);
    assertEquals ("Wordpress sites check: OK", resp);
  }

}
