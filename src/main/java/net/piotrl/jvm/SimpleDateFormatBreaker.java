package net.piotrl.jvm;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.*;

// With much help of SO thread: http://stackoverflow.com/questions/4021151/java-dateformat-is-not-threadsafe-what-does-this-leads-to
public class SimpleDateFormatBreaker {
    public List<Date> threadUnsafe(String source, String pattern) {
        final DateFormat format = new SimpleDateFormat(pattern);
        try {
            return getDates(() -> format.parse(source));
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException(e);
        }
    }

    public List<Date> threadSafe(String source, String pattern) {
        final ThreadLocal<DateFormat> threadDateFormat = ThreadLocal.withInitial(() -> new SimpleDateFormat(pattern));
        try {
            return getDates(() -> threadDateFormat.get().parse(source));
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException(e);
        }
    }

    private List<Date> getDates(Callable<Date> task) throws InterruptedException, java.util.concurrent.ExecutionException {
        //pool with 5 threads
        ExecutorService exec = Executors.newFixedThreadPool(10);
        List<Future<Date>> results = new ArrayList<>();

        //perform 10 date conversions
        for (int i = 0; i < 100; i++) {
            results.add(exec.submit(task));
        }
        exec.shutdown();

        // gather result
        List<Date> list = new ArrayList<>();
        for (Future<Date> result : results) {
            list.add(result.get());
        }
        return list;
    }
}
