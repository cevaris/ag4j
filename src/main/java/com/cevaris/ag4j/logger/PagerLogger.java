package com.cevaris.ag4j.logger;

import java.io.PrintStream;

public class PagerLogger implements Logger {
  private final PrintStream out;

  public PagerLogger(PrintStream out) {
    this.out = out;
  }

  public void debug(String message) {
    out.println(String.format("DEBUG: %s", message));
  }

  public void error(String message) {
    out.println(String.format("ERR: %s", message));
  }

  public void info(String message) {
    out.println(message);
  }

}
