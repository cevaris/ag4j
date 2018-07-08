package com.cevaris.ag4j.ignores;

import java.nio.file.Path;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.cevaris.ag4j.Constants;

public class IgnoreRepoImpl implements IgnoreRepo {
  /**
   * Thread safe ignore pattern -> java regex parser
   */
  private final PatternParser parser = new PatternParser();
  /**
   * One entry for every .*ignore file there is in search path.
   * The value is set of parsed patterns which includes both
   * - Any parent defined .*ignore patterns
   * - Any current level defined .*ignore patterns
   */
  private final Map<Path, Set<ParsedPattern>> ignoredPathMap = new HashMap<>();
  private final Map<Path, Set<ParsedPattern>> negatedPathMap = new HashMap<>();

  @Override
  public void add(Path parent, List<String> patterns) {
    if (patterns.isEmpty()) {
      // to limit memory, we should only store ignore patterns with directories that have an ignore
      // file.
      return;
    }

    Set<ParsedPattern> collectedPatterns = new HashSet<>();
    Set<ParsedPattern> collectedNegated = new HashSet<>();

    // TODO: should walk up to nearest .gitignore + parsed paths
    Iterator<Path> iter = parent.iterator();
    Path curr = Constants.ROOT_PATH; // initialize with rootPath
    while (iter.hasNext()) {
      curr = curr.resolve(iter.next());

      Set<ParsedPattern> candidatePatterns = ignoredPathMap.get(curr);
      if (candidatePatterns != null) {
        collectedPatterns.addAll(candidatePatterns);
      }
    }

    // parse patterns and collect any non empty [[ParsedPatterns]]
    for (String pattern : patterns) {
      PatternParser.Result parsedPatterns = parser.parse(pattern);
      if (!parsedPatterns.ignored().isEmpty()) {
        collectedPatterns.addAll(parsedPatterns.ignored());
      }
      if (!parsedPatterns.negated().isEmpty()) {
        collectedNegated.addAll(parsedPatterns.negated());
      }
    }

    ignoredPathMap.put(parent, collectedPatterns);
    negatedPathMap.put(parent, collectedNegated);
    return; // used as debug breakpoint
  }

  @Override
  public boolean shouldIgnore(Path path) {
    Set<ParsedPattern> patterns = Collections.emptySet();

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

    if (patterns.isEmpty()) {
      // no pattern, no ignoring/filtering
      return false;
    }

    String pathStr = path.toString();

    boolean foundMatch = false;
    for (ParsedPattern pattern : patterns) {
      foundMatch |= pattern.isMatch(pathStr);
    }

    boolean foundNegMatch = false;
    if(foundMatch && negatedPathMap.containsKey(currParent)){
      Set<ParsedPattern> negated = negatedPathMap.get(currParent);
      for (ParsedPattern pattern : negated) {
        foundNegMatch |= pattern.isMatch(pathStr);
      }
    }

    return foundMatch && !foundNegMatch;
  }

  // visible for testing
  Set<ParsedPattern> getPatterns(Path path) {
    return Collections.unmodifiableSet(ignoredPathMap.get(path));
  }
}
