package com.cevaris.ag4j.logger;

public class LoggerFactory {
  private static Logger logger;

  public static Logger get() {
    if (logger == null) {
      logger = new Slf4jLoggerImpl();
    }
    return logger;
  }

}
