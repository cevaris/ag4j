package com.cevaris.ag4j.ignores;

import java.util.HashSet;
import java.util.Set;
import java.util.regex.Pattern;

public class PatternParser {
  public Set<ParsedPattern> parse(String rawPattern) {
    final Set<ParsedPattern> results = new HashSet<>();
    boolean isNegated = false;

    String sanitizedPattern = rawPattern
        .trim()
        .replace(".", "\\.");

    // ignore empty lines
    if (sanitizedPattern.isEmpty()) {
      return results;
    }

    // ignore commented lines
    if (sanitizedPattern.startsWith("#")) {
      return results;
    }

    // mark negated pattern
    if (sanitizedPattern.startsWith("!")) {
      isNegated = true;
      sanitizedPattern = sanitizedPattern.substring(1);
    }

    if (sanitizedPattern.startsWith("**")) {
      String minusGlobs = sanitizedPattern.substring(2);
      results.add(
          new ParsedPattern(
              Pattern.compile(String.format("%s/", minusGlobs)),
              sanitizedPattern,
              isNegated
          )
      );
      results.add(
          new ParsedPattern(
              Pattern.compile(String.format("%s/?$", minusGlobs)),
              sanitizedPattern,
              isNegated
          )
      );
    } else if (sanitizedPattern.startsWith("*")) {
      String minusGlobs = sanitizedPattern.substring(1);
      results.add(
          new ParsedPattern(
              Pattern.compile(String.format("^/[^/]+%s", minusGlobs)),
              sanitizedPattern,
              isNegated
          )
      );
    } else {
      results.add(
          new ParsedPattern(
              Pattern.compile(sanitizedPattern),
              sanitizedPattern,
              isNegated
          )
      );
    }


    return results;
  }

}
