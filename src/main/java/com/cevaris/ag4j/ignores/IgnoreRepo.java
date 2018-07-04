package com.cevaris.ag4j.ignores;

import java.nio.file.Path;
import java.util.List;

public interface IgnoreRepo {
  /**
   * @param parent parent directly these patterns should execute on
   * @param patterns accepts dir/file names and single/double globs
   */
  void add(Path parent, List<String> patterns);

  /**
   * @param path can be path to directory or name
   */
  boolean shouldIgnore(Path path);
}
