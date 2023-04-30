package com.damaru.doorbell;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.core.io.Resource;

@SpringBootApplication
public class DoorbellApplication implements CommandLineRunner {

    private static final Logger log = LoggerFactory.getLogger(DoorbellApplication.class);

    @Autowired
    DataChannel dataChannel;

    @Value("classpath:media/bell.wav")
    Resource resourceFile;

    public static void main(String[] args) {
        SpringApplication.run(DoorbellApplication.class, args);
    }

    DataMessage.SubscribeListener dataListener = new DataMessage.SubscribeListener() {

        @Override
        public void onReceive(DataMessage dataMessage) {
            String topic = dataMessage.getTopic();
            String payload = dataMessage.getPayload();
            log.info("Got " + payload + " " + topic);

            if (topic.contains("data")) {
                int val = Integer.valueOf(payload);
                if (val > 0) {
                    Sound.play(resourceFile);
                }
            }
        }

        @Override
        public void handleException(Exception exception) {
            log.error("oops", exception);
        }
    };

    @Override
    public void run(String... args) throws Exception {
        dataChannel.subscribe(dataListener);
        while (true) {
            Thread.sleep(1000);
        }
    }
}
