package urisman.wp.monitor;

import java.util.List;

public interface Config {
  final String fromEmail = "igor.urisman@gmail.com";
  final String toEmail = fromEmail;
  final List<String> sites = List.of("https://urisman.net/", "https://getvariant.com");
  final int networkTimeoutMillis = 2000;

}
