package threadpool;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import org.junit.Test;

public class FixSchedule {


    @Test
    public void testException() throws Exception {
        ScheduledExecutorService executorService = Executors.newScheduledThreadPool(1);
        final AtomicInteger a = new AtomicInteger(0);
        ScheduledFuture scheduledFuture = executorService.scheduleAtFixedRate(new Runnable() {
            @Override public void run() {
                System.out.println("Run " + a.getAndIncrement());
                if (a.get() > 0) {
                    throw new RuntimeException("xxxx");
                }
            }
        }, 1000, 1000, TimeUnit.MILLISECONDS);
        try {
            scheduledFuture.get();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
