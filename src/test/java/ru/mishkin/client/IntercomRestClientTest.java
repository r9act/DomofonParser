package ru.mishkin.client;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockserver.integration.ClientAndServer;
import org.mockserver.model.HttpRequest;
import org.mockserver.model.HttpResponse;
import ru.mishkin.client.impl.IntercomRestClientImpl;

import java.io.IOException;

import static org.mockserver.integration.ClientAndServer.startClientAndServer;

public class IntercomRestClientTest {

    private ClientAndServer mockServer;

    private final IntercomRestClient client = new IntercomRestClientImpl();

    @BeforeEach
    public void startServer() {
        mockServer = startClientAndServer(8888);
    }

    @AfterEach
    public void stopServer() {
        mockServer.stop();
    }

    @Test
    public void getPlayListShouldReturnFile() throws IOException, InterruptedException {

        String expectedFileName = "playlist.m3u8";

        mockServer.when(
                HttpRequest.request()
                        .withMethod("GET")
                        .withPath("/getPlaylist.m3u8")
        ).respond(
                HttpResponse.response().withStatusCode(200).withBody(expectedFileName)
        );

        var actual = client.getPlaylist().getName();

        Assertions.assertEquals(actual, expectedFileName);
    }

}
