package com.cevaris.ag4j.logger;

import java.util.concurrent.atomic.AtomicInteger;

import org.slf4j.LoggerFactory;

/**
 * Global logger
 */
public class LoggerImpl implements Logger {
  private org.slf4j.Logger logger = LoggerFactory.getLogger(org.slf4j.Logger.ROOT_LOGGER_NAME);

  private AtomicInteger lineNumber = new AtomicInteger(0);

  public void debug(String message) {
    int currLineNumber = lineNumber.incrementAndGet();
    logger.info(String.format("%5d DEBUG: %s", currLineNumber, message));
  }

  public void error(String message) {
    int currLineNumber = lineNumber.incrementAndGet();
    logger.info(String.format("%5d ERR: %s", currLineNumber, message));
  }

  public void info(String message) {
    int currLineNumber = lineNumber.incrementAndGet();
    logger.info(String.format("%5d %s", currLineNumber, message));
  }

}
