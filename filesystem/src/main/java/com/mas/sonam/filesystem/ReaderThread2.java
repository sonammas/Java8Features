package com.mas.sonam.filesystem;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;

public class ReaderThread2 implements Runnable {

    private final InMemoryCache inMemoryCache;

    public ReaderThread2(InMemoryCache inMemoryCache) {
        this.inMemoryCache = inMemoryCache;
    }


    @Override
    public void run() {
        BufferedReader br = null;
        try {
            br = new BufferedReader(new FileReader(new File("C:\\test\\read2.txt")));
            String buffer;
            while ((buffer = br.readLine()) != null) {
                String[] words = buffer.split(" ");
                Arrays.stream(words).forEach(word -> {
                    inMemoryCache.add("thread2", word);
                });
            }
        } catch (IOException e) {

            e.printStackTrace();
        }  finally {
            try {
                br.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}