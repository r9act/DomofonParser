package ru.mishkin.service;

import org.springframework.stereotype.Component;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.stream.Stream;

@Component
public class VideoPostProcessor {

    private final Path TEMP_FOLDER = Path.of("E:\\DomofonParser\\temp\\");
    private final String LIBRARY_FOLDER = "E:\\DomofonParser\\library\\";

    public void mergeVideo() throws IOException {

        Path fileName = Path.of(LocalDate.now() + " " + LocalTime.now().getHour() + "-"
                + LocalTime.now().getMinute() + "-" + LocalTime.now().getSecond() + ".ts");
        Path resultFile = Path.of(LIBRARY_FOLDER + fileName);

        ByteBuffer buffer = ByteBuffer.wrap(new byte[getResultArrLength()]);

        DirectoryStream<Path> directory = Files.newDirectoryStream((TEMP_FOLDER));
        for (Path p : directory) {
            buffer.put(Files.readAllBytes(p));
        }

        Files.createFile(resultFile);
        Files.write(resultFile, buffer.array());
        removeTempFiles();
    }

    public int getResultArrLength() {
        var resultArrLength = 0;
        try (Stream<Path> files = Files.list(TEMP_FOLDER)) {
            resultArrLength = files.map(x -> {
                try {
                    return Files.readAllBytes(x).length;
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }).reduce(0, Integer::sum);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return resultArrLength;
    }

    public void removeTempFiles() throws IOException {
        Files.walk(TEMP_FOLDER)
                .map(Path::toFile)
                .forEach(File::delete);
    }
}



