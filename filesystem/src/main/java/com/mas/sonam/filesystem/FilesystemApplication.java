package com.mas.sonam.filesystem;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.counting;
import static java.util.stream.Collectors.groupingBy;

@SpringBootApplication
public class FilesystemApplication {

    public static void main(String[] args) throws InterruptedException {
        SpringApplication.run(FilesystemApplication.class, args);
        FilesystemApplication filesystemApplication = new FilesystemApplication();
        filesystemApplication.threadOutput();
    }

    private void threadOutput() throws InterruptedException {

        InMemoryCache inMemoryCache = new InMemoryCache();
        ReaderThread readerThread = new ReaderThread(inMemoryCache);
        ReaderThread2 readerThread2 = new ReaderThread2(inMemoryCache);

        new Thread(readerThread).start();
        new Thread(readerThread2).start();
        List<ResultObject> resultObjects = InMemoryCache.cache;
        Thread.sleep(10000);
        List<ResultObject> resultObjectFromThread1 = getResultObject("thread1", resultObjects);
        List<ResultObject> resultObjectFromThread2 = getResultObject("thread2", resultObjects);

        Map<String, Long> collectfromThread1 = getCollectionFromThread(resultObjectFromThread1);
        Map<String, Long> collectfromThread2 = getCollectionFromThread(resultObjectFromThread2);

        Map<String, Long> total = new HashMap<>(collectfromThread2);
        collectfromThread1.forEach((k,v)-> total.merge(k, v , (v1, v2) -> v1+v2));

        total.forEach((k,v) -> {
            System.out.print(k+ "  " + v + " = " +collectfromThread1.get(k)+ " + " +
                    collectfromThread2.get(k));
            System.out.print("\n");
        });
    }

    private List<ResultObject> getResultObject(String threadName, List<ResultObject> resultObjects){
        return resultObjects.stream().filter(ro -> ro.getThread().equals(threadName)).collect(Collectors.toList());
    }

    private Map<String, Long> getCollectionFromThread(List<ResultObject> resultObjects){
        return resultObjects.stream().collect(groupingBy(ResultObject::getWord, counting()));
    }
}
