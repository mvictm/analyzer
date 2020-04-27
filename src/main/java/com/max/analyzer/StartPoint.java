package com.max.analyzer;

import com.max.analyzer.service.Executor;
import lombok.extern.java.Log;

@Log
public class StartPoint {
    public static void main(String[] args) {
        log.info(">> Start program");
        Executor.execute(args);
        log.info("<< Finish program");
    }
}
