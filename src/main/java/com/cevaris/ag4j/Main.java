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

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

public class Main {
  private static final Logger logger = LoggerFactory.get();

  private final static String WAT = "What do you want to search for?";
  private final static String FILE_NOT_FOUND =
      "Error stat()ing: %s\n" +
          "Error opening directory %s: No such file or directory";


  public static void main(String[] args) throws ParseException {
    DefaultParser parser = new DefaultParser();
    Options opts = new Options();
    opts.addOption("G", true, "filter by path");
    opts.addOption("D", "debug");

    CommandLine cmdLine = parser.parse(opts, args);

    if (args.length == 0) {
      System.out.println(WAT);
      System.exit(1);
    }

    if (cmdLine.hasOption("D")) {
      System.out.println(Arrays.toString(cmdLine.getArgs()));
      System.out.println(String.join(",", args));
    }

    String[] nonOptions = cmdLine.getArgs();
    String pattern = nonOptions[0];
    FileSystem pathFinder = FileSystems.getDefault();

    List<File> files = collectFiles(Arrays.copyOfRange(nonOptions, 1, nonOptions.length));
    Path[] sources = new Path[files.size() + 1];
    if (files.isEmpty()) {
      System.out.println(pattern + " " + System.getProperty("user.dir"));
      sources[0] = pathFinder.getPath(System.getProperty("user.dir")).toAbsolutePath();
    } else {
      System.out.println(pattern + " " + String.join(", ", files.toString()));
      for (Integer i = 0; i < files.size(); i++) {
        sources[i] = pathFinder.getPath(files.get(i).getAbsolutePath());
      }
    }

    FileWalker walker = new FileWalker();
    for (Path p : sources) {
      try {
        logger.debug(String.format("DEBUG: walking %s", p));
        Files.walkFileTree(p, walker);
      } catch (IOException e) {
        // should not get here as we already validated the files
      }
    }

    System.exit(0);
  }

  private static String fileNotFound(String name) {
    return String.format(FILE_NOT_FOUND, name, name);
  }

  private static List<File> collectFiles(String[] args) {
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
