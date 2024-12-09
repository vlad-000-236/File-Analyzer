package com.exam.fileanalyzer.services;

import org.springframework.stereotype.Component;

import java.io.File;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Map;

@Component
public interface LogsAnalyzerInterface {
    String zipOpener(File fileName);
    ArrayList <File> unzipedFilesChecker(String unzipedFilesPath, LocalDate userDate, int numOfDays);

    Map<String, Integer> parser(ArrayList <File> filesList, String userString);

    Map<String, Integer> countEntriesInZipFile(String searchQuery,
                                               File zipFile,
                                               LocalDate startDate,
                                               Integer numberOfDays);
}
