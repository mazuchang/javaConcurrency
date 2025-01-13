package mzc.code.useCases.memorizer.rely;

@FunctionalInterface
public interface Computable<A, V> {
    V compute(A arg) throws InterruptedException;
}
