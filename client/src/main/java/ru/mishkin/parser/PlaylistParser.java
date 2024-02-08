package ru.mishkin.parser;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.Reader;
import java.util.Scanner;

public class PlaylistParser {

    public String parsePlaylist(File file) throws FileNotFoundException {
        Scanner sc = new Scanner(file);
        String[] arr = null;
        String data = "";
        while (sc.hasNextLine()) {
            data = sc.nextLine();
        }
        return data;
    }
}
