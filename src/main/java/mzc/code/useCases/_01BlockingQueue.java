package mzc.code.useCases;

import java.io.File;
import java.io.FileFilter;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * page:75/76
 * 本例中利用 BlockingQueue 实现了一个桌面搜索应用程序的生产者消费者任务
 */
public class _01BlockingQueue {

    private static final int BOUND = 10;
    private static final int N_CONSUMERS = Runtime.getRuntime().availableProcessors();

    /**
     * 启动程序
     */
    public static void startIndexing(File[] roots) {
        BlockingQueue<File> queue = new LinkedBlockingQueue<>(BOUND);
        FileFilter filter = pathname -> true;

        for (File root : roots)
            new Thread(new FileCrawler(queue, filter, root)).start();

        for (int i = 0; i < N_CONSUMERS; i++)
            new Thread(new Indexer(queue)).start();
    }

    /**
     * 该类用于在某个文件层次结构中搜索复合索引表中的文件，并将它们的名称放入工作队列
     */
    static class FileCrawler implements Runnable {

        private final BlockingQueue<File> fileQueue;
        private final FileFilter fileFilter;
        private final File root;

        public FileCrawler(BlockingQueue<File> fileQueue, final FileFilter fileFilter, File root) {
            this.fileQueue = fileQueue;
            this.root = root;
            // 递归过滤文件夹
            this.fileFilter = pathname -> pathname.isDirectory() || fileFilter.accept(pathname);
        }

        private boolean alreadyIndexed(File f) {
            return false;
        }

        @Override
        public void run() {
            try {
                crawl(root);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }

        private void crawl(File root) throws InterruptedException {
            // 获取 fileFilter 下所有的文件
            File[] entries = root.listFiles(fileFilter);
            if(entries != null) {
                for (File entry : entries) {
                    if(entry.isDirectory())
                        // 是文件夹继续递归调用
                        crawl(entry);
                    else if(!alreadyIndexed(entry))
                        // 没有被编制过索引则加入队列待编制
                        fileQueue.put(entry);
                }
            }
        }
    }

    /**
     * 该类负责从队列中取出文件名称，并为其编制索引
     */
    static class Indexer implements Runnable{
        private final BlockingQueue<File> queue;

        public Indexer(BlockingQueue<File> queue) {
            this.queue = queue;
        }

        @Override
        public void run() {
            try {
                while (true)
                    indexFile(queue.take());
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }

        public void indexFile(File file) {
            // 为文件编制索引
        }
    }

}
