package com.cevaris.ag4j;

import java.util.HashSet;
import java.util.Set;

public class Constants {
  public static final Set<String> SKIP_PATH_NAMES = new HashSet<>();


  public static final Set<String> IGNORE_PATH_NAMES = new HashSet<>();


  static {
    SKIP_PATH_NAMES.add(".git");

    IGNORE_PATH_NAMES.add(".ignore");
    IGNORE_PATH_NAMES.add(".gitignore");
    IGNORE_PATH_NAMES.add(".hgignore");
    IGNORE_PATH_NAMES.add(".agignore");

  }
}
