package com.cevaris.ag4j.common;

public class Tuple2<L, R> {
  private final L l;
  private final R r;

  private Tuple2(L l, R r) {
    this.l = l;
    this.r = r;
  }

  public static <L, R> Tuple2 tuple(L l, R r) {
    return new Tuple2(l, r);
  }


  public L l() {
    return l;
  }

  public R r() {
    return r;
  }
}
