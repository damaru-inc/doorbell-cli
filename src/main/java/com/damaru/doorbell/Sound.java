package com.damaru.doorbell;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import java.io.BufferedInputStream;
import java.io.File;


public class Sound {

    public static synchronized void play(final Resource file)
    {
        // Note: use .wav files
        new Thread(new Runnable() {
            public void run() {
                try {
//                    File file = new ClassPathResource(
//                            fileName).getFile();
                    //File file = resourceFile;
                    Clip clip = AudioSystem.getClip();
                    //AudioInputStream inputStream = AudioSystem.getAudioInputStream(file.getFile());
                    AudioInputStream inputStream =
                            AudioSystem.getAudioInputStream(new BufferedInputStream(file.getInputStream()));
                    clip.open(inputStream);
                    clip.start();
                } catch (Exception e) {
                    System.out.println("play sound error: " + e.getMessage() + " for " + file.getFilename());
                }
            }
        }).start();
    }
}
