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

    private final String TEMP_FOLDER = "E:\\DomofonParser\\temp\\";
    private final String LIBRARY_FOLDER = "E:\\DomofonParser\\library\\";


    public void moveFileToLibrary() throws IOException {
        String fileName = LocalDate.now() + " " + LocalTime.now().getHour() + "-"
                + LocalTime.now().getMinute() + "-" + LocalTime.now().getSecond() + ".ts";

        Path sourcePath = getFirstFileName();
        Path destinationPath = Path.of(LIBRARY_FOLDER + fileName);

        if (!(Files.walk(Path.of(TEMP_FOLDER)).count() == 0)) {
            Files.copy(sourcePath, destinationPath);
            Files.delete(sourcePath);
        } else {
            System.out.println("No files!");
        }

    }

    public void mergeVideo() throws IOException {

        ByteBuffer buffer = ByteBuffer.wrap(new byte[getResultArrLength()]);

        DirectoryStream<Path> directory = Files.newDirectoryStream(Path.of((TEMP_FOLDER)));
        if (getFilesCount() >= 2) {
            for (Path p : directory) {
                buffer.put(Files.readAllBytes(p));
            }
            Files.write(getFirstFileName(), buffer.array());
            Files.delete(removeTempFile());

        }
    }


    public int getResultArrLength() {
        var resultArrLength = 0;
        try (Stream<Path> files = Files.list(Path.of(TEMP_FOLDER))) {
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

    public Path getFirstFileName() {
        Path headFileName;
        try (Stream<Path> files = Files.list(Path.of(TEMP_FOLDER))) {
            headFileName = files.findFirst().get();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return headFileName;
    }

    public int getFilesCount() {
        var filesCount = 0;
        try (Stream<Path> files = Files.list(Path.of(TEMP_FOLDER))) {
            filesCount = (int) files.count();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return filesCount;
    }

    public Path removeTempFile() throws IOException {
        return Files.walk(Path.of(TEMP_FOLDER))
                .reduce((path1, path2) -> path2).get();
    }


//    public void removeTempFiles() throws IOException {
//        Files.walk(Path.of(TEMP_FOLDER))
//                .map(Path::toFile)
//                .forEach(File::delete);
//    }
}



