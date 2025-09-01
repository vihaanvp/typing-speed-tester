package me.vihaanvp.typingspeedtester.model;

import java.io.*;
import java.util.Properties;

public class UserSettings {
    private static final String SETTINGS_FILE = "settings.properties";
    
    // Default settings
    private boolean soundEnabled = false;
    private String theme = "Dark";
    private boolean showWpmInRealTime = true;
    private boolean highlightErrors = true;
    private int fontSize = 18;
    private String fontFamily = "Consolas";
    private boolean autoSaveResults = true;
    
    private Properties properties;
    
    public UserSettings() {
        properties = new Properties();
        loadSettings();
    }
    
    private void loadSettings() {
        File settingsFile = new File(SETTINGS_FILE);
        if (settingsFile.exists()) {
            try (FileInputStream fis = new FileInputStream(settingsFile)) {
                properties.load(fis);
                
                soundEnabled = Boolean.parseBoolean(properties.getProperty("soundEnabled", "false"));
                theme = properties.getProperty("theme", "Dark");
                showWpmInRealTime = Boolean.parseBoolean(properties.getProperty("showWpmInRealTime", "true"));
                highlightErrors = Boolean.parseBoolean(properties.getProperty("highlightErrors", "true"));
                fontSize = Integer.parseInt(properties.getProperty("fontSize", "18"));
                fontFamily = properties.getProperty("fontFamily", "Consolas");
                autoSaveResults = Boolean.parseBoolean(properties.getProperty("autoSaveResults", "true"));
                
            } catch (IOException | NumberFormatException e) {
                // Use defaults if loading fails
                System.err.println("Could not load settings, using defaults: " + e.getMessage());
            }
        }
    }
    
    public void saveSettings() {
        properties.setProperty("soundEnabled", String.valueOf(soundEnabled));
        properties.setProperty("theme", theme);
        properties.setProperty("showWpmInRealTime", String.valueOf(showWpmInRealTime));
        properties.setProperty("highlightErrors", String.valueOf(highlightErrors));
        properties.setProperty("fontSize", String.valueOf(fontSize));
        properties.setProperty("fontFamily", fontFamily);
        properties.setProperty("autoSaveResults", String.valueOf(autoSaveResults));
        
        try (FileOutputStream fos = new FileOutputStream(SETTINGS_FILE)) {
            properties.store(fos, "TypeSpeedApp User Settings");
        } catch (IOException e) {
            System.err.println("Could not save settings: " + e.getMessage());
        }
    }
    
    // Getters and setters
    public boolean isSoundEnabled() { return soundEnabled; }
    public void setSoundEnabled(boolean soundEnabled) { this.soundEnabled = soundEnabled; }
    
    public String getTheme() { return theme; }
    public void setTheme(String theme) { this.theme = theme; }
    
    public boolean isShowWpmInRealTime() { return showWpmInRealTime; }
    public void setShowWpmInRealTime(boolean showWpmInRealTime) { this.showWpmInRealTime = showWpmInRealTime; }
    
    public boolean isHighlightErrors() { return highlightErrors; }
    public void setHighlightErrors(boolean highlightErrors) { this.highlightErrors = highlightErrors; }
    
    public int getFontSize() { return fontSize; }
    public void setFontSize(int fontSize) { this.fontSize = fontSize; }
    
    public String getFontFamily() { return fontFamily; }
    public void setFontFamily(String fontFamily) { this.fontFamily = fontFamily; }
    
    public boolean isAutoSaveResults() { return autoSaveResults; }
    public void setAutoSaveResults(boolean autoSaveResults) { this.autoSaveResults = autoSaveResults; }
}