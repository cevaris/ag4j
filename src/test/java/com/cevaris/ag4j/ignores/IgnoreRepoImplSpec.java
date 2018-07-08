package com.cevaris.ag4j.ignores;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.junit.Assert;
import org.junit.Test;

import static com.cevaris.ag4j.common.TestUtils.path;
import static com.cevaris.ag4j.common.TestUtils.sorted;

import static org.junit.Assert.assertEquals;

public class IgnoreRepoImplSpec {

  @Test
  public void testSimpleSubtreeSiblings() {
    IgnoreRepoImpl ignoreRepo = new IgnoreRepoImpl();

    Path testPath = path("test");
    List<String> patterns = new ArrayList<String>() {{
      add("ignore1");
      add("ignore2");
    }};
    ignoreRepo.add(testPath, patterns);


    Path subTree = path("test", "subtree");
    List<String> childPatterns = new ArrayList<String>() {{
      add("ignore2"); // duplicate from parent
      add("ignore3");
    }};
    ignoreRepo.add(subTree, childPatterns);


    Path siblingPath = path("sibling");
    List<String> siblingPatterns = new ArrayList<String>() {{
      add("ignore4");
    }};
    ignoreRepo.add(siblingPath, siblingPatterns);


    assertEquals(sorted(Arrays.asList("ignore1", "ignore2")), sortedPatterns(ignoreRepo, testPath));
    assertEquals(sorted(Arrays.asList("ignore1", "ignore2", "ignore3")), sortedPatterns(ignoreRepo, subTree));
    assertEquals(sorted(Collections.singletonList("ignore4")), sortedPatterns(ignoreRepo, siblingPath));
  }

  @Test
  public void testNegation() {
    IgnoreRepo ignoreRepo = new IgnoreRepoImpl();
    ignoreRepo.add(path("/"), Arrays.asList("aaa/", "!aaa/ccc"));

    List<String> shouldMatch = Arrays.asList(
        "/aaa/bbb/file.txt",
        "/aaa/bbb/",
        "/aaa/"
    );

    List<String> shouldNotMatch = Arrays.asList(
        "/aaa/ccc/",
        "/aaa/ccc/otherfile.txt"
    );

    testMatch(ignoreRepo, shouldMatch, shouldNotMatch);
  }

  private List<String> sortedPatterns(IgnoreRepoImpl ignoreRepo, Path testPath) {
    return ignoreRepo.getPatterns(testPath).stream().map(ParsedPattern::getSource).sorted().collect(Collectors.toList());
  }

  private void testMatch(IgnoreRepo ignoreRepo, List<String> shouldMatch, List<String> shouldNotMatch) {
    for (String example : shouldMatch) {
      boolean actual = ignoreRepo.shouldIgnore(path(example));
      Assert.assertTrue(String.format("%s", example), actual);
    }
    for (String example : shouldNotMatch) {
      boolean actual = ignoreRepo.shouldIgnore(path(example));
      Assert.assertFalse(String.format("%s", example), actual);
    }
  }
}
