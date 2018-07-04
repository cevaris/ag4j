package com.cevaris.ag4j.ignores;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.cevaris.ag4j.Constants;

public class IgnoreRepoImpl implements IgnoreRepo {
  private final Map<Path, Set<String>> ignoredPathMap = new HashMap<>();

  @Override
  public void add(Path parent, List<String> patterns) {
    if (patterns.isEmpty()) {
      // to limit memory, we should only store ignore patterns with directories that have an ignore
      // file.
      return;
    }

    List<String> parentPatterns = new ArrayList<>();

    Iterator<Path> iter = parent.iterator();
    Path curr = Constants.ROOT_PATH; // initialize with rootPath
    while (iter.hasNext()) {
      curr = curr.resolve(iter.next());
      Set<String> candidatePatterns = ignoredPathMap.get(curr);
      if (candidatePatterns != null) {
        parentPatterns.addAll(candidatePatterns);
      }
    }

    // add current dir patterns
    patterns.addAll(parentPatterns);
    ignoredPathMap.put(parent, new HashSet<>(patterns));
    return;
  }

  @Override
  public boolean shouldIgnore(Path path) {
    Set<String> patterns = Collections.emptySet();

    // Look at parents for any ignores, break when we find first match.
    Path currParent = path.getParent();
    while (currParent != null && patterns.isEmpty()) {
      if (ignoredPathMap.containsKey(currParent)) {
        patterns = ignoredPathMap.get(currParent);
      }
      currParent = currParent.getParent();
    }

    // we may not find any patterns for whole absolute path.
    // this can happen if file path does not fall within a repo.

    boolean foundMatch = false;
    for (String pattern : patterns) {
    }
    return foundMatch;
  }

  // visible for testing
  Set<String> getPatterns(Path path) {
    return Collections.unmodifiableSet(ignoredPathMap.get(path));
  }
}
