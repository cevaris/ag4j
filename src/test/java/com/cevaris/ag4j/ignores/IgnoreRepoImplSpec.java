package com.cevaris.ag4j.ignores;

import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import org.junit.Assert;
import org.junit.Test;

public class IgnoreRepoImplSpec {

  private Path path(String... paths) {
    return FileSystems.getDefault().getPath("", paths);
  }

  @Test
  public void testSinglePattern() {
    IgnoreRepoImpl ignoreRepo = new IgnoreRepoImpl();

    Path testPath = path("/test");
    List<String> patterns = new ArrayList<String>() {{
      add("ignore1");
      add("ignore2");
    }};
    ignoreRepo.add(testPath, patterns);


    Path childPath = path("/test/subtree");
    List<String> childPatterns = new ArrayList<String>() {{
      add("ignore2"); // duplicate from parent
      add("ignore3");
    }};
    ignoreRepo.add(childPath, childPatterns);

    Path siblingPath = path("/sibling/");
    List<String> siblingPatterns = new ArrayList<String>() {{
      add("ignore4");
    }};
    ignoreRepo.add(siblingPath, siblingPatterns);

    Assert.assertEquals(sorted(ignoreRepo.getPatterns(testPath)), sorted(Arrays.asList("ignore1", "ignore2")));
    Assert.assertEquals(sorted(ignoreRepo.getPatterns(childPath)), sorted(Arrays.asList("ignore1", "ignore2", "ignore3")));
    Assert.assertEquals(sorted(ignoreRepo.getPatterns(siblingPath)), sorted(Collections.singletonList("ignore4")));
  }

  private <T extends Comparable<? super T>> List<T> sorted(Set<T> ss) {
    List<T> ls = new ArrayList<>(ss.size());
    ls.addAll(ss);
    Collections.sort(ls);
    return ls;
  }

  private <T extends Comparable<? super T>> List<T> sorted(List<T> ls) {
    Collections.sort(ls);
    return ls;
  }
}
