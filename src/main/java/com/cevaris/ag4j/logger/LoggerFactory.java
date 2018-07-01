package com.cevaris.ag4j.logger;

import java.io.IOException;
import java.io.PrintStream;
import java.util.Optional;

public class LoggerFactory {
  private static Logger logger;
  private static Optional<PrintStream> pager = Optional.empty();

  public static Logger get() {
    if (logger == null) {
      logger = new PagerLogger(System.out);
    }
    return logger;
  }

  public static Logger get(String pagerCommand) {
    PrintStream out = System.out;
    if (!pagerCommand.trim().isEmpty()) {
      try {
        Process process = Runtime.getRuntime().exec(pagerCommand);
        pager = Optional.of(new PrintStream(process.getOutputStream()));

      } catch (IOException e) {
        out.println(String.format("sh: %s: command not found", pagerCommand));
      }
    }
    return logger = new PagerLogger(out);
  }

  public static void close() {
    if (pager.isPresent()) {
      pager.get().flush();
      pager.get().close();
    }
  }
}
