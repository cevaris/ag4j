package com.cevaris.ag4j.ignores;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

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

    assertEquals(sorted(ignoreRepo.getPatterns(testPath)), sorted(Arrays.asList("ignore1", "ignore2")));
    assertEquals(sorted(ignoreRepo.getPatterns(childPath)), sorted(Arrays.asList("ignore1", "ignore2", "ignore3")));
    assertEquals(sorted(ignoreRepo.getPatterns(siblingPath)), sorted(Collections.singletonList("ignore4")));
  }
}
