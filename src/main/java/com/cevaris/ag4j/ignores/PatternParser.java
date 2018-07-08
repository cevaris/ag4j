package com.cevaris.ag4j.ignores;

import java.util.HashSet;
import java.util.Set;
import java.util.function.Consumer;
import java.util.regex.Pattern;

public class PatternParser {
  static class Result {
    private final Set<ParsedPattern> ignoredPatterns;
    private final Set<ParsedPattern> negatedPatterns;

    public Result(Set<ParsedPattern> ignoredPatterns, Set<ParsedPattern> negatedPatterns) {
      this.ignoredPatterns = ignoredPatterns;
      this.negatedPatterns = negatedPatterns;
    }

    public boolean isEmpty() {
      return ignoredPatterns.isEmpty() && negatedPatterns.isEmpty();
    }

    public Set<ParsedPattern> ignored() {
      return ignoredPatterns;
    }

    public Set<ParsedPattern> negated() {
      return negatedPatterns;
    }
  }

  public Result parse(String rawPattern) {
    final Set<ParsedPattern> ignored = new HashSet<>();
    final Set<ParsedPattern> negated = new HashSet<>();

    Consumer<ParsedPattern> addIgnored = ignored::add;
    Consumer<ParsedPattern> addNegated = negated::add;

    boolean isNegated = false;

    String sanitizedPattern = rawPattern
        .trim()
        .replace(".", "\\.");

    // ignore empty lines
    if (sanitizedPattern.isEmpty()) {
      return new Result(ignored, negated);
    }

    // ignore commented lines
    if (sanitizedPattern.startsWith("#")) {
      return new Result(ignored, negated);
    }

    // mark negated pattern
    if (sanitizedPattern.startsWith("!")) {
      isNegated = true;
      sanitizedPattern = sanitizedPattern.substring(1);
    }

    if (sanitizedPattern.startsWith("**")) {
      String minusGlobs = sanitizedPattern.substring(2);
      ParsedPattern p1 = new ParsedPattern(
          Pattern.compile(String.format("%s/", minusGlobs)),
          sanitizedPattern,
          isNegated
      );
      if (isNegated) {
        addNegated.accept(p1);
      } else {
        addIgnored.accept(p1);
      }

      ParsedPattern p2 = new ParsedPattern(
          Pattern.compile(String.format("%s/?$", minusGlobs)),
          sanitizedPattern,
          isNegated
      );
      if (isNegated) {
        addNegated.accept(p2);
      } else {
        addIgnored.accept(p2);
      }

    } else if (sanitizedPattern.startsWith("*")) {
      String minusGlobs = sanitizedPattern.substring(1);
      ParsedPattern p = new ParsedPattern(
          Pattern.compile(String.format("^/[^/]+%s", minusGlobs)),
          sanitizedPattern,
          isNegated
      );
      if (isNegated) {
        addNegated.accept(p);
      } else {
        addIgnored.accept(p);
      }
    } else {
      ParsedPattern p = new ParsedPattern(
          Pattern.compile(sanitizedPattern),
          sanitizedPattern,
          isNegated
      );
      if (isNegated) {
        addNegated.accept(p);
      } else {
        addIgnored.accept(p);
      }
    }

    return new Result(ignored, negated);
  }

}
