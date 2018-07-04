package com.cevaris.ag4j.ignores;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ParsedPattern {
  private final Pattern pattern;
  private final String source;

  public ParsedPattern(Pattern pattern, String source) {
    this.pattern = pattern;
    this.source = source;
  }

  public boolean isMatch(String str) {
    Matcher matches = pattern.matcher(str);
    boolean result = matches.find();
    return result;
  }

  @Override
  public String toString() {
    return String.format("ParsedPattern(pattern=%s, source=%s)", pattern, source);
  }
}
