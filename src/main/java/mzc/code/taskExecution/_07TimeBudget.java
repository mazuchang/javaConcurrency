package mzc.code.taskExecution;

import java.util.*;
import java.util.concurrent.*;

/**
 * page:109
 * 本例中演示了利用 Executor.invokeAll() 一次提交多个 Future 任务，当这些任务执行完毕后，或调用线程被中断、超过时 invokeAll() 将返回
 * 此时客户端代码可以通过调用 get() 或 isCancelled() 来判断究竟是哪种情况，
 * 该方法非常适合一些需要同时进行多个任务，且这些任务间没有关联关系，即使有一些任务失败也不影响最终返回的场景，在这种情况下，不宜让程序
 * 响应时间受限于最慢的响应时间，而应该只返回在指定时间内正确响应的数据，对于没有及时响应或失败的服务，我们可以忽略它，或抛出异常提示客户端。
 */
public class _07TimeBudget {
    private static ExecutorService exec = Executors.newCachedThreadPool();

    public List<TravelQuote> getRankedTravelQuotes(TravelInfo travelInfo,
                                                   Set<TravelCompany> companies,
                                                   Comparator<TravelQuote> ranking,
                                                   long time, TimeUnit unit) throws InterruptedException {
        List<QuoteTask> tasks = new ArrayList<>();
        for (TravelCompany company : companies)
            tasks.add(new QuoteTask(company, travelInfo));

        List<Future<TravelQuote>> futures = exec.invokeAll(tasks, time, unit);

        List<TravelQuote> travelQuotes = new ArrayList<>(tasks.size());
        Iterator<QuoteTask> iterator = tasks.iterator();
        for (Future<TravelQuote> future : futures) {
            QuoteTask task = iterator.next();
            try {
                travelQuotes.add(future.get());
            } catch (ExecutionException e) {
                travelQuotes.add(task.getFailureQuote(e.getCause()));
            } catch (CancellationException e) {
                travelQuotes.add(task.getTimeoutQuote(e));
            }
        }
        Collections.sort(travelQuotes, ranking);
        return travelQuotes;
    }


}

class QuoteTask implements Callable<TravelQuote> {
    private final TravelCompany company;
    private final TravelInfo travelInfo;

    public QuoteTask(TravelCompany company, TravelInfo travelInfo) {
        this.company = company;
        this.travelInfo = travelInfo;
    }

    TravelQuote getFailureQuote(Throwable t) {
        return null;
    }

    TravelQuote getTimeoutQuote(CancellationException e) {
        return null;
    }

    public TravelQuote call() throws Exception {
        return company.solicitQuote(travelInfo);
    }
}

interface TravelCompany {
    TravelQuote solicitQuote(TravelInfo travelInfo) throws Exception;
}

interface TravelQuote {
}

interface TravelInfo {
}
