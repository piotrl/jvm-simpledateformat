package net.piotrl.jvm;

import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.function.Supplier;

public class App {
    public static void main(String[] args) throws Exception {
        SimpleDateFormatBreaker formatBreaker = new SimpleDateFormatBreaker();

        System.out.println("\nSimpleDateFormat without thrad safe checks");
        testDateFormatterOnMultipleThreads(() ->
                formatBreaker.threadUnsafe("20170323", "yyyyMMdd")
        );

        System.out.println("\nThreadSafe formatter");
        testDateFormatterOnMultipleThreads(() ->
                formatBreaker.threadSafe("20170323", "yyyyMMdd")
        );
    }

    private static void testDateFormatterOnMultipleThreads(Supplier<List<Date>> dateProducer) throws Exception {
        try {
            List<Date> formattedDates = dateProducer.get();
            boolean allDatesTheSame = formattedDates.stream()
                    .allMatch(date -> Objects.equals(date.toString(), "Thu Mar 23 00:00:00 CET 2017"));

            if (!allDatesTheSame) {
                System.out.println("[ERROR] Dates are different!!");
            } else {
                System.out.println("[OK] All dates are the same");
            }
        } catch (Exception e) {
            System.out.println("[ERROR] SimpleDateFormat thrown exception: " + e.getLocalizedMessage());
        }
    }
}
