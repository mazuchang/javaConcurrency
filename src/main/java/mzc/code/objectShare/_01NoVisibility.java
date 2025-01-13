package mzc.code.objectShare;

/**
 * page:27
 * 本例实现了一个不可见性的场景，当运行主程序时，可能会输出 0 也可能一直阻塞下去
 */
public class _01NoVisibility {

    private static boolean ready;
    private static int number;

    private static class ReaderThread extends Thread {
        public void run () {
            while(!ready)
                Thread.yield();
            System.out.println(number);
        }
    }

    public static void main(String[] args) {
        new ReaderThread().start();
        number = 42;
        ready = true;
    }

}
