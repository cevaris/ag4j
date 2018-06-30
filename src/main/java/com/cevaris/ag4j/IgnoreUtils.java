package com.cevaris.ag4j;

import java.io.File;
import java.util.HashSet;
import java.util.Set;

public class IgnoreUtils {

  public Set<String> notIgnoredFiles(File[] files) {
    FileWalker walker = new FileWalker();
    // for each folder
    //   check if this folder is ignored
    //   if we find a ignore file, parse save ignore patterns including parents
    // add git global ignore patterns to ignore patterns
    return new HashSet<>();
  }


}
