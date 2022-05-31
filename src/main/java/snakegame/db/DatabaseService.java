
package snakegame.db;

import org.sqlite.SQLiteDataSource;

import java.sql.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class DatabaseService {
    private static DatabaseService databaseService;
    private final static String dbFileName = "jdbc:sqlite:scores.s3db";
    private static DateTimeFormatter dateFormatter;

    // METHODS -------------------------------------
    private DatabaseService() {
        create_db_if_not_exists();
        dateFormatter = DateTimeFormatter.ofPattern("dd-MM-uuuu kk:mm");
    }

    private void create_db_if_not_exists() {
        SQLiteDataSource dataSource = new SQLiteDataSource();
        dataSource.setUrl(dbFileName);

        try (Connection connection = dataSource.getConnection()) {
            try (Statement statement = connection.createStatement()) {
                String instruction = "CREATE TABLE if not exists SCORES (" +
                        "score INTEGER," +
                        "date VARCHAR);";
                statement.executeUpdate(instruction);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static DatabaseService Instance() {
        if (databaseService == null) {
            databaseService = new DatabaseService();
        }

        return databaseService;
    }

    public void addScore(int score) {
        SQLiteDataSource dataSource = new SQLiteDataSource();
        dataSource.setUrl(dbFileName);

        try (Connection connection = dataSource.getConnection()) {
            String instruction = "INSERT INTO SCORES VALUES (?, ?);";
            try (PreparedStatement preparedStatement = connection.prepareStatement(instruction)) {
                preparedStatement.setString(1, String.valueOf(score));
                preparedStatement.setString(2, LocalDateTime.now().format(dateFormatter));

                preparedStatement.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<DatabaseScoreRecord> getScores() {
        SQLiteDataSource dataSource = new SQLiteDataSource();
        dataSource.setUrl(dbFileName);
        try (Connection connection = dataSource.getConnection()) {
            String instruction = "SELECT * FROM SCORES ORDER BY SCORE DESC;";
            try (PreparedStatement preparedStatement = connection.prepareStatement(instruction)) {
                ResultSet results = preparedStatement.executeQuery();

                ArrayList<DatabaseScoreRecord> records = new ArrayList<>();
                while (results.next()) {
                    records.add(new DatabaseScoreRecord(results.getString(1),
                            results.getString(2)));
                }

                return records;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return new ArrayList<>();
    }
}
