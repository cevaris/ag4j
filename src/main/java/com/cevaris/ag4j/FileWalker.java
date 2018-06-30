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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

class FileWalker extends SimpleFileVisitor<Path> {
  private Logger logger = LoggerFactory.getLogger(FileWalker.class);

  private final Map<Path, Set<String>> ignorePatterns;

  FileWalker() {
    this.ignorePatterns = mkignorePatters();
  }

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
            "DEBUG: Skipping ignore file %s: not readable",
            gitIgnoreGlobalFile.getPath()
        ));
      }
    }

    return tmp;
  }

  @Override
  public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
    FileVisitResult result = super.visitFile(file, attrs);
    if (attrs.isDirectory()) {
      logger.debug(String.format("DEBUG: looking for ignore files in %s", file));
    } else {
      logger.debug(String.format("DEBUG: attempt pattern match on %s", file));
    }
    return result;
  }
}
