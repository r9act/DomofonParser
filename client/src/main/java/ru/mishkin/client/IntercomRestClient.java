package ru.mishkin.client;

import org.springframework.stereotype.Component;

import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.IOException;

public interface IntercomRestClient {
    void getVideoChunk(String chunkId) throws IOException, InterruptedException;

    void getPlaylist() throws IOException, InterruptedException;
}
