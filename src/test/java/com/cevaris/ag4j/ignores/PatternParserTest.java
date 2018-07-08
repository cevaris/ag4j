package com.cevaris.ag4j.ignores;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.Assert;
import org.junit.Test;

public class PatternParserTest {

  private boolean testMatch(Set<ParsedPattern> patterns, String target) {
    boolean foundMatch = false;
    for (ParsedPattern pattern : patterns) {
      foundMatch |= pattern.isMatch(target);
      continue;
    }
    return foundMatch;
  }

  private void testMatch(Set<ParsedPattern> patterns, List<String> shouldMatch, List<String> shouldNotMatch) {
    for (String example : shouldMatch) {
      boolean actual = testMatch(patterns, example);
      Assert.assertTrue(String.format("%s", example), actual);
    }
    for (String example : shouldNotMatch) {
      boolean actual = testMatch(patterns, example);
      Assert.assertFalse(String.format("%s", example), actual);
    }
  }

  @Test
  public void testEmptyPattern() {
    PatternParser parser = new PatternParser();
    Assert.assertEquals(0, parser.parse("").size());
    Assert.assertEquals(0, parser.parse("  ").size());
    Assert.assertEquals(0, parser.parse(" \n ").size());
    Assert.assertEquals(0, parser.parse(" \t ").size());
    Assert.assertEquals(0, parser.parse(" \r ").size());
  }

  @Test
  public void testSingleGlob() {
    PatternParser parser = new PatternParser();
    Set<ParsedPattern> patterns = parser.parse("*.log");

    List<String> shouldMatch = Arrays.asList(
        "/debug.log",
        "/foo.log"
    );

    List<String> shouldNotMatch = Arrays.asList(
        "/logs/debug.log",
        "/logs/other.log",
        "/build/logs/debug.log"
    );

    testMatch(patterns, shouldMatch, shouldNotMatch);
  }

  @Test
  public void testDoubleGlob() {
    PatternParser parser = new PatternParser();
    Set<ParsedPattern> patterns = parser.parse("**/logs");
    List<String> shouldMatch = Arrays.asList(
        "/logs/debug.log",
        "/logs/monday/foo.bar",
        "/build/logs/debug.log",
        "/build/logs",
        "/build/logs/"
    );

    List<String> shouldNotMatch = Arrays.asList(
        "/build/logs.gzip",
        "/build/logs.txt"
    );

    testMatch(patterns, shouldMatch, shouldNotMatch);
  }


  @Test
  public void testDoubleGlobFile() {
    PatternParser parser = new PatternParser();
    Set<ParsedPattern> patterns = parser.parse("**/logs/debug.log");
    List<String> shouldMatch = Arrays.asList(
        "/logs/debug.log",
        "/build/logs/debug.log"
    );

    List<String> shouldNotMatch = Arrays.asList(
        "/logs/build/debug.log",
        "/logs/nested/build/debug.log"
    );

    testMatch(patterns, shouldMatch, shouldNotMatch);
  }


  @Test
  public void testNegation() {
    PatternParser parser = new PatternParser();

    Set<ParsedPattern> patterns = new HashSet<>();
    patterns.addAll(parser.parse("!important.log"));

    List<String> shouldMatch = Arrays.asList(
        "/important.log",
        "/logs/important.log"

    );

    List<String> shouldNotMatch = Collections.emptyList();

    testMatch(patterns, shouldMatch, shouldNotMatch);

    Assert.assertEquals(1, patterns.size());
    Assert.assertEquals(true, patterns.stream().findFirst().get().isNegated());
  }

}
