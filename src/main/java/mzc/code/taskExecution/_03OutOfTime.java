package mzc.code.taskExecution;

import java.util.Timer;
import java.util.TimerTask;

import static java.util.concurrent.TimeUnit.SECONDS;

/**
 * page:102
 * 本例中演示了一个使用 Timer 进行定时调度的一个弊端，由于 Timer 在执行所有定时任务时只会创建一个线程，如果某个任务执行时间过长，
 * 将会破坏其他 TimerTask 的定时精准性
 * 例如：
 *      某个周期 TimerTask 需要 10ms 执行依次，而另一个 TimerTask 需要执行 40ms，那么这个周期任务或者在 40ms 任务执行完成后快速
 *      连续地调用 4 次，或者彻底 “丢失” 4次调用。（具体采用哪种方式处理取决于 Timer 是基于固定速率来调度还是基于固定延时来调度）
 *      线程池可以弥补这个缺陷，它可以提供多个线程来执行延时任务和周期任务
 *
 *      Timer 的另一个问题是，如果 Timer 抛出了一个未检查的异常，Timer 不会捕获异常，而是会直接终止定时任务，在终止情况下 Timer
 *      不会恢复线程的执行，而是错误的认为整个 Timer 都被取消了，因此，已经被调度但尚未执行的 TimerTask 将不会再执行，新的任务也不能被调度
 *      这个问题被称为 “线程泄露”
 */
public class _03OutOfTime {

    public static void main(String[] args) throws InterruptedException {
        Timer timer = new Timer();
        timer.schedule(new ThrowTask(), 1);
        SECONDS.sleep(1);
        timer.schedule(new ThrowTask(), 1);
        SECONDS.sleep(5);
    }

    static class ThrowTask extends TimerTask {
        @Override
        public void run() {
            throw new RuntimeException();
        }
    }
}
