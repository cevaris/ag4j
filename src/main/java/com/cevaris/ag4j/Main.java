package com.cevaris.ag4j;

import java.io.File;
import java.util.LinkedList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import joptsimple.OptionParser;
import joptsimple.OptionSet;

public class Main {
  public static final Logger logger = LoggerFactory.getLogger(Main.class);

  private final static String WAT = "ERR: What do you want to search for?";

  public static void main(String[] args) {
    OptionParser parser = new OptionParser("GD");
    OptionSet opts = parser.parse(args);

    if (args.length == 0) {
      System.out.println(WAT);
      System.exit(1);
    }

    if (opts.has("D")) {
      System.out.println(opts.nonOptionArguments());
      System.out.println(String.join(",", args));
    }

    List<File> files = collectFiles(args);
    if (files.isEmpty()) {
      System.out.println(opts.nonOptionArguments().get(0) + " " + System.getProperty("user.dir"));
      System.exit(0);
    }

    System.out.println(opts.nonOptionArguments().get(0) + " " + String.join(", ", files.toString()));
    System.exit(0);
  }

  private static List<File> collectFiles(String[] args) {
    List<File> files = new LinkedList<>();
    for (String arg : args) {
      File file = new File(arg);
      if (file.exists()) {
        files.add(file);
      }
    }
    return files;
  }
}
