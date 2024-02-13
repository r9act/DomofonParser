package ru.mishkin.configuration;

import lombok.SneakyThrows;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import ru.mishkin.service.VideoPostProcessor;
import ru.mishkin.service.VideoStreamSaver;

@EnableScheduling
@Configuration
public class Scheduler {

    private final VideoStreamSaver videoStreamSaver;
    private final VideoPostProcessor videoPostProcessor;

    public Scheduler(VideoStreamSaver videoStreamSaver, VideoPostProcessor videoPostProcessor) {
        this.videoStreamSaver = videoStreamSaver;
        this.videoPostProcessor = videoPostProcessor;
    }

    @SneakyThrows
    @Scheduled(cron = "0/10 * * ? * *")
    public void runProcessor() {
        videoStreamSaver.saveVideoStream();
        System.out.println("Saved chunk!");
    }

    @SneakyThrows
    @Scheduled(cron = "0/11  * * ? * *")
    public void runVideoMerger() {
        videoPostProcessor.mergeVideo();
        System.out.println("Merged videos!");
    }
    @SneakyThrows
    @Scheduled(cron = "59/60  * * ? * *")
    public void runVideoMoveToLib(){
        videoPostProcessor.moveFileToLibrary();
        System.out.println("Moved 1 min video to library!");
    }
}
