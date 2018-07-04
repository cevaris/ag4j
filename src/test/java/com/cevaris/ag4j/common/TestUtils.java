package com.cevaris.ag4j.common;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import com.cevaris.ag4j.Constants;

public class TestUtils {
  public static Path path(String... paths) {
    Path curr = Constants.ROOT_PATH;
    for (String section : paths) {
      curr = curr.resolve(section);
    }
    return curr;
  }

  public static <T extends Comparable<? super T>> List<T> sorted(Set<T> ss) {
    List<T> ls = new ArrayList<>(ss.size());
    ls.addAll(ss);
    Collections.sort(ls);
    return ls;
  }

  public static <T extends Comparable<? super T>> List<T> sorted(List<T> ls) {
    Collections.sort(ls);
    return ls;
  }
}
