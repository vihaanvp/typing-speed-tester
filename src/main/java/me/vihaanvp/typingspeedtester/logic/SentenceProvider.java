package me.vihaanvp.typingspeedtester.logic;

import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.List;

public class SentenceProvider {
    private final Map<String, List<String>> inbuiltSentences;
    private final Map<String, List<String>> externalSentences;
    private final String[] difficulties = {"Easy", "Medium", "Hard"};
    private final String[] fileNames = {"easy_sentences.txt", "medium_sentences.txt", "hard_sentences.txt"};

    public SentenceProvider() {
        inbuiltSentences = new HashMap<>();
        externalSentences = new HashMap<>();
        initInbuilt();
        loadExternal();
    }

    private void initInbuilt() {
        inbuiltSentences.put("Easy", Arrays.asList(
                "The quick brown fox jumps over the lazy dog.",
                "Typing is fun and easy to learn.",
                "Java is a popular programming language.",
                // ... add at least several more ...
                "Keep your hands on the keyboard."
        ));
        inbuiltSentences.put("Medium", Arrays.asList(
                "Consistent daily practice improves typing skills over time.",
                "Java Swing provides a set of lightweight GUI components.",
                // ... add more ...
                "Programming is a journey of continuous learning."
        ));
        inbuiltSentences.put("Hard", Arrays.asList(
                "Developers often use FlatLaf to modernize Java desktop applications, achieving a sophisticated and consistent dark theme.",
                "Exceptional typists can reach astonishing speeds while maintaining high accuracy across complex sentences."
                // ... add more ...
        ));
    }

    private void loadExternal() {
        File appDir = getAppDirectory();
        for (int i = 0; i < difficulties.length; i++) {
            String diff = difficulties[i];
            File f = new File(appDir, fileNames[i]);
            List<String> loaded = loadFromFile(f);
            externalSentences.put(diff, loaded);
        }
    }

    private List<String> loadFromFile(File file) {
        List<String> lines = new ArrayList<>();
        if (file.exists() && file.isFile()) {
            try (BufferedReader br = new BufferedReader(
                    new InputStreamReader(new FileInputStream(file), StandardCharsets.UTF_8))) {
                String line;
                while ((line = br.readLine()) != null) {
                    line = line.trim();
                    if (!line.isEmpty()) lines.add(line);
                }
            } catch (IOException e) {
                // Ignore, fallback to inbuilt
            }
        }
        return lines;
    }

    public String getRandomSentence(String difficulty) {
        List<String> pool = null;
        // Try external file for this difficulty
        pool = externalSentences.getOrDefault(difficulty, Collections.emptyList());
        if (pool != null && !pool.isEmpty()) return getRandom(pool);
        // Fallback to inbuilt for this difficulty
        pool = inbuiltSentences.getOrDefault(difficulty, Collections.emptyList());
        if (pool != null && !pool.isEmpty()) return getRandom(pool);
        // Fallback to inbuilt easy
        pool = inbuiltSentences.getOrDefault("Easy", Collections.emptyList());
        if (pool != null && !pool.isEmpty()) return getRandom(pool);
        // Total fallback
        return "No sentences available.";
    }

    private String getRandom(List<String> list) {
        Random rand = new Random();
        return list.get(rand.nextInt(list.size()));
    }

    public static File getAppDirectory() {
        // Use the directory where the app is running from (works for both jar/exe and IDE)
        try {
            String path = new File(SentenceProvider.class.getProtectionDomain().getCodeSource().getLocation().toURI()).getParent();
            return new File(path);
        } catch (Exception e) {
            return new File(System.getProperty("user.dir"));
        }
    }

    // Utility for MainFrame to open app directory
    public static void openAppDirectory() {
        File appDir = getAppDirectory();
        try {
            Desktop.getDesktop().open(appDir);
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Could not open folder: " + appDir.getAbsolutePath());
        }
    }

    // Utility for MainFrame to get tutorial text
    public static String getCustomSentenceTutorial() {
        return "To add or customize sentences:\n\n"
                + "1. In the folder that just opened, create or edit the following text files:\n"
                + "   - easy_sentences.txt\n"
                + "   - medium_sentences.txt\n"
                + "   - hard_sentences.txt\n"
                + "2. Each line in the file is a new sentence.\n"
                + "3. Save your changes and restart the app to use your custom sentences.\n"
                + "4. If a file is missing or empty, inbuilt sentences will be used for that difficulty.";
    }
}