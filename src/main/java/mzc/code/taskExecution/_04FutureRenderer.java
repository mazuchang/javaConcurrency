package mzc.code.taskExecution;

import mzc.code.useCases._04FutureTask;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

/**
 * page:105
 * 为解决程序清单 6-10 中串行渲染页面元素带来的性能问题，本例演示了使用 Future 实现的页面渲染器，
 * 通过 Future 将渲染过程分解为两个任务来执行，可以实现更高的并发性
 */
public abstract class _04FutureRenderer {

    private final ExecutorService executor = Executors.newCachedThreadPool();

    void renderPage(CharSequence source) {
        final List<ImageInfo> imageInfos = scanForImageInfo(source);
        Callable<List<ImageData>> task = () -> {
            ArrayList<ImageData> result = new ArrayList<>();
            for (ImageInfo imageInfo : imageInfos) {
                result.add(imageInfo.downloadImage());
            }
            return result;
        };
        Future<List<ImageData>> future = executor.submit(task);
        renderText(source);

        try {
            List<ImageData> imageData = future.get();
            for (ImageData imageDatum : imageData) {
                renderImage(imageDatum);
            }
        }catch (InterruptedException e) {
            // 重新设置线程的中断状态
            Thread.currentThread().interrupt();
            // 由于不需要结果，因此取消任务
            future.cancel(true);
        } catch (ExecutionException e) {
            throw _04FutureTask.launderThrowable(e);
        }
    }

    interface ImageData {
    }

    interface ImageInfo {
        ImageData downloadImage();
    }

    abstract void renderText(CharSequence s);

    abstract List<ImageInfo> scanForImageInfo(CharSequence s);

    abstract void renderImage(ImageData i);
}


