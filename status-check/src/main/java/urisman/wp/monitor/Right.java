package com.variant.share.util;

import java.util.function.Function;

public record Right<L,R>(R value) implements Either<L,R> {
  public <T> T ifLeftOrIfRight(Function<L, T> doLeft, Function<R, T> doRight) {
    return doRight.apply(value);
  }
  public boolean isRight() { return true; }

}
