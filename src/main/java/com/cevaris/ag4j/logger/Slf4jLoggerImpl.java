package com.cevaris.ag4j.logger;

import org.slf4j.LoggerFactory;

/**
 * Global logger
 */
public class Slf4jLoggerImpl implements Logger {
  private org.slf4j.Logger logger = LoggerFactory.getLogger(org.slf4j.Logger.ROOT_LOGGER_NAME);

  public void debug(String message) {
    logger.info(String.format("DEBUG: %s", message));
  }

  public void error(String message) {
    logger.info(String.format("ERR: %s", message));
  }

  public void info(String message) {
    logger.info(message);
  }
}
