package mzc.code.useExecutor;

import mzc.code.useExecutor.rely.Node;
import mzc.code.useExecutor.rely.Puzzle;

import java.util.List;
import java.util.concurrent.*;

/**
 * page:153
 * 本例演示了谜题框架的并行解决方案，它在谜题空间中执行一个深度优先搜索，当找到解答方案后结束搜索
 */
public class _07ConcurrentPuzzleSolver<P, M> {

    private final Puzzle<P, M> puzzle;
    private final ExecutorService exec;
    private final ConcurrentMap<P, Boolean> seen;
    final _08ValueLatch<Node<P, M>> solution = new _08ValueLatch<>();

    public _07ConcurrentPuzzleSolver(Puzzle<P, M> puzzle) {
        this.puzzle = puzzle;
        this.exec = initThreadPool();
        this.seen = new ConcurrentHashMap<P, Boolean>();
        if (exec instanceof ThreadPoolExecutor) {
            ThreadPoolExecutor tpe = (ThreadPoolExecutor) exec;
            tpe.setRejectedExecutionHandler(new ThreadPoolExecutor.DiscardPolicy());
        }
    }

    public List<M> solve() throws InterruptedException {
        try {
            P p = puzzle.initialPosition();
            exec.execute(newTask(p, null, null));
            // 阻塞直到找到解答
            Node<P, M> value = solution.getValue();
            return (value == null) ? null : value.asMoveList();
        } finally {
            exec.shutdown();
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
