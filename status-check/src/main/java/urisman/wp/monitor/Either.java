package urisman.wp.monitor;

import java.util.function.Function;
import java.util.function.Consumer;

sealed public interface Either<L,R> permits Left, Right {
  <T> T ifLeftOrIfRight(Function<L,T> doLeft, Function<R,T> doRight);
  void ifLeftOrIfRight(Consumer<L> doLeft, Consumer<R> doRight);
  boolean isRight();
  default boolean isLeft() { return ! isRight(); }

}
