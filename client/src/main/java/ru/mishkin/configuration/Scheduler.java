package ru.mishkin.configuration;

import lombok.SneakyThrows;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import ru.mishkin.service.VideoStreamProcessor;

@EnableScheduling
@Configuration
public class Scheduler {

    private final VideoStreamProcessor videoStreamProcessor;

    public Scheduler(VideoStreamProcessor videoStreamProcessor) {
        this.videoStreamProcessor = videoStreamProcessor;
    }

    @SneakyThrows
    @Scheduled(cron ="0/10 * * ? * *")
    public void runProcessor() {
        videoStreamProcessor.processVideoStream();
    }
}
