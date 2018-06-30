package com.cevaris.ag4j;

import java.io.File;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Main {
  public static final Logger logger = LoggerFactory.getLogger(Main.class);

  private final static String WAT = "ERR: What do you want to search for?";
  private final static String FILE_NOT_FOUND =
      "ERR: Error stat()ing: %s\n" +
          "ERR: Error opening directory %s: No such file or directory";


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

    List<File> files = collectFiles(Arrays.copyOfRange(nonOptions, 1, nonOptions.length));
    if (files.isEmpty()) {
      System.out.println(pattern + " " + System.getProperty("user.dir"));
    } else {
      System.out.println(pattern + " " + String.join(", ", files.toString()));
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
        System.out.println(fileNotFound(arg));
      }
    }
    return files;
  }
}
