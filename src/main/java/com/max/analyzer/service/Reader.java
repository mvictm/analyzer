package com.max.analyzer.service;

import lombok.SneakyThrows;

import java.nio.file.Files;
import java.nio.file.Paths;

public class Reader {
    private final String pathToFile;

    public Reader(String pathToFile) {
        this.pathToFile = pathToFile;
    }

    @SneakyThrows
    public void readFiles() {
        Storage.addToStorage(Files.lines(Paths.get(pathToFile)).parallel());
    }
}
