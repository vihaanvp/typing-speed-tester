package me.vihaanvp.typingspeedtester.logic;

public class TypingTestManager {
    private final SentenceProvider sentenceProvider;
    private final HighScoreManager highScoreManager;

    private String sentence;
    private String difficulty;
    private long startTime;
    private boolean running;
    private int errorCount;
    private int wpm;
    private int accuracy;
    private String userInput = "";

    public TypingTestManager(SentenceProvider provider, HighScoreManager highScoreManager) {
        this.sentenceProvider = provider;
        this.highScoreManager = highScoreManager;
    }

    public void startTest(String sentence, String difficulty) {
        this.sentence = sentence;
        this.difficulty = difficulty;
        this.startTime = System.currentTimeMillis();
        this.running = true;
        this.errorCount = 0;
        this.wpm = 0;
        this.accuracy = 0;
        this.userInput = "";
    }

    public void onUserInput(String userInput) {
        this.userInput = userInput;
        if (!running) return;
        int len = Math.min(userInput.length(), sentence.length());
        errorCount = 0;
        int correct = 0;
        for (int i = 0; i < len; i++) {
            if (userInput.charAt(i) == sentence.charAt(i)) correct++;
            else errorCount++;
        }
        errorCount += Math.abs(userInput.length() - sentence.length());

        // Stats
        double elapsedMin = getElapsedTimeSec() / 60.0;
        int wordCount = (userInput.split("\\s+").length);
        wpm = (int) (elapsedMin > 0 ? wordCount / elapsedMin : 0);
        accuracy = (int) (userInput.length() > 0 ? (100.0 * correct / sentence.length()) : 0);
    }

    public boolean isComplete() {
        return running && userInput.equals(sentence);
    }

    public void stopTest() {
        running = false;
    }

    public boolean isRunning() {
        return running;
    }

    public String getSentence() {
        return sentence;
    }

    public String getDifficulty() {
        return difficulty;
    }

    public int getErrorCount() {
        return errorCount;
    }

    public int getWpm() {
        return wpm;
    }

    public int getAccuracy() {
        return accuracy;
    }

    public double getElapsedTimeSec() {
        if (running) {
            return (System.currentTimeMillis() - startTime) / 1000.0;
        } else {
            return 0.0;
        }
    }

    public String getCurrentInput() {
        return userInput;
    }
}