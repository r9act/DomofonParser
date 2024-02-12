package ru.mishkin.service;

import org.springframework.stereotype.Component;
import ru.mishkin.client.IntercomRestClient;
import ru.mishkin.parser.PlaylistParser;

import java.io.IOException;

@Component
public class VideoStreamSaver {

    private final IntercomRestClient client;

    public VideoStreamSaver(IntercomRestClient client) {
        this.client = client;
    }

    public void saveVideoStream() throws IOException, InterruptedException {
        int chunkId = PlaylistParser.getChunkId(client.getPlaylist());
        client.getVideoChunk(chunkId);
    }
}


