package com.cevaris.ag4j.cli;

import java.util.Optional;

public interface AppArgs {
  void parse(String[] args) throws AppArgsException;

  String[] getArgs();

  Boolean isDebug();

  Optional<String> pagerCommand();
}
