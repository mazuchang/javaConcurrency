package mzc.code.useExecutor;

import mzc.code.useExecutor.rely.Node;
import mzc.code.useExecutor.rely.Puzzle;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * page:152
 * 本例演示了谜题框架的串行解决方案，它在谜题空间中执行一个深度优先搜索，当找到解答方案后结束搜索
 */
public class _06SequentialPuzzleSolver<P, M> {

    private final Puzzle<P, M> puzzle;
    private final Set<P> seen = new HashSet<>();

    public _06SequentialPuzzleSolver(Puzzle<P, M> puzzle) {
        this.puzzle = puzzle;
    }

    public List<M> solve() {
        P pos = puzzle.initialPosition();
        return search(new Node<>(pos, null, null));
    }

    private List<M> search(Node<P, M> node) {
        if (!seen.contains(node.pos)) {
            seen.add(node.pos);
            if (puzzle.isGoal(node.pos)) return node.asMoveList();
            for (M move : puzzle.legalMoves(node.pos)) {
                P pos = puzzle.move(node.pos, move);
                Node<P, M> child = new Node<>(pos, move, node);
                List<M> result = search(child);
                if (result != null) return result;
            }
        }
        return null;
    }
}
