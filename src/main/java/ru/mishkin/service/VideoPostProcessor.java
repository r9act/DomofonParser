package ru.mishkin.service;

import lombok.extern.log4j.Log4j;
import lombok.extern.slf4j.Slf4j;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import ru.mishkin.utils.FileNameFormatter;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.stream.Stream;

@Log4j
@Component
public class VideoPostProcessor {

    Logger logger = Logger.getLogger(VideoPostProcessor.class);

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
                logger.info("File moved to library: " + sourcePath + " -> " + destinationPath);
            }
        }
    }

    public void mergeVideos() throws IOException {
        ByteBuffer buffer = ByteBuffer.wrap(new byte[getResultArrLength()]);
        try (DirectoryStream<Path> directory = Files.newDirectoryStream(Path.of((tempFolder)))) {
            if (getFilesCount() >= 2) {
                for (Path p : directory) {
                    buffer.put(Files.readAllBytes(p));
                }
                Files.write(getFirstFileName(), buffer.array());
                var numberOfMergedFiles = getFilesCount();
                removeTempFiles();
                logger.info("Files merged: " + numberOfMergedFiles);
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
            logger.error("Error happened while getting ResultArrayLength: " + e.getMessage());
        }
        return resultArrLength;
    }

    public Path getFirstFileName() {
        Path headFileName = null;
        try (Stream<Path> files = Files.list(Path.of(tempFolder))) {
            headFileName = files.findFirst().get();
            return headFileName;
        } catch (IOException e) {
            logger.error("Error happened while getting FirstFileName: " + e.getMessage());
        }
        return headFileName;
    }

    public int getFilesCount() {
        var filesCount = 0;
        try (Stream<Path> files = Files.list(Path.of(tempFolder))) {
            filesCount = (int) files.count();
        } catch (IOException e) {
            logger.error("Error happened while getting FilesCount: " + e.getMessage());
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



