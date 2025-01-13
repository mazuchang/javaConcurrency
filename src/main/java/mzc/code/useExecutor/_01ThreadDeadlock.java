package mzc.code.useExecutor;

import java.util.concurrent.*;

/**
 * page:139
 * 我们知道通过 Executor 框架可以将任务的提交与任务的执行策略解耦开来，这种方式为指定和修改执行策略提供了相当大的灵活性，
 * 然而并非所有任务都能适用所有的执行策略，有些类型的任务需要明确地指定执行策略，包括：
 * <p>
 * 1. 依赖性任务
 * 如果提交给线程池的任务需要依赖其他的任务，此时除非线程池无限大，否则都有可能造成死锁，这种现象也被称为线程饥饿死锁
 * （本例就演示了这样一种线程饥饿死锁现象）
 * <p>
 * 2. 使用线程封闭机制的任务
 * 对于一些使用单线程的 Executor（newSingleThreadExecutor）的任务来说，利用单线程 Executor 自身提供的并发性保证，可以
 * 使得该线程执行的任务在访问该对象时不需要同步，即使这些资源不是线程安全的。然而如果将 Executor 从单线程环境改为线程池环境
 * 将会失去线程安全性
 * <p>
 * 3. 对响应时间敏感的任务
 * 如果一个运行时间较长的热为奴提交到单线程的 Executor 中，或者多个运行时间较长的任务提交到一个只包含少量线程的线程池中，
 * 将降低该 Executor 管理的服务的响应性
 * <p>
 * 4. 使用 ThreadLocal 的任务
 * 在标准的 Executor 实现中，当执行需求较低时将回收空闲线程，而当需求增加时将添加新的线程，并且如果从任务中抛出了一个未检查
 * 异常，那么将用新的工作者线程来替代抛出异常的线程，因此只有当线程本地值的生命周期受限于任务的生命周期时，在线程池中使用 ThreadLocal
 * 才有意义，而在线程池的线程中不应该使用 ThreadLocal 在任务间传递值
 */
public class _01ThreadDeadlock {

    ExecutorService exec = Executors.newSingleThreadExecutor();

    public class RenderPageTask implements Callable<String> {

        public String call() throws ExecutionException, InterruptedException {
            Future<String> header, footer;
            header = exec.submit(new LoadFileTask("header.html"));
            footer = exec.submit(new LoadFileTask("footer.html"));
            String page = renderBody();
            // 将发生死锁 —— 由于任务在等待子任务的结果
            return header.get() + page + footer.get();
        }

        private String renderBody() {
            // Here's where we would actually render the page
            return "";
        }
    }

    public class LoadFileTask implements Callable<String> {
        private final String fileName;

        public LoadFileTask(String fileName) {
            this.fileName = fileName;
        }

        public String call() throws Exception {
            // Here's where we would actually read the file
            return "";
        }
    }
}
