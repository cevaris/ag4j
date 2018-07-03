package com.cevaris.ag4j;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.cevaris.ag4j.logger.Logger;
import com.cevaris.ag4j.logger.LoggerFactory;

class FileWalker extends SimpleFileVisitor<Path> {
  private static final Logger logger = LoggerFactory.get();

  private final Map<Path, Set<String>> ignorePatterns = new HashMap<>();

  private Map<Path, Set<String>> mkignorePatters() {
    Map<Path, Set<String>> tmp = new HashMap<>();

    File gitIgnoreGlobalFile = new File(System.getProperty("HOME"), ".gitignore_global");
    Path gitIgnoreGlobalPath = gitIgnoreGlobalFile.toPath();

    if (gitIgnoreGlobalFile.exists()) {
      List<String> patterns = null;
      try {
        patterns = Files.readAllLines(gitIgnoreGlobalPath);
        tmp.put(gitIgnoreGlobalPath, new HashSet<>(patterns));
      } catch (IOException e) {
        logger.debug(String.format(
            "Skipping ignore file %s: not readable",
            gitIgnoreGlobalFile.getPath()
        ));
      }
    }

    return tmp;
  }

  @Override
  public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {
    FileVisitResult result = super.preVisitDirectory(dir, attrs);
    if (Constants.SKIP_PATH_NAMES.contains(dir.getFileName().toString())) {
      logger.debug(String.format("skipping %s", dir));
      result = FileVisitResult.SKIP_SUBTREE;
    } else if (attrs.isDirectory()) {
      logger.debug(String.format("looking for ignore files in %s", dir));
      for (String name : Constants.IGNORE_PATH_NAMES) {
        Path ignoreFilePath = dir.resolve(name);
        if (Files.exists(ignoreFilePath)) {
          // parse ignore file
          logger.debug(String.format("parsing ignore file %s", ignoreFilePath));
          Set<String> patterns = IgnoreUtils.parsePath(ignoreFilePath);
          if (!patterns.isEmpty()) {
            ignorePatterns.put(ignoreFilePath.getParent(), patterns);
            logger.debug(String.format("found patterns: %s", String.join(", ", patterns)));
          }

        } else {
          logger.debug(String.format("skipping ignore file %s: not readable", ignoreFilePath));
        }
      }

      if (dir.getFileName().endsWith(".git") && Files.exists(dir.resolve("info/exclude"))) {
        // include info/exclude ignore patterns too
      }
    }

    return result;
  }

  @Override
  public FileVisitResult visitFileFailed(Path file, IOException exc) throws IOException {
    FileVisitResult result = super.visitFileFailed(file, exc);
    logger.error(String.format("%s", exc));
    return result;
  }

  @Override
  public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
    FileVisitResult result = super.visitFile(file, attrs);
    if (!attrs.isDirectory()) {
      logger.debug(String.format("attempt pattern match on %s", file));
    }
    return result;
  }
}
