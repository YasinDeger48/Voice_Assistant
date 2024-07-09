package com.yasindeger;

import edu.cmu.sphinx.api.Configuration;
import edu.cmu.sphinx.api.LiveSpeechRecognizer;
import edu.cmu.sphinx.api.SpeechResult;

import javax.sound.sampled.*;
import java.io.BufferedInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;


public class VoiceAssistant {
    public static void main(String[] args) {

        Configuration configuration = new Configuration();

        configuration.setAcousticModelPath("resource:/edu/cmu/sphinx/models/en-us/en-us/");
        configuration.setDictionaryPath("resource:/dictionary/7969.dic");
        configuration.setLanguageModelPath("resource:/dictionary/7969.lm");

        System.out.println(configuration.getAcousticModelPath());
        System.out.println(configuration.getDictionaryPath());
        //System.out.println("configuration.getLanguageModelPath() = " + configuration.getLanguageModelPath());
        try {

            List<String> list = new ArrayList<>(List.of("hey onix", "onix"));
            LiveSpeechRecognizer speechRecognizer = new LiveSpeechRecognizer(configuration);
            speechRecognizer.startRecognition(true);

            sleep(1);
            playSound("sounds/hey.wav");
            commandStacks(speechRecognizer,list);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static void commandStacks(LiveSpeechRecognizer speechRecognizer,List<String> list) {
        try {
            SpeechResult speechResult = null;

            while ((speechResult = speechRecognizer.getResult()) != null) {
                String voiceCommand = speechResult.getHypothesis();
                System.out.println("voiceCommand is = " + voiceCommand);
                boolean hey = list.stream().anyMatch(p -> p.equalsIgnoreCase(voiceCommand));

                if (hey) {
                    sleep(1);
                    playSound("sounds/hey2.wav");
                } else if (voiceCommand.equalsIgnoreCase("closing onix")) {
                    System.out.println("System closed");
                    sleep(1);
                    playSound("sounds/byebye.wav");
                    System.exit(1);
                } else if (voiceCommand.equalsIgnoreCase("Open Chrome")) {
                    String[] command = {"open", "-a", "/Applications/Google Chrome.app"};
                    Runtime.getRuntime().exec(command);
                } else if (voiceCommand.equalsIgnoreCase("Close Chrome")) {
                    String[] command = {"osascript", "-e", "tell application \"Google Chrome\" to quit"};
                    Runtime.getRuntime().exec(command);
                } else if (voiceCommand.equalsIgnoreCase("open brave")) {
                    String[] command = {"open", "-a", "/Applications/Brave Browser.app"};
                    Runtime.getRuntime().exec(command);
                } else if (voiceCommand.equalsIgnoreCase("close Brave")) {
                    String[] command = {"osascript", "-e", "tell application \"Brave\" to quit"};
                    Runtime.getRuntime().exec(command);
                } else if (voiceCommand.equalsIgnoreCase("open recorder")) {
                    String[] command = {"open", "-a", "licecap.app"};
                    Runtime.getRuntime().exec(command);
                } else if (voiceCommand.equalsIgnoreCase("close recorder")) {
                    String[] command = {"osascript", "-e", "tell application \"licecap\" to quit"};
                    Runtime.getRuntime().exec(command);
                } else if (voiceCommand.equalsIgnoreCase("take snapshot")) {
                    //takeScreenShot();
                } else if (voiceCommand.equalsIgnoreCase("take full snapshot")) {
                    //takeScreenShot();
                } else if (voiceCommand.equalsIgnoreCase("navigate")) {
                    //navigatingSite();
                } else if (voiceCommand.equalsIgnoreCase("open postman")) {
                    String[] command = {"open", "-a", "Postman.app"};
                    Runtime.getRuntime().exec(command);
                } else if (voiceCommand.equalsIgnoreCase("close postman")) {
                    String[] command = {"osascript", "-e", "tell application \"Postman\" to quit"};
                    Runtime.getRuntime().exec(command);
                } else if (voiceCommand.equalsIgnoreCase("open safari")) {
                    String[] command = {"open", "-a", "Safari.app"};
                    Runtime.getRuntime().exec(command);
                } else if (voiceCommand.equalsIgnoreCase("close safari")) {
                    String[] command = {"osascript", "-e", "tell application \"Safari\" to quit"};
                    Runtime.getRuntime().exec(command);
                } else if (voiceCommand.equalsIgnoreCase("open docker")) {
                    String[] command = {"open", "-a", "Docker.app"};
                    Runtime.getRuntime().exec(command);
                } else if (voiceCommand.equalsIgnoreCase("close docker")) {
                    String[] command = {"osascript", "-e", "tell application \"Docker Desktop\" to quit"};
                    Runtime.getRuntime().exec(command);
                } else {
                    System.out.println("Your voice is not recognized");
                }
            }
        }catch (RuntimeException ignored){

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }


    public static void playSound(String soundFile) {
        try {
            InputStream inputStream = VoiceAssistant.class.getClassLoader().getResourceAsStream(soundFile);
            if (inputStream == null) {
                throw new FileNotFoundException("File not found: " + soundFile);
            }

            // BufferedInputStream ile sarmala
            BufferedInputStream bufferedInputStream = new BufferedInputStream(inputStream);

            // AudioInputStream oluşturma
            AudioInputStream audioStream = AudioSystem.getAudioInputStream(bufferedInputStream);
            Clip clip = AudioSystem.getClip();
            clip.open(audioStream);
            clip.start();

            clip.addLineListener(event -> {
                if (event.getType() == LineEvent.Type.STOP) {
                    clip.close();
                }
            });

        } catch (IOException | UnsupportedAudioFileException | LineUnavailableException e) {
            e.printStackTrace();
        }
    }

    private static void sleep(long sec) {
        try {
            Thread.sleep(sec * 1000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}


