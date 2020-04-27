package com.max.analyzer.util;

import lombok.SneakyThrows;
import lombok.experimental.UtilityClass;
import lombok.extern.java.Log;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Objects;
import java.util.stream.Stream;

@Log
@UtilityClass
public class Validator {
    public void checkParams(String[] args) {
        log.info(">> Validation params");
        if (args.length < 3) {
            throw new RuntimeException("Not enough parameters.");
        }

        Stream.of(args)
                .skip(2)
                .filter(Objects::nonNull)
                .forEach(Validator::validFile);

        log.info("<< All params are valid");
    }

    @SneakyThrows
    private void validFile(String path) {
        if (!Files.exists(Paths.get(path)) || Files.size(Paths.get(path)) == 0) {
            throw new RuntimeException("File " + path + " is not exist or empty");
        }
    }
}