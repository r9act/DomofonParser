package ru.mishkin.configuration;

import lombok.SneakyThrows;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import ru.mishkin.service.VideoPostProcessor;
import ru.mishkin.service.VideoStreamSaver;

import java.util.logging.Logger;

@EnableScheduling
@Configuration
public class Scheduler {

    Logger logger = Logger.getLogger("Schedular");

    private final VideoStreamSaver videoStreamSaver;
    private final VideoPostProcessor videoPostProcessor;

    static Integer numberOfSavedChunks = 0;

    public Scheduler(VideoStreamSaver videoStreamSaver, VideoPostProcessor videoPostProcessor) {
        this.videoStreamSaver = videoStreamSaver;
        this.videoPostProcessor = videoPostProcessor;
    }

    @SneakyThrows
    @Scheduled(cron = "0/10 * * ? * *")
    public void runProcessor() {
        videoStreamSaver.saveVideoStream();
        numberOfSavedChunks++;
        logger.info("Saved chunks: "+numberOfSavedChunks);

    }

    @SneakyThrows
    @Scheduled(cron = "0/60 * * ? * *")
    public void runVideoMerger() {
        videoPostProcessor.mergeVideo();
        logger.info("Merged videos");
    }

    @SneakyThrows
    @Scheduled(cron = "0 */60 * ? * *")
//    @Scheduled(cron = "0/10 * * ? * *")
    public void runVideoMoveToLib(){
        if (numberOfSavedChunks >= 360) {
            videoPostProcessor.moveFileToLibrary();
            numberOfSavedChunks = 0;
           logger.info("Moved file to library");
        } else logger.info("Not enough files yet!");
    }
}
