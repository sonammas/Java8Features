package com.mas.sonam.filesystem;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;

public class ReaderThread implements Runnable {

    private final InMemoryCache inMemoryCache;

    public ReaderThread(InMemoryCache inMemoryCache) {
        this.inMemoryCache = inMemoryCache;
    }

    @Override
    public void run() {
        BufferedReader br = null;
        try {
            br = new BufferedReader(new FileReader(new File("C:\\test\\read1.txt")));
            String line;

            while ((line = br.readLine()) != null) {
                String[] words = line.split(" ");
                Arrays.stream(words).forEach(word -> {
                    inMemoryCache.add("thread1", word);
                });
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                br.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}