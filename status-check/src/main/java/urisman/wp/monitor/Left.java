package urisman.wp.monitor;

import java.util.function.Consumer;
import java.util.function.Function;

public record Left<L,R>(L value) implements Either<L,R> {
  @Override
  public <T> T ifLeftOrIfRight(Function<L, T> doLeft, Function<R, T> doRight) {
    return doLeft.apply(value);
  }
  @Override
  public void ifLeftOrIfRight(Consumer<L> doLeft, Consumer<R> doRight) {
    doLeft.accept(value);
  }
  @Override
  public boolean isRight() { return false; }
}