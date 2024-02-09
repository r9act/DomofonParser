package ru.mishkin.service;

import org.springframework.stereotype.Component;
import ru.mishkin.client.IntercomRestClient;
import ru.mishkin.parser.PlaylistParser;

import java.io.IOException;

@Component
public class VideoStreamProcessor {

    private final IntercomRestClient client;

    public VideoStreamProcessor(IntercomRestClient client) {
        this.client = client;
    }

    public void processVideoStream() throws IOException, InterruptedException {
        int chunkId = PlaylistParser.getChunkId(client.getPlaylist());
        client.getVideoChunk(chunkId);
        System.out.println(chunkId);
    }
}


