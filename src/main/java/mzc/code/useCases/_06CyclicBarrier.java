package mzc.code.useCases;

import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

/**
 * page:84
 * 本例演示了利用 CyclicBarrier 协调细胞自动衍生系统的计算
 */
public class _06CyclicBarrier {

    private final Board mainBoard;
    private final CyclicBarrier barrier;
    private final Worker[] workers;

    public _06CyclicBarrier(Board mainBoard) {
        this.mainBoard = mainBoard;

        int CPUCores = Runtime.getRuntime().availableProcessors();
        this.barrier = new CyclicBarrier(CPUCores, () -> {
            mainBoard.commitNewValues();
        });

        this.workers = new Worker[CPUCores];
        for (int i = 0; i < CPUCores; i++)
            workers[i] = new Worker(mainBoard.getSubBoard(CPUCores, i));
    }

    public void start() {
        for (int i = 0; i < workers.length; i++)
            new Thread(workers[i]).start();
        mainBoard.waitForConvergence();
    }

    private class Worker implements Runnable {
        private final Board board;

        public Worker(Board board) {
            this.board = board;
        }

        @Override
        public void run() {
            while (!board.hasConverged()) {
                for (int x = 0; x < board.getMaxX(); x++)
                    for (int y = 0; y < board.getMaxY(); y++)
                        board.setNewValue(x, x, computeValue(x, y));
                try {
                    barrier.await();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    return;
                } catch (BrokenBarrierException e) {
                    e.printStackTrace();
                    return;
                }
            }

        }

        private int computeValue(int x, int y) {
            // Compute the new value that goes in (x,y)
            return 0;
        }
    }
}

interface Board {
    int getMaxX();

    int getMaxY();

    int getValue(int x, int y);

    int setNewValue(int x, int y, int value);

    void commitNewValues();

    boolean hasConverged();

    void waitForConvergence();

    Board getSubBoard(int numPartitions, int index);
}