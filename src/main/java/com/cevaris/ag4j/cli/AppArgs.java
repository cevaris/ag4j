package com.cevaris.ag4j.cli;

import java.util.Optional;

public interface AppArgs {
  void parse(String[] args);

  String[] getArgs();

  Boolean isDebug();

  Optional<String> pagerCommand();
}
