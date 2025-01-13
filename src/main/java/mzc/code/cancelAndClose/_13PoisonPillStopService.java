package mzc.code.cancelAndClose;

import java.io.File;
import java.io.FileFilter;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * page:128/129
 * 本例演示了通过 “毒丸” 对象关闭服务的一种方式
 */
public class _13PoisonPillStopService {

    private static final int CAPACITY = 1000;
    private static final File POISON = new File("");
    private final IndexerThread consumer = new IndexerThread();
    private final CrawlerThread producer = new CrawlerThread();
    private final BlockingQueue<File> queue;
    private final FileFilter fileFilter;
    private final File root;

    public _13PoisonPillStopService(File root, final FileFilter fileFilter) {
        this.root = root;
        this.queue = new LinkedBlockingQueue<>(CAPACITY);
        this.fileFilter = new FileFilter() {
            public boolean accept(File f) {
                return f.isDirectory() || fileFilter.accept(f);
            }
        };
    }

    public void start() {
        consumer.start();
        producer.start();
    }

    public void stop() {
        producer.interrupt();
    }

    private boolean alreadyIndexed(File f) {
        return false;
    }

    public void awaitTermination() throws InterruptedException {
        consumer.join();
    }

    class CrawlerThread extends Thread {
        @Override
        public void run() {
            try {
                crawl(root);
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                while (true) {
                    try {
                        queue.put(POISON);
                    } catch (InterruptedException e) {
                        // 重新尝试
                        e.printStackTrace();
                    }
                }
            }
        }

        private void crawl(File root) throws InterruptedException {
            File[] entries = root.listFiles(fileFilter);
            if (entries != null) {
                for (File entry : entries) {
                    if (entry.isDirectory())
                        crawl(entry);
                    else if (!alreadyIndexed(entry))
                        queue.put(entry);
                }
            }
        }
    }

    class IndexerThread extends Thread {
        @Override
        public void run() {
            try {
                while (true) {
                    File file = queue.take();
                    if(file == POISON)
                        break;
                    else
                        indexFile(file);
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        public void indexFile(File file) {
            /*...*/
        };
    }
}

