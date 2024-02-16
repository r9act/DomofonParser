package ru.mishkin.utils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class FileNameFormatter {

    public static String getFileName(){
        String datePattern = "yyyy-MM-dd HH-mm-ss";
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(datePattern);
        LocalDateTime localDateTime = LocalDateTime.now();
         return dateTimeFormatter.format(localDateTime);
    }
}
