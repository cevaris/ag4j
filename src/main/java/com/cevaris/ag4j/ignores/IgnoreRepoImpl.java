package com.cevaris.ag4j.ignores;

import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class IgnoreRepoImpl implements IgnoreRepo {
  private final Map<Path, Set<String>> ignoredPathMap = new HashMap<>();

  // TODO: better rootPath path builder
  static final Path rootPath =
      FileSystems.getDefault().getRootDirectories().iterator().next().getRoot();

  @Override
  public void add(Path parent, List<String> patterns) {
    if (patterns.isEmpty()) {
      // to limit memory, we should only store ignore data with directories that have a ignore file.
      return;
    }

    // add all grandparent paths ignore patterns to current ignore pattern

    List<String> parentPatterns = new ArrayList<>();

    Iterator<Path> iter = parent.iterator();
    Path curr = rootPath; // initialize with rootPath
    while (iter.hasNext()) {
      curr = curr.resolve(iter.next());
      Set<String> candidatePatterns = ignoredPathMap.get(curr);
      if (candidatePatterns != null) {
        parentPatterns.addAll(candidatePatterns);
      }
    }

    patterns.addAll(parentPatterns);
    ignoredPathMap.put(parent, new HashSet<>(patterns));
    return;
  }

  @Override
  public boolean shouldIgnore(Path path) {
    return false;
  }

  // visible for testing
  public Set<String> getPatterns(Path path) {
    return Collections.unmodifiableSet(ignoredPathMap.get(path));
  }
}
