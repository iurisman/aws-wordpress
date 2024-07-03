package urisman.wp.monitor;

import java.util.function.Consumer;
import java.util.function.Function;

public record Right<L,R>(R value) implements Either<L,R> {
  @Override
  public <T> T ifLeftOrIfRight(Function<L, T> doLeft, Function<R, T> doRight) {
    return doRight.apply(value);
  }
  @Override
  public void ifLeftOrIfRight(Consumer<L> doLeft, Consumer<R> doRight) {
    doRight.accept(value);
  }@Override
  public boolean isRight() { return true; }
}
