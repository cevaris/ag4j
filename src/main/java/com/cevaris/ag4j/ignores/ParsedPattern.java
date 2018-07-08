package com.cevaris.ag4j.ignores;

import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ParsedPattern {
  private final Pattern pattern;
  private final String source;
  private final boolean isNegated;

  public ParsedPattern(Pattern pattern, String source, boolean isNegated) {
    this.pattern = pattern;
    this.source = source;
    this.isNegated = isNegated;
  }

  public boolean isMatch(String str) {
    Matcher matches = pattern.matcher(str);
    boolean result = matches.find();
    return result;
  }

  @Override
  public String toString() {
    return String.format("ParsedPattern(pattern=%s, source=%s, isNegated=%s)", pattern, source, isNegated);
  }

  public boolean isNegated() {
    return isNegated;
  }

  public String getSource() {
    return source;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    ParsedPattern that = (ParsedPattern) o;
    return isNegated == that.isNegated &&
        Objects.equals(pattern.pattern(), that.pattern.pattern()) &&
        Objects.equals(source, that.source);
  }

  @Override
  public int hashCode() {
    return Objects.hash(pattern.pattern(), source, isNegated);
  }
}
