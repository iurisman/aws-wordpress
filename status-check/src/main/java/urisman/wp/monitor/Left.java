package com.variant.share.util;

import java.util.function.Function;

public record Left<L,R>(L value) implements Either<L,R> {
  @Override
  public <T> T ifLeftOrIfRight(Function<L, T> doLeft, Function<R, T> doRight) {
    return doLeft.apply(value);
  }
  @Override
  public boolean isRight() { return false; }
}