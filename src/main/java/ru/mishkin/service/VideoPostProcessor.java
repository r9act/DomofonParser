package ru.mishkin.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import ru.mishkin.utils.FileNameFormatter;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.stream.Stream;

@Component
public class VideoPostProcessor {

    @Value("${domofon.folder.temp}")
    private String tempFolder;
    @Value("${domofon.folder.library}")
    private String libraryFolder;

    private final String VIDEO_EXTENSION = ".ts";

    public void moveFileToLibrary() throws IOException {

        Path sourcePath = getFirstFileName();
        Path destinationPath = Path.of(libraryFolder + "/" + FileNameFormatter.getFileName() + VIDEO_EXTENSION);

        try (Stream<Path> stream = Files.walk(Path.of(tempFolder))) {
            if (stream.findAny().isPresent()) {
                Files.copy(sourcePath, destinationPath);
                Files.delete(sourcePath);
            } else {
                System.out.println("No files!");
            }
        }
    }

    public void mergeVideo() throws IOException {
        ByteBuffer buffer = ByteBuffer.wrap(new byte[getResultArrLength()]);
        try (DirectoryStream<Path> directory = Files.newDirectoryStream(Path.of((tempFolder)))) {
            if (getFilesCount() >= 2) {
                for (Path p : directory) {
                    buffer.put(Files.readAllBytes(p));
                }
                Files.write(getFirstFileName(), buffer.array());
                removeTempFiles();
            }
        }
    }

    public int getResultArrLength() {
        var resultArrLength = 0;
        try (Stream<Path> files = Files.list(Path.of(tempFolder))) {
            resultArrLength = files.map(file -> {
                try {
                    return Files.readAllBytes(file).length;
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
        try (Stream<Path> files = Files.list(Path.of(tempFolder))) {
            headFileName = files.findFirst().get();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return headFileName;
    }

    public int getFilesCount() {
        var filesCount = 0;
        try (Stream<Path> files = Files.list(Path.of(tempFolder))) {
            filesCount = (int) files.count();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return filesCount;
    }

    public void removeTempFiles() throws IOException {
        try (Stream<Path> files = Files.list(Path.of(tempFolder))) {
            files.skip(1)
                    .map(Path::toFile)
                    .forEach(File::delete);
        }
    }
}



