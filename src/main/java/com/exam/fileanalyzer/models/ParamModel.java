package com.exam.fileanalyzer.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Component;

import java.io.File;
import java.time.LocalDate;

@Component
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ParamModel {
    String searchQuery;

    File zipFile;

    LocalDate startDate;

    Integer numberOfDays;
}
