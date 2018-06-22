package com.cevaris.ag4j;

import java.io.File;

import joptsimple.OptionParser;
import joptsimple.OptionSet;

public class Main {
  private final static String WAT = "ERR: What do you want to search for?";

  public static void main(String[] args) {
    OptionParser parser = new OptionParser("G");
    OptionSet opts = parser.parse(args);

    if (args.length == 0) {
      System.out.println(WAT);
      System.exit(1);
    }

    if (args.length > 1 && !(new File(args[1]).exists())) {
      System.out.println(opts.nonOptionArguments().get(0) + " " + System.getProperty("user.dir"));
      System.exit(0);
    } else {
      System.out.println(opts.nonOptionArguments().get(0) + " " + new File(args[1]).getAbsolutePath());
      System.exit(0);
    }

    System.out.println(opts.nonOptionArguments());
    System.out.println(String.join(",", args));
  }
}
