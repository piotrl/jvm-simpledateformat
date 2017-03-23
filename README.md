## JVM SimpleDateFormat Kata

#### Challange
- Make SimpleDateFormat produce wrong results on multiple threads
- Create Thread safe SimpleDateFormat

#### HowTo run

`mvn exec:java`

#### Results
```
SimpleDateFormat without thrad safe checks
[ERROR] SimpleDateFormat thrown exception: java.util.concurrent.ExecutionException: java.lang.NumberFormatException: multiple points

ThreadSafe formatter
[OK] All dates are the same
```

#### Solution

- Creating ThreadLocal instance with SimpleDateFormat
```
ThreadLocal<DateFormat> threadDateFormat = ThreadLocal.withInitial(() -> new SimpleDateFormat(pattern));
```