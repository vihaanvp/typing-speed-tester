package me.vihaanvp.typingspeedtester.logic;

import me.vihaanvp.typingspeedtester.model.HighScore;

import java.io.*;
import java.util.*;

public class HighScoreManager {
    private static final String FILE = "highscores.txt";
    private Map<String, List<HighScore>> highScores = new HashMap<>();

    public HighScoreManager() {
        load();
    }

    public void recordScore(String difficulty, int wpm) {
        highScores.putIfAbsent(difficulty, new ArrayList<>());
        List<HighScore> list = highScores.get(difficulty);
        list.add(new HighScore(wpm, new Date()));
        list.sort((a, b) -> Integer.compare(b.getWpm(), a.getWpm()));
        if (list.size() > 10) list.remove(list.size() - 1);
        save();
    }

    public String getHighScoresText() {
        StringBuilder sb = new StringBuilder();
        for (String diff : Arrays.asList("Easy", "Medium", "Hard")) {
            sb.append(diff).append(":\n");
            List<HighScore> list = highScores.getOrDefault(diff, new ArrayList<>());
            int i = 1;
            for (HighScore score : list) {
                sb.append(i++).append(". ").append(score.getWpm()).append(" WPM\n");
            }
            sb.append("\n");
        }
        return sb.toString();
    }

    private void save() {
        try (PrintWriter out = new PrintWriter(new FileWriter(FILE))) {
            for (String diff : highScores.keySet()) {
                for (HighScore score : highScores.get(diff)) {
                    out.println(diff + "," + score.getWpm() + "," + score.getDate().getTime());
                }
            }
        } catch (IOException e) {
            // Ignore
        }
    }

    private void load() {
        highScores.clear();
        File file = new File(FILE);
        if (!file.exists()) return;
        try (BufferedReader in = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = in.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 3) {
                    String diff = parts[0];
                    int wpm = Integer.parseInt(parts[1]);
                    Date date = new Date(Long.parseLong(parts[2]));
                    highScores.putIfAbsent(diff, new ArrayList<>());
                    highScores.get(diff).add(new HighScore(wpm, date));
                }
            }
        } catch (IOException e) {
            // Ignore
        }
    }
}