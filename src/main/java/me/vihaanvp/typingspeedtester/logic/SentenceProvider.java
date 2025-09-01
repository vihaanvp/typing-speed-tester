package me.vihaanvp.typingspeedtester.logic;

import me.vihaanvp.typingspeedtester.model.PracticeMode;

import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.List;

public class SentenceProvider {
    private final Map<String, List<String>> inbuiltSentences;
    private final Map<String, List<String>> externalSentences;
    private final Map<PracticeMode, List<String>> practiceContent;
    private final String[] difficulties = {"Easy", "Medium", "Hard"};
    private final String[] fileNames = {"easy_sentences.txt", "medium_sentences.txt", "hard_sentences.txt"};

    public SentenceProvider() {
        inbuiltSentences = new HashMap<>();
        externalSentences = new HashMap<>();
        practiceContent = new HashMap<>();
        initInbuilt();
        initPracticeContent();
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

    private void initPracticeContent() {
        // Numbers practice
        practiceContent.put(PracticeMode.NUMBERS, Arrays.asList(
                "1234567890 0987654321 1122334455",
                "Phone: 555-123-4567 or 555-987-6543",
                "Years: 1995, 2000, 2010, 2023, 2024",
                "Calculations: 123 + 456 = 579, 789 - 321 = 468",
                "ID Numbers: 98765, 43210, 11223, 99887",
                "Price: $123.45, $67.89, $999.99, $1,234.56",
                "Coordinates: 40.7128, -74.0060 (New York)",
                "Binary: 101010, 110011, 001100, 111000",
                "Hex: A1B2C3, FF00AA, 123ABC, DEF456",
                "Math: 2^10 = 1024, 3.14159, sqrt(144) = 12"
        ));
        
        // Punctuation practice
        practiceContent.put(PracticeMode.PUNCTUATION, Arrays.asList(
                "Hello, world! How are you today? I'm fine, thanks.",
                "Questions: What? When? Where? How? Why? Who?",
                "Contractions: don't, won't, can't, shouldn't, wouldn't",
                "Quotes: \"Hello,\" she said. 'This is important!'",
                "Email: user@example.com, support@company.org",
                "Special: #hashtag, @username, &amp;, 50% off!",
                "Lists: (a) first, (b) second, (c) third item",
                "Math: x + y = z; a > b; c < d; e == f",
                "Coding: if (x > 0) { return true; } else { return false; }",
                "Brackets: [array], {object}, (parentheses), <tags>"
        ));
        
        // Programming practice
        practiceContent.put(PracticeMode.PROGRAMMING, Arrays.asList(
                "public static void main(String[] args) {",
                "for (int i = 0; i < length; i++) {",
                "if (condition == true && flag != false) {",
                "private final List<String> items = new ArrayList<>();",
                "try { process(); } catch (Exception e) { log.error(e); }",
                "const result = await fetch('/api/data').then(res => res.json());",
                "function calculateSum(a, b) { return a + b; }",
                "SELECT * FROM users WHERE age > 18 ORDER BY name;",
                "git commit -m \"Fix bug in user authentication module\"",
                "docker run -p 8080:80 --name webapp nginx:latest"
        ));
        
        // Famous quotes
        practiceContent.put(PracticeMode.QUOTES, Arrays.asList(
                "The only way to do great work is to love what you do. - Steve Jobs",
                "Life is what happens to you while you're busy making other plans. - John Lennon",
                "The future belongs to those who believe in the beauty of their dreams. - Eleanor Roosevelt",
                "It is during our darkest moments that we must focus to see the light. - Aristotle",
                "The way to get started is to quit talking and begin doing. - Walt Disney",
                "Your time is limited, so don't waste it living someone else's life. - Steve Jobs",
                "If life were predictable it would cease to be life, and be without flavor. - Eleanor Roosevelt",
                "If you look at what you have in life, you'll always have more. - Oprah Winfrey",
                "If you set your goals ridiculously high and it's a failure, you will fail above everyone else's success. - James Cameron",
                "Success is not final, failure is not fatal: it is the courage to continue that counts. - Winston Churchill"
        ));
    }

    public String getRandomSentence(String difficulty) {
        return getRandomSentence(difficulty, PracticeMode.NORMAL);
    }
    
    public String getRandomSentence(String difficulty, PracticeMode practiceMode) {
        List<String> pool = null;
        
        // Handle practice modes
        if (practiceMode != PracticeMode.NORMAL && practiceMode != PracticeMode.CUSTOM) {
            pool = practiceContent.get(practiceMode);
            if (pool != null && !pool.isEmpty()) {
                return getRandom(pool);
            }
        }
        
        // Handle custom mode
        if (practiceMode == PracticeMode.CUSTOM) {
            pool = externalSentences.getOrDefault(difficulty, Collections.emptyList());
            if (pool != null && !pool.isEmpty()) return getRandom(pool);
        }
        
        // Normal mode or fallback
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