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
        //configuration.setAcousticModelPath("resource:/edu/cmu/sphinx/models/tr-tr/tr-tr/");
        configuration.setDictionaryPath("resource:/dictionary/7969.dic");
        configuration.setLanguageModelPath("resource:/dictionary/7969.lm");

        try {

            List<String> list = new ArrayList<>(List.of("hey boncuk", "boncuk"));
            LiveSpeechRecognizer speechRecognizer = new LiveSpeechRecognizer(configuration);
            speechRecognizer.startRecognition(true);

            sleep(1);
            playSound("sounds/dinliyorum.wav");
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
                String finalVoiceCommand = voiceCommand;
                boolean hey = list.stream().anyMatch(p -> p.equalsIgnoreCase(finalVoiceCommand));
                voiceCommand = (hey) ? "true" : voiceCommand;
                String [] command = null;
                switch (voiceCommand.toLowerCase()) {
                    case "true":
                        sleep(1);
                        playSound("sounds/efendim.wav");
                        speechRecognizer.stopRecognition();
                        waitSoundPlayback(2);
                        speechRecognizer.startRecognition(true);
                        break;
                    case "close boncuk":
                        System.out.println("System closed");
                        sleep(1);
                        playSound("sounds/fikra.wav");
                        waitSoundPlayback(24);
                        playSound("sounds/hoscakal.wav");
                        sleep(1);
                        System.exit(1);
                        break;
                    case "open chrome":
                        command = new String[]{"open", "-a", "/Applications/Google Chrome.app"};
                        Runtime.getRuntime().exec(command);
                        break;
                    case "close chrome":
                        command = new String[]{"osascript", "-e", "tell application \"Google Chrome\" to quit"};
                        Runtime.getRuntime().exec(command);
                        break;
                    case "open brave":
                        command = new String[]{"open", "-a", "/Applications/Brave Browser.app"};
                        Runtime.getRuntime().exec(command);
                        break;
                    case "close brave":
                        command = new String[]{"osascript", "-e", "tell application \"Brave\" to quit"};
                        Runtime.getRuntime().exec(command);
                        break;
                    case "open postman":
                        command = new String[]{"open", "-a", "Postman.app"};
                        Runtime.getRuntime().exec(command);
                        break;
                    case "close postman":
                        command = new String[]{"osascript", "-e", "tell application \"Postman\" to quit"};
                        Runtime.getRuntime().exec(command);
                        break;
                    case "open docker":
                        command = new String[]{"open", "-a", "Docker.app"};
                        Runtime.getRuntime().exec(command);
                        break;
                    case "close docker":
                        command = new String[]{"osascript", "-e", "tell application \"Docker Desktop\" to quit"};
                        Runtime.getRuntime().exec(command);
                        break;
                    case "open safari":
                        command = new String[]{"open", "-a", "Safari.app"};
                        Runtime.getRuntime().exec(command);
                        break;
                    case "close safari":
                        command = new String[]{"osascript", "-e", "tell application \"Safari\" to quit"};
                        Runtime.getRuntime().exec(command);
                        break;
                    case "open recorder":
                        command = new String[]{"open", "-a", "licecap.app"};
                        Runtime.getRuntime().exec(command);
                        break;
                    case "close recorder":
                        command = new String[]{"osascript", "-e", "tell application \"licecap\" to quit"};
                        Runtime.getRuntime().exec(command);
                        break;
                    case "hey boncuk":
                    case "boncuk":
                        playSound("sounds/efendim.wav");
                        break;
                    default:
                        System.out.println("Your voice is not recognized");
                        break;
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

            BufferedInputStream bufferedInputStream = new BufferedInputStream(inputStream);

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
    private static void waitSoundPlayback(long sec) {
        try {
            Thread.sleep(sec*1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}


