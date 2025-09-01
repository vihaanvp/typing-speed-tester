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
                "Keep your hands on the keyboard.",
                "Practice makes perfect in typing.",
                "The sun shines bright today.",
                "Cats like to sleep in the sun.",
                "Dogs are loyal companions.",
                "Books help expand your mind.",
                "Music makes life more beautiful.",
                "Time flies when you are having fun.",
                "Learning new skills is rewarding.",
                "Friends make life worth living.",
                "Nature provides endless beauty.",
                "Good food brings people together.",
                "Exercise keeps your body healthy.",
                "Reading improves your vocabulary.",
                "Kindness costs nothing but means everything.",
                "Dreams give life meaning and purpose.",
                "Laughter is the best medicine for stress."
        ));
        
        inbuiltSentences.put("Medium", Arrays.asList(
                "Consistent daily practice improves typing skills over time.",
                "Java Swing provides a set of lightweight GUI components.",
                "Programming is a journey of continuous learning.",
                "Software development requires patience, creativity, and problem-solving.",
                "The keyboard layout was designed for efficiency and comfort.",
                "Regular exercise and proper nutrition contribute to overall wellness.",
                "Technology has revolutionized the way we communicate and work.",
                "Critical thinking skills are essential in our information age.",
                "Collaboration and teamwork often lead to better solutions.",
                "Environmental conservation is crucial for future generations.",
                "Artificial intelligence is transforming multiple industries worldwide.",
                "Learning multiple programming languages broadens your perspective.",
                "Good documentation saves time and prevents misunderstandings.",
                "Version control systems help manage code changes effectively.",
                "User experience design focuses on creating intuitive interfaces.",
                "Cybersecurity measures protect sensitive data from threats.",
                "Agile methodologies emphasize flexibility and rapid iteration.",
                "Database optimization improves application performance significantly.",
                "Open source software promotes collaboration and innovation.",
                "Continuous integration and deployment streamline development workflows."
        ));
        
        inbuiltSentences.put("Hard", Arrays.asList(
                "Developers often use FlatLaf to modernize Java desktop applications, achieving a sophisticated and consistent dark theme.",
                "Exceptional typists can reach astonishing speeds while maintaining high accuracy across complex sentences.",
                "Microservices architecture decomposes monolithic applications into smaller, independently deployable services.",
                "Functional programming paradigms emphasize immutability, pure functions, and declarative coding styles.",
                "Machine learning algorithms analyze vast datasets to identify patterns and make predictions.",
                "Distributed systems face challenges including network partitions, eventual consistency, and fault tolerance.",
                "Cross-platform development frameworks enable code reuse across multiple operating systems and devices.",
                "Containerization technologies like Docker provide consistent deployment environments across development stages.",
                "GraphQL APIs offer flexible query capabilities, allowing clients to request exactly the data they need.",
                "Blockchain technology ensures data integrity through cryptographic hashing and distributed consensus mechanisms.",
                "Real-time systems require deterministic behavior with strict timing constraints and predictable performance.",
                "Concurrent programming involves managing multiple threads of execution while avoiding race conditions.",
                "Compiler optimization techniques transform source code into efficient machine instructions automatically.",
                "Neural networks simulate biological brain functions using interconnected nodes and weighted connections.",
                "Event-driven architectures promote loose coupling between components through asynchronous message passing.",
                "Quantum computing leverages quantum mechanical phenomena to solve computationally intensive problems.",
                "Cryptographic protocols secure communications through mathematical algorithms and key exchange mechanisms.",
                "Domain-driven design aligns software architecture with business requirements and organizational structure.",
                "Performance profiling identifies bottlenecks and optimization opportunities in software applications.",
                "Biometric authentication systems verify user identity through unique physiological or behavioral characteristics."
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