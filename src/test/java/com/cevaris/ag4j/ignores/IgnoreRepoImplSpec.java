package com.cevaris.ag4j.ignores;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
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


    Path childPath = path("test", "subtree");
    List<String> childPatterns = new ArrayList<String>() {{
      add("ignore2"); // duplicate from parent
      add("ignore3");
    }};
    ignoreRepo.add(childPath, childPatterns);


    Path siblingPath = path("sibling");
    List<String> siblingPatterns = new ArrayList<String>() {{
      add("ignore4");
    }};
    ignoreRepo.add(siblingPath, siblingPatterns);


    assertEquals(sorted(Arrays.asList("ignore1", "ignore2")), ignoreRepo.getPatterns(testPath).stream().map(ParsedPattern::getSource).sorted().collect(Collectors.toList()));
    assertEquals(sorted(Arrays.asList("ignore1", "ignore2", "ignore3")), ignoreRepo.getPatterns(childPath).stream().map(ParsedPattern::getSource).sorted().collect(Collectors.toList()));
    assertEquals(sorted(Collections.singletonList("ignore4")), ignoreRepo.getPatterns(siblingPath).stream().map(ParsedPattern::getSource).sorted().collect(Collectors.toList()));
  }
}
