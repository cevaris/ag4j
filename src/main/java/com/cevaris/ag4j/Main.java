package com.cevaris.ag4j;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.LinkedList;
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

    List<File> files = validateFileSources(Arrays.copyOfRange(nonOptions, 1, nonOptions.length));
    Path[] sources = new Path[files.size() + 1];
    if (files.isEmpty()) {
      sources[0] = pathFinder.getPath(WORKING_DIR).toAbsolutePath();
    } else {
      for (Integer i = 0; i < files.size(); i++) {
        sources[i] = pathFinder.getPath(files.get(i).getAbsolutePath());
      }
    }

    FileWalker walker = new FileWalker();
    for (Path sourcPath : sources) {
      try {
        logger.debug(String.format("DEBUG: walking %s", sourcPath));
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

  private static List<File> validateFileSources(String[] args) {
    List<File> files = new LinkedList<>();
    for (String arg : args) {
      File file = new File(arg);
      if (file.exists()) {
        files.add(file);
      } else {
        logger.error(fileNotFound(arg));
      }
    }
    return files;
  }
}
