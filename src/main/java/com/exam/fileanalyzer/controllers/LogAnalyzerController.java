package com.exam.fileanalyzer.controllers;

import com.exam.fileanalyzer.services.LogsAnalyzerInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.time.LocalDate;
import java.util.Map;


@RestController
@RequestMapping("/api")
public class LogAnalyzerController {

    private LogsAnalyzerInterface logsAnalyzerInterface;
    private Map<String, Integer> entries;

    private final File zipPath = new File("src\\test\\resources" +
                                                    "\\logs-27_02_2018-03_03_2018.zip");

    @Autowired
    public LogAnalyzerController(LogsAnalyzerInterface logsAnalyzerInterface){
        this.logsAnalyzerInterface = logsAnalyzerInterface;
    }

    @GetMapping("/logsAnalyze")
    public Map<String, Integer> LogsAnalyzer(){
        return entries = logsAnalyzerInterface.countEntriesInZipFile("Mozilla", zipPath,
                LocalDate.of(2018, 2, 27), 3);
    }
}
