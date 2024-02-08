package ru.mishkin;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import ru.mishkin.client.IntercomRestClient;
import ru.mishkin.client.impl.IntercomRestClientImpl;
import ru.mishkin.parser.PlaylistParser;

import java.io.File;
import java.io.IOException;

//@SpringBootApplication
public class ClientApplication {

    public static void main(String[] args) throws IOException, InterruptedException {
//        SpringApplication.run(ClientApplication.class, args);
        IntercomRestClient client = new IntercomRestClientImpl();
        PlaylistParser parser = new PlaylistParser();
        client.getPlaylist();
        String chunkId = parser.parsePlaylist(new File("playlist.m3u8"));
        client.getVideoChunk(chunkId);
    }
}
