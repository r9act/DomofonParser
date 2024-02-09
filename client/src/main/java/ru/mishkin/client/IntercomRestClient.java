package ru.mishkin.client;

import java.io.File;
import java.io.IOException;

public interface IntercomRestClient {

    void getVideoChunk(Integer chunkId) throws IOException, InterruptedException;

    File getPlaylist() throws IOException, InterruptedException;
}
