package com.max.analyzer.service;

import lombok.Getter;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

public class Storage {
    @Getter
    private static final List<Stream<String>> storageList = new ArrayList<>();

    public static void addToStorage(Stream<String> dataStream) {
        storageList.add(dataStream);
    }
}
