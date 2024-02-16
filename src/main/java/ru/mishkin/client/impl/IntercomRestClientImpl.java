package ru.mishkin.client.impl;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import ru.mishkin.client.IntercomRestClient;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.file.Path;
import java.nio.file.Paths;

@Component
public class IntercomRestClientImpl implements IntercomRestClient {

    @Value("${domofon.folder.temp}")
    private String tempFolder;

    private final String HOST = "https://camera.lipetsk.ru/";
    private final String AREA = "ms-30.camera.lipetsk.ru/live/";
    private final String CAMERA_ID = "4d37af26-bc90-4b48-b0ff-ca968937e74e/";
    private final String PLAYLIST = "playlist.m3u8";
    private final String VIDEO_EXTENSION = ".ts";

    private final HttpClient httpClient = HttpClient.newHttpClient();

    @Override
    public void getVideoChunk(Integer chunkId) throws IOException, InterruptedException {
        HttpRequest build = HttpRequest.newBuilder()
                .uri(URI.create(HOST + AREA + CAMERA_ID + chunkId.toString() + VIDEO_EXTENSION))
                .GET()
                .build();

        var response = httpClient.send(build, HttpResponse.BodyHandlers.ofByteArray());
        byte[] videoChunk = response.body();

        //Domo/temp
        String directory = tempFolder + "/" + chunkId + VIDEO_EXTENSION;

        File outputVideoFile = new File(directory);

        try (FileOutputStream outputStream = new FileOutputStream(outputVideoFile)) {
            outputStream.write(videoChunk);
        }
    }

    @Override
    public File getPlaylist() throws IOException, InterruptedException {
        HttpRequest build = HttpRequest.newBuilder()
                .uri(URI.create(HOST + AREA + CAMERA_ID + PLAYLIST))
                .GET()
                .build();

        var response = httpClient.send(build, HttpResponse.BodyHandlers.ofByteArray());

        byte[] playlist = response.body();
        Path root = Paths.get(".").normalize().toAbsolutePath();
        String directory = root + "/" + PLAYLIST;

        File outputPlaylistFile = new File(directory);

        try (FileOutputStream outputStream = new FileOutputStream(outputPlaylistFile)) {
            outputStream.write(playlist);
        }
        return outputPlaylistFile;
    }
}
