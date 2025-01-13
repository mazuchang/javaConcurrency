package mzc.code.taskExecution;

import mzc.code.useCases._04FutureTask;

import java.util.List;
import java.util.concurrent.*;

/**
 * page:107
 * 在 _04FutureRenderer 中我们利用 Future 提前对图像进行加载，如此一来可以提高程序的效率，
 * 本例中我们将使用 CompletionServer 继续优化，使得页面元素在下载完成后能立即显示出来
 *
 * 接口 CompletionService 的功能是以异步的方式一边生产新的任务，一边处理已完成任务的结果，这样可以将执行任务与处理任务分离开来进行处理
 * 其仅有一个实现类 ExecutorCompletionService，类 ExecutorCompletionService 需要依赖于 Executor 对象，
 * 大部分的实现也就是实现线程池 ThreadPoolExecutor 对象
 * 使用 CompletionService 的好处是无需像 Future 那样阻塞的获取最终结果进行处理。
 */
public abstract class _05CompletionServerRenderer {

    private final ExecutorService executorService;

    public _05CompletionServerRenderer(ExecutorService executorService) {
        this.executorService = executorService;
    }

    void renderPage(CharSequence source) {
        List<ImageInfo> imageInfos = scanForImageInfo(source);
        CompletionService<ImageData> completionService = new ExecutorCompletionService<>(executorService);
        for (ImageInfo imageInfo : imageInfos)
            completionService.submit(() -> imageInfo.downloadImage());
        renderText(source);
        try {
            for (int i = 0, size = imageInfos.size(); i < size; i++) {
                Future<ImageData> take = completionService.take();
                ImageData imageData = take.get();
                renderImage(imageData);
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
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
