package com.cevaris.ag4j.cli;

import java.util.Optional;
import java.util.function.Supplier;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

public class ApacheAppArgs implements AppArgs {
  private CommandLine cmdLine;

  @Override
  public void parse(String[] args) throws AppArgsException {
    DefaultParser parser = new DefaultParser();
    Options opts = new Options();
    opts.addOption("G", true, "filter by path");
    opts.addOption("D", "debug");
    opts.addOption("", "pager", true, "pipes to less");

    try {
      cmdLine = parser.parse(opts, args);
    } catch (ParseException e) {
      throw new AppArgsException(e);
    }
  }

  @Override
  public String[] getArgs() {
    return withDefault(new String[0], () -> cmdLine.getArgs());
  }

  @Override
  public Boolean isDebug() {
    return withDefault(false, () -> cmdLine.hasOption("D"));
  }

  @Override
  public Optional<String> pagerCommand() {
    return withDefault(Optional.empty(), () -> {
      if (cmdLine.hasOption("pager")) {
        return Optional.of(cmdLine.getOptionValue("pager"));
      } else {
        return Optional.empty();
      }
    });
  }

  private <A> A withDefault(A d, Supplier<A> value) {
    return cmdLine == null ? d : value.get();
  }
}
