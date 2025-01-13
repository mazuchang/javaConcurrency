package mzc.code.taskExecution;

import java.util.concurrent.*;

/**
 * page:108
 * 本例中演示了如何通过 Future 来为任务设置时限，例如本例中利用 Future.get() 的超时机制实现了一个加载页面广告的功能
 * 当页面在指定时间内未加载完广告（报错或超时），就为页面设置一个默认的广告
 */
public class _06RenderWithTimeBudget {

    private static final Ad DEFAULT_AD = new Ad();
    private static final long TIME_BUDGET = 1000;
    private static final ExecutorService exec = Executors.newCachedThreadPool();

    Page renderPageWithAd() {
        long endNanos = System.nanoTime() + TIME_BUDGET;
        Future<Ad> future = exec.submit(new FetchAdTask());
        // 在等待广告的同时显示页面
        Page page = renderPageBody();
        Ad ad = null;
        try {
            // 只等待指定毫秒
            long timeLeft = System.nanoTime();
            ad = future.get(timeLeft, TimeUnit.NANOSECONDS);
        } catch (ExecutionException e) {
            ad = DEFAULT_AD;
        } catch (TimeoutException e) {
            ad = DEFAULT_AD;
            future.cancel(true);
        } catch (InterruptedException e) {
            ad = DEFAULT_AD;
        }
        page.setAd(ad);
        return page;
    }

    Page renderPageBody() {
        return new Page();
    }

    static class Ad {
    }

    static class Page {
        public void setAd(Ad ad) {
        }
    }

    static class FetchAdTask implements Callable<Ad> {
        public Ad call() {
            return new Ad();
        }
    }

}
