package me.vihaanvp.typingspeedtester.model;

public enum PracticeMode {
    NORMAL("Normal Text", "Regular sentences for general typing practice"),
    NUMBERS("Numbers", "Practice typing numbers and numeric sequences"),
    PUNCTUATION("Punctuation", "Focus on punctuation marks and special characters"),
    PROGRAMMING("Programming", "Code snippets and programming syntax"),
    QUOTES("Famous Quotes", "Inspiring quotes and famous sayings"),
    CUSTOM("Custom", "Your own custom sentences");
    
    private final String displayName;
    private final String description;
    
    PracticeMode(String displayName, String description) {
        this.displayName = displayName;
        this.description = description;
    }
    
    public String getDisplayName() {
        return displayName;
    }
    
    public String getDescription() {
        return description;
    }
}