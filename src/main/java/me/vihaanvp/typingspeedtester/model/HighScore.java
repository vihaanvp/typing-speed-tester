package me.vihaanvp.typingspeedtester.model;

import java.util.Date;

public class HighScore {
    private final int wpm;
    private final Date date;

    public HighScore(int wpm, Date date) {
        this.wpm = wpm;
        this.date = date;
    }

    public int getWpm() {
        return wpm;
    }

    public Date getDate() {
        return date;
    }
}