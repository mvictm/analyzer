package com.max.analyzer.service;

import com.max.analyzer.util.Validator;
import lombok.extern.java.Log;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Log
public class Executor {
    private static List<String> fileNamesForWrite;
    private static List<String> fileNamesForRead;

    public static void execute(String[] args) {
        Validator.checkParams(args);
        initializeParams(args);
        fileNamesForRead.parallelStream().forEach(file -> new Reader(file).readFiles());
        startWrite();
    }

    private static void initializeParams(String[] args) {
        log.info(">> Initialize parameter");
        fileNamesForWrite = Stream.of(args)
                .limit(2)
                .parallel()
                .collect(Collectors.toList());
        fileNamesForRead = Stream.of(args)
                .skip(2)
                .filter(Objects::nonNull)
                .parallel()
                .collect(Collectors.toList());
    }

    private static void startWrite() {
        log.info(">> Write statistics to files");
        Analyzer analyzer = new Analyzer();
        analyzer.writeSumAmountPerDay(fileNamesForWrite.get(0));
        analyzer.writeSumEachOffice(fileNamesForWrite.get(1));
        log.info("<< End write");
    }
}
