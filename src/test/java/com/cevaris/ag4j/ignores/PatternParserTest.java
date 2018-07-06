package com.cevaris.ag4j.ignores;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

import com.cevaris.ag4j.common.Tuple2;

import org.junit.Assert;
import org.junit.Test;

public class PatternParserTest {

  private boolean testMatch(Set<ParsedPattern> patterns, String target) {
    boolean foundMatch = false;
    for (ParsedPattern pattern : patterns) {
      foundMatch |= pattern.isMatch(target);
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
  public void testSingleGlob() {
    PatternParser parser = new PatternParser();
    Set<ParsedPattern> patterns = parser.parse("*.log");

    List<String> shouldMatch = Arrays.asList(
        "/debug.log",
        "/foo.log"
    );

    List<String> shouldNotMatch = Arrays.asList(
        "/logs/debug.log",
        "/logs/other.log"
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

}
