package snakegame.db;

public class DatabaseScoreRecord {
    private String score;
    private String date;

    public DatabaseScoreRecord(String score, String date) {
        this.score = score;
        this.date = date;
    }

    public String getScore() {
        return score;
    }

    public String getDate() {
        return date;
    }
}
