package mzc.code.useExecutor;

import mzc.code.useExecutor.rely.Node;
import mzc.code.useExecutor.rely.Puzzle;

import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * page:155
 * 在 _07ConcurrentPuzzleSolver 中第一个找到解答的线程会关闭 Executor 从而阻止接受新的任务，此时为了避免处理拒绝策略，
 * 还需要将拒绝执行处理器设置为“抛弃已提交的任务”。然后等待所有未完成的任务最终执行完成，并且在执行任何新任务时都会失败，从而使 Executor 结束。
 * （如果任务运行的时间过长，可以中断它们而不是等它们完成）
 * 然而如果不存在解答，则 _07ConcurrentPuzzleSolver 不能很好的处理这种情况，本例中通过记录活动任务的数量，当该值为零时将解答结果设置为 null
 * 从而使得不存在解答时程序得以正常结束
 */
public class _09PuzzleSolver<P, M> {

    final _08ValueLatch<Node<P, M>> solution = new _08ValueLatch<>();
    private final AtomicInteger taskCount = new AtomicInteger(0);
    private final Puzzle<P, M> puzzle;
    private final ExecutorService exec;
    private final ConcurrentMap<P, Boolean> seen;

    public _09PuzzleSolver(Puzzle<P, M> puzzle) {
        this.puzzle = puzzle;
        this.exec = initThreadPool();
        this.seen = new ConcurrentHashMap<P, Boolean>();
        if (exec instanceof ThreadPoolExecutor) {
            ThreadPoolExecutor tpe = (ThreadPoolExecutor) exec;
            tpe.setRejectedExecutionHandler(new ThreadPoolExecutor.DiscardPolicy());
        }
    }

    class CountingSolveTask extends SolverTask {
        CountingSolveTask(P pos, M move, Node<P, M> prev) {
            super(pos, move, prev);
            taskCount.incrementAndGet();
        }

        public void run() {
            try {
                super.run();
            } finally {
                if (taskCount.decrementAndGet() == 0)
                    solution.setValue(null);

            }
        }
    }

    private ExecutorService initThreadPool() {
        return Executors.newCachedThreadPool();
    }

    protected Runnable newTask(P p, M m, Node<P, M> n) {
        return new SolverTask(p, m, n);
    }

    protected class SolverTask extends Node<P, M> implements Runnable {
        SolverTask(P pos, M move, Node<P, M> prev) {
            super(pos, move, prev);
        }

        public void run() {
            if (solution.isSet()
                    || seen.putIfAbsent(pos, true) != null)
                return;
            if (puzzle.isGoal(pos))
                solution.setValue(this);
            else
                for (M m : puzzle.legalMoves(pos))
                    exec.execute(newTask(puzzle.move(pos, m), m, this));
        }
    }
}
