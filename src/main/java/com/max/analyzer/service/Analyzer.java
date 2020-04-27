package com.max.analyzer.service;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import lombok.extern.java.Log;

import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Log
public class Analyzer {
    private final Multimap<String, BigDecimal> dateStat = ArrayListMultimap.create();
    private final Multimap<String, BigDecimal> officeStat = ArrayListMultimap.create();

    public Analyzer() {
        Storage.getStorageList().stream().parallel().flatMap(i -> i).forEach(this::initializeMap);
    }

    public void writeSumAmountPerDay(String path) {
        log.info("Write statistic by date");
        sortAndWrite(getGroupByMap(dateStat), Map.Entry.comparingByKey(this::compareDate), path);
    }

    public void writeSumEachOffice(String path) {
        log.info("Write statistic by office");
        sortAndWrite(getGroupByMap(officeStat), Map.Entry.comparingByValue(Comparator.reverseOrder()), path);
    }

    private void initializeMap(String data) {
        dateStat.put(getDate(data), getAmount(data));
        officeStat.put(getOffice(data), getAmount(data));
    }

    private Map<String, BigDecimal> getGroupByMap(Multimap<String, BigDecimal> multimap) {
        Map<String, BigDecimal> groupByMap = new HashMap<>();
        multimap.asMap().forEach((i, j) -> groupByMap.put(i, sumCollect(j)));
        return groupByMap;
    }

    private BigDecimal sumCollect(Collection<BigDecimal> collection) {
        return collection.stream().reduce(BigDecimal.ZERO, BigDecimal::add);
        /*BigDecimal result = new BigDecimal(0);
        collection.forEach(i -> result.add(i).setScale(2, BigDecimal.ROUND_HALF_UP));
        return result;*/
    }

    private void sortAndWrite(Map<String, BigDecimal> map, Comparator<Map.Entry<String, BigDecimal>> comparator, String path) {
        map.entrySet()
                .stream()
                .sorted(comparator)
                .map(data -> String.format("%s %s", data.getKey(), data.getValue()))
                .forEach(line -> writeToFile(path, line));
    }

    private String getDate(String data) {
        return data.split(" ")[0];
    }

    private String getOffice(String data) {
        return data.split(" ")[2];
    }

    private BigDecimal getAmount(String data) {
        return BigDecimal.valueOf(Double.parseDouble(data.split(" ")[4]))
                .setScale(2, BigDecimal.ROUND_HALF_UP);
    }

    private void writeToFile(String fileName, String data) {
        try {
            Files.write(Paths.get(fileName), correctData(data).getBytes(), StandardOpenOption.CREATE, StandardOpenOption.APPEND);
        } catch (IOException e) {
            throw new RuntimeException("Can't write to file " + e);
        }
    }

    private String correctData(String data) {
        return new StringJoiner(" ").add(data).add("\n").toString();
    }

    private int compareDate(String date, String date1) {
        return LocalDate.parse(date, DateTimeFormatter.ofPattern("yyyy-MM-dd")).compareTo(LocalDate.parse(date1, DateTimeFormatter.ofPattern("yyyy-MM-dd")));
    }
}
