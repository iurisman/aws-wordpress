package com.variant.share.util;

import java.util.function.Function;

sealed public interface Either<L,R> permits Left, Right {
  <T> T ifLeftOrIfRight(Function<L,T> doLeft, Function<R,T> doRight);
  boolean isRight();
  default boolean isLeft() { return ! isRight(); }

}
