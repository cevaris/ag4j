package com.cevaris.arg4j.ignores;

import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.util.Collections;

import com.cevaris.ag4j.ignores.IgnoreRepoImpl;

import org.junit.Assert;
import org.junit.Test;

public class IgnoreRepoImplSpec {

  private Path path(String... paths) {
    return FileSystems.getDefault().getPath("", paths);
  }

  @Test
  public void testThis() {
    IgnoreRepoImpl ignoreRepo = new IgnoreRepoImpl();
    Path testPath = path("/test");
    ignoreRepo.add(testPath, Collections.singletonList("ignore"));
    Assert.assertEquals(ignoreRepo.getPatterns(testPath), Collections.singleton("ignore"));
  }
}
