package com.cevaris.ag4j;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.nio.file.Path;
import java.util.HashSet;
import java.util.NoSuchElementException;
import java.util.Scanner;
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

  public static Set<String> parsePath(Path path) {
    Set<String> patterns = new HashSet<>();
    Scanner scanner = null;
    try {
      scanner = new Scanner(new FileReader(path.toFile()));
      String line;
      while ((line = scanner.nextLine()) != null) {
        String candidate = line.trim();
        // ignore commented lines
        if (!candidate.startsWith("#")) {
          patterns.add(candidate);
        }
      }
    } catch (FileNotFoundException | NoSuchElementException e) {
      // empty or non-existing file
    }
    return patterns;
  }


}
