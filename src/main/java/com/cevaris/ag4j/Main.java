package com.cevaris.ag4j;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.cevaris.ag4j.cli.ApacheAppArgs;
import com.cevaris.ag4j.cli.AppArgs;
import com.cevaris.ag4j.logger.Logger;
import com.cevaris.ag4j.logger.LoggerFactory;

public class Main {
  private static final Logger logger = LoggerFactory.get();

  private final static String WORKING_DIR = System.getProperty("user.dir");
  private final static String WAT = "What do you want to search for?";
  private final static String FILE_NOT_FOUND =
      "Error stat()ing: %s\n" +
          "Error opening directory %s: No such file or directory";

  private void start(AppArgs args) {
    if (args.getArgs().length == 0) {
      logger.error(WAT);
      System.exit(1);
    }

    String[] nonOptions = args.getArgs();
    FileSystem pathFinder = FileSystems.getDefault();

    List<Path> sourcePaths = validateFileSources(Arrays.copyOfRange(nonOptions, 1, nonOptions.length));
    if (sourcePaths.isEmpty()) {
      sourcePaths.add(pathFinder.getPath(WORKING_DIR).toAbsolutePath());
    }

    FileWalker walker = new FileWalker();
    for (Path sourcPath : sourcePaths) {
      try {
        logger.debug(String.format("walking %s", sourcPath));
        Files.walkFileTree(sourcPath, walker);
      } catch (IOException e) {
        // should not get here as we already validated the files
      }
    }

    System.exit(0);
  }
  public static void main(String[] args) {
    AppArgs appArgs = new ApacheAppArgs();
    appArgs.parse(args);

    new Main().start(appArgs);
  }

  private static String fileNotFound(String name) {
    return String.format(FILE_NOT_FOUND, name, name);
  }

  private static List<Path> validateFileSources(String[] args) {
    List<Path> sourcePaths = new ArrayList<>();
    for (String arg : args) {
      File file = new File(arg);
      if (file.exists()) {
        sourcePaths.add(file.toPath().toAbsolutePath());
      } else {
        logger.error(fileNotFound(arg));
      }
    }
    return sourcePaths;
  }
}
