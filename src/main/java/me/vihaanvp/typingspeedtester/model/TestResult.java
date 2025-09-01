package me.vihaanvp.typingspeedtester.model;

public class TestResult {
    private final int wpm;
    private final int accuracy;
    private final int errorCount;
    private final double timeSeconds;
    private final String difficulty;
    private final TestMode testMode;
    private final int charactersTyped;
    private final int wordsTyped;
    
    public enum TestMode {
        COMPLETION("Complete Text"),
        TIME_30("30 Seconds"),
        TIME_60("1 Minute"),
        TIME_120("2 Minutes"),
        TIME_300("5 Minutes");
        
        private final String displayName;
        
        TestMode(String displayName) {
            this.displayName = displayName;
        }
        
        public String getDisplayName() {
            return displayName;
        }
        
        public int getTimeLimit() {
            switch (this) {
                case TIME_30: return 30;
                case TIME_60: return 60;
                case TIME_120: return 120;
                case TIME_300: return 300;
                default: return -1; // No time limit
            }
        }
    }
    
    public TestResult(int wpm, int accuracy, int errorCount, double timeSeconds, 
                     String difficulty, TestMode testMode, int charactersTyped, int wordsTyped) {
        this.wpm = wpm;
        this.accuracy = accuracy;
        this.errorCount = errorCount;
        this.timeSeconds = timeSeconds;
        this.difficulty = difficulty;
        this.testMode = testMode;
        this.charactersTyped = charactersTyped;
        this.wordsTyped = wordsTyped;
    }
    
    // Getters
    public int getWpm() { return wpm; }
    public int getAccuracy() { return accuracy; }
    public int getErrorCount() { return errorCount; }
    public double getTimeSeconds() { return timeSeconds; }
    public String getDifficulty() { return difficulty; }
    public TestMode getTestMode() { return testMode; }
    public int getCharactersTyped() { return charactersTyped; }
    public int getWordsTyped() { return wordsTyped; }
    
    public double getCharactersPerMinute() {
        return charactersTyped / (timeSeconds / 60.0);
    }
}