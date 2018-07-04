package com.cevaris.ag4j.ignores;

import java.util.HashSet;
import java.util.Set;
import java.util.regex.Pattern;

public class PatternParser {
  public Set<ParsedPattern> parse(String rawPattern) {
    Set<ParsedPattern> results = new HashSet<>();

    rawPattern = rawPattern
        .trim()
        .replace(".", "\\.");

    if (rawPattern.startsWith("#")) {
      return results;
    }

    if (rawPattern.startsWith("**")) {
      String minusGlobs = rawPattern.substring(2);
      results.add(
          new ParsedPattern(
              Pattern.compile(String.format("%s/", minusGlobs)),
              rawPattern
          )
      );
      results.add(
          new ParsedPattern(
              Pattern.compile(String.format("%s/?$", minusGlobs)),
              rawPattern
          )
      );
    } else if (rawPattern.startsWith("*")) {
      String minusGlobs = rawPattern.substring(1);
      results.add(
          new ParsedPattern(
              Pattern.compile(String.format("^/[^/]+%s", minusGlobs)),
              rawPattern
          )
      );
    }


    return results;
  }

}
