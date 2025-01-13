package mzc.code.guiApplication;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * page:160-163
 * 本例中给出了一个监听器示例，通过将长时间任务绑定到一个可视化组件等方式，使得长时间任务可以支持用户反馈及取消
 */
public class _03ListenerExamples {

    private static ExecutorService exec = Executors.newCachedThreadPool();

    private final JButton colorButton = new JButton("Change color");
    private final Random random = new Random();

    // 简单的事件监听器
    private void backgroundRandom() {
        colorButton.addActionListener(e -> colorButton.setBackground(new Color(random.nextInt())));
    }


    private final JButton computeButton = new JButton("Big computation");

    // 将长时间任务绑定到一个可视化组件
    private void longRunningTask() {
        computeButton.addActionListener(e -> exec.execute(() -> {
            /* Do big computation */
        }));
    }


    private final JButton button = new JButton("Do");
    private final JLabel label = new JLabel("idle");

    // 支持任务反馈的长时间任务
    private void longRunningTaskWithFeedback() {
        button.addActionListener(e -> {
            button.setEnabled(false);
            label.setText("busy");
            exec.execute(() -> {
                try {
                    /* Do big computation */
                } finally {
                    _02GuiExecutor.instance().execute(() -> {
                        button.setEnabled(true);
                        label.setText("idle");
                    });
                }
            });
        });
    }


    private final JButton startButton = new JButton("Start");
    private final JButton cancelButton = new JButton("Cancel");
    private Future<?> runningTask = null; // thread-confined

    // 取消一个长时间任务
    private void taskWithCancellation() {
        startButton.addActionListener(e -> {
            if (runningTask != null) {
                runningTask = exec.submit(new Runnable() {
                    public void run() {
                        while (moreWork()) {
                            if (Thread.currentThread().isInterrupted()) {
                                cleanUpPartialWork();
                                break;
                            }
                            doSomeWork();
                        }
                    }

                    private boolean moreWork() {
                        return false;
                    }

                    private void cleanUpPartialWork() {
                    }

                    private void doSomeWork() {
                    }

                });
            }
            ;
        });

        cancelButton.addActionListener(event -> {
            if (runningTask != null)
                runningTask.cancel(true);
        });
    }

    // 通过 BackgroundTask 来执行长时间的并且可取消的任务
    private void runInBackground(final Runnable task) {
        startButton.addActionListener(e -> {
            class CancelListener implements ActionListener {
                _04BackgroundTask<?> task;

                public void actionPerformed(ActionEvent event) {
                    if (task != null)
                        task.cancel(true);
                }
            }
            final CancelListener listener = new CancelListener();
            listener.task = new _04BackgroundTask<Void>() {
                public Void compute() {
                    while (moreWork() && !isCancelled())
                        doSomeWork();
                    return null;
                }

                private boolean moreWork() {
                    return false;
                }

                private void doSomeWork() {
                }

                public void onCompletion(boolean cancelled, String s, Throwable exception) {
                    cancelButton.removeActionListener(listener);
                    label.setText("done");
                }
            };
            cancelButton.addActionListener(listener);
            exec.execute(task);
        });
    }
}
