package ru.mishkin.utils;

import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

@Component
public class PlaylistParser {

    public static Integer getChunkId(File file) throws FileNotFoundException {
        Scanner sc = new Scanner(file);
        String data = "";
        while (sc.hasNextLine()) data = sc.nextLine();
        String[] value = data.split("\\.");
        String number = value[0];
        return Integer.parseInt(number);
    }
}
