package me.vihaanvp.typingspeedtester.ui;

import me.vihaanvp.typingspeedtester.model.UserSettings;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class SettingsDialog extends JDialog {
    private UserSettings settings;
    private boolean cancelled = false;
    
    private JCheckBox soundEnabledBox;
    private JComboBox<String> themeBox;
    private JCheckBox showWpmBox;
    private JCheckBox highlightErrorsBox;
    private JSpinner fontSizeSpinner;
    private JComboBox<String> fontFamilyBox;
    private JCheckBox autoSaveBox;
    
    public SettingsDialog(JFrame parent, UserSettings settings) {
        super(parent, "Settings", true);
        this.settings = settings;
        
        initComponents();
        loadCurrentSettings();
        
        setSize(450, 400);
        setLocationRelativeTo(parent);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
    }
    
    private void initComponents() {
        setLayout(new BorderLayout());
        
        // Main panel with settings
        JPanel mainPanel = new JPanel(new BorderLayout());
        
        // Settings panel
        JPanel settingsPanel = new JPanel(new GridBagLayout());
        settingsPanel.setBorder(BorderFactory.createTitledBorder("Preferences"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;
        
        // Sound settings
        gbc.gridx = 0; gbc.gridy = 0;
        settingsPanel.add(new JLabel("Enable Sound Effects:"), gbc);
        gbc.gridx = 1;
        soundEnabledBox = new JCheckBox();
        settingsPanel.add(soundEnabledBox, gbc);
        
        // Theme settings
        gbc.gridx = 0; gbc.gridy = 1;
        settingsPanel.add(new JLabel("Theme:"), gbc);
        gbc.gridx = 1;
        themeBox = new JComboBox<>(new String[]{"Dark", "Light"});
        settingsPanel.add(themeBox, gbc);
        
        // Real-time WPM
        gbc.gridx = 0; gbc.gridy = 2;
        settingsPanel.add(new JLabel("Show Real-time WPM:"), gbc);
        gbc.gridx = 1;
        showWpmBox = new JCheckBox();
        settingsPanel.add(showWpmBox, gbc);
        
        // Highlight errors
        gbc.gridx = 0; gbc.gridy = 3;
        settingsPanel.add(new JLabel("Highlight Errors:"), gbc);
        gbc.gridx = 1;
        highlightErrorsBox = new JCheckBox();
        settingsPanel.add(highlightErrorsBox, gbc);
        
        // Font size
        gbc.gridx = 0; gbc.gridy = 4;
        settingsPanel.add(new JLabel("Font Size:"), gbc);
        gbc.gridx = 1;
        fontSizeSpinner = new JSpinner(new SpinnerNumberModel(18, 10, 36, 1));
        settingsPanel.add(fontSizeSpinner, gbc);
        
        // Font family
        gbc.gridx = 0; gbc.gridy = 5;
        settingsPanel.add(new JLabel("Font Family:"), gbc);
        gbc.gridx = 1;
        fontFamilyBox = new JComboBox<>(new String[]{"Consolas", "Monaco", "Courier New", "Monospaced"});
        fontFamilyBox.setEditable(true);
        settingsPanel.add(fontFamilyBox, gbc);
        
        // Auto-save results
        gbc.gridx = 0; gbc.gridy = 6;
        settingsPanel.add(new JLabel("Auto-save Results:"), gbc);
        gbc.gridx = 1;
        autoSaveBox = new JCheckBox();
        settingsPanel.add(autoSaveBox, gbc);
        
        mainPanel.add(settingsPanel, BorderLayout.CENTER);
        
        // Button panel
        JPanel buttonPanel = new JPanel(new FlowLayout());
        JButton okButton = new JButton("OK");
        JButton cancelButton = new JButton("Cancel");
        JButton resetButton = new JButton("Reset to Defaults");
        
        okButton.addActionListener(e -> {
            saveSettings();
            setVisible(false);
        });
        
        cancelButton.addActionListener(e -> {
            cancelled = true;
            setVisible(false);
        });
        
        resetButton.addActionListener(e -> {
            int result = JOptionPane.showConfirmDialog(this, 
                "Reset all settings to defaults?", 
                "Confirm Reset", 
                JOptionPane.YES_NO_OPTION);
            if (result == JOptionPane.YES_OPTION) {
                resetToDefaults();
            }
        });
        
        buttonPanel.add(okButton);
        buttonPanel.add(cancelButton);
        buttonPanel.add(resetButton);
        
        add(mainPanel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);
    }
    
    private void loadCurrentSettings() {
        soundEnabledBox.setSelected(settings.isSoundEnabled());
        themeBox.setSelectedItem(settings.getTheme());
        showWpmBox.setSelected(settings.isShowWpmInRealTime());
        highlightErrorsBox.setSelected(settings.isHighlightErrors());
        fontSizeSpinner.setValue(settings.getFontSize());
        fontFamilyBox.setSelectedItem(settings.getFontFamily());
        autoSaveBox.setSelected(settings.isAutoSaveResults());
    }
    
    private void saveSettings() {
        settings.setSoundEnabled(soundEnabledBox.isSelected());
        settings.setTheme((String) themeBox.getSelectedItem());
        settings.setShowWpmInRealTime(showWpmBox.isSelected());
        settings.setHighlightErrors(highlightErrorsBox.isSelected());
        settings.setFontSize((Integer) fontSizeSpinner.getValue());
        settings.setFontFamily((String) fontFamilyBox.getSelectedItem());
        settings.setAutoSaveResults(autoSaveBox.isSelected());
        settings.saveSettings();
    }
    
    private void resetToDefaults() {
        soundEnabledBox.setSelected(false);
        themeBox.setSelectedItem("Dark");
        showWpmBox.setSelected(true);
        highlightErrorsBox.setSelected(true);
        fontSizeSpinner.setValue(18);
        fontFamilyBox.setSelectedItem("Consolas");
        autoSaveBox.setSelected(true);
    }
    
    public boolean wasCancelled() {
        return cancelled;
    }
}