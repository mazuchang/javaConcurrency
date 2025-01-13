package mzc.code.cancelAndClose;

import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;

/**
 * page:122
 * 本例中演示了通过改写 interrupt 方法将非标准的取消操作封装在 Thread 中，由此可以使得那些由于执行不可中断操作而被阻塞的线程，
 * 可以使用类似于中断的手段来停止这些线程，但这要求我们必须知道线程阻塞的原因
 */
public class _08ReaderThread extends Thread {

    private static final int BUFSZ = 512;
    private final Socket socket;
    private final InputStream in;

    public _08ReaderThread(Socket socket) throws IOException {
        this.socket = socket;
        in = socket.getInputStream();
    }

    @Override
    public void interrupt() {
        try {
            socket.close();
        } catch (IOException e) {
        } finally {
            super.interrupt();
        }
    }

    @Override
    public void run() {
        try {
            byte[] bytes = new byte[BUFSZ];
            while (true) {
                int count = in.read(bytes);
                if (count < 0)
                    break;
                else if (count > 0)
                    processBuffer(bytes, count);
            }
        } catch (IOException e) {
            // 允许线程退出
        }
    }

    public void processBuffer(byte[] buf, int count) {
    }
}
