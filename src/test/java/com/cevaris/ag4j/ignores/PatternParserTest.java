package com.cevaris.ag4j.ignores;

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
      continue;
    }
    return foundMatch;
  }

  @Test
  public void testSimpleSubtreeSiblings() {
    PatternParser parser = new PatternParser();
    Set<ParsedPattern> patterns = parser.parse("**/logs");
    List<Tuple2<String, Boolean>> shouldMatch = Arrays.asList(
        Tuple2.tuple("/logs/debug.log", true),
        Tuple2.tuple("/logs/monday/foo.bar", true),
        Tuple2.tuple("/build/logs/debug.log", true),
        Tuple2.tuple("/build/logs", true),
        Tuple2.tuple("/build/logs/", true),
        Tuple2.tuple("/build/logs.txt", false)
    );

    for (Tuple2<String, Boolean> example : shouldMatch) {
      boolean actual = testMatch(patterns, example.l());
      Assert.assertEquals(String.format("%s", example.l()), example.r(), actual);
    }
  }
}
