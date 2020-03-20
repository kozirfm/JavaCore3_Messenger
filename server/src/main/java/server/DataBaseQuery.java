package server;

import java.sql.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DataBaseQuery {
    private static Connection connection;
    private static Statement statement;
    private static final String dbPath = "jdbc:sqlite:server/src/main/resources/main.db";
    private static final Logger logger = Logger.getLogger(DataBaseQuery.class.getName());

    private static void connectDb() throws ClassNotFoundException, SQLException {
        logger.setLevel(Level.ALL);
        Class.forName("org.sqlite.JDBC");
        connection = DriverManager.getConnection(dbPath);
        statement = connection.createStatement();
        logger.log(Level.INFO, "База данных подключена");
    }

    private static void disconnectDb() {
        try {
            statement.close();
        } catch (SQLException e) {
            logger.log(Level.SEVERE, e.getMessage(), e);
        }
        try {
            connection.close();
            logger.log(Level.INFO, "База данных отключена");
        } catch (SQLException e) {
            logger.log(Level.SEVERE, e.getMessage(), e);
        }
    }

    public static boolean prepareRegistrationData(String login, String password, String nickname) {
        try {
            connectDb();
            PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO users (login,password, nickname) VALUES (?, ?, ?);");
            preparedStatement.setString(1, login);
            preparedStatement.setString(2, password);
            preparedStatement.setString(3, nickname);
            int result = preparedStatement.executeUpdate();
            preparedStatement.close();
            return result == 1;
        } catch (SQLException | ClassNotFoundException e) {
            logger.log(Level.SEVERE, "Пользователь с такими данными уже существует", e);
        } finally {
            disconnectDb();
        }
        return false;
    }

    public static String selectNickname(String login, String password) {
        try {
            connectDb();
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT nickname FROM users WHERE login = ? AND password = ?;");
            preparedStatement.setString(1, login);
            preparedStatement.setString(2, password);
            ResultSet resultSet = preparedStatement.executeQuery();
            String result = resultSet.getString("nickname");
            preparedStatement.close();
            return result;
        } catch (SQLException | ClassNotFoundException e) {
            logger.log(Level.SEVERE, "Неверный логин / пароль", e);
        } finally {
            disconnectDb();
        }
        return null;
    }

    public static boolean changeNickname(String lastNickname, String newNickname) {
        try {
            connectDb();
            PreparedStatement preparedStatement = connection.prepareStatement("UPDATE users SET nickname = ? WHERE nickname = ?;");
            preparedStatement.setString(1, newNickname);
            preparedStatement.setString(2, lastNickname);
            int result = preparedStatement.executeUpdate();
            preparedStatement.close();
            return result == 1;
        } catch (SQLException | ClassNotFoundException e) {
            logger.log(Level.SEVERE, "Никнейм занят", e);
        } finally {
            disconnectDb();
        }
        return false;
    }
}
