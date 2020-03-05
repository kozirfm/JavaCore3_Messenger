package server;

import java.sql.*;

public class DataBase {
    private Connection connection;
    private Statement statement;

    public void connectDb() throws ClassNotFoundException, SQLException {
        Class.forName("org.sqlite.JDBC");
        connection = DriverManager.getConnection("jdbc:sqlite:server/src/main/resources/main.db");
        statement = connection.createStatement();
        System.out.println("База данных подключена");
    }

    public void disconnectDb() {
        try {
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        try {
            connection.close();
            System.out.println("База данных отключена");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public boolean prepareRegistrationData(String login, String password, String nickname) {
        try {
            connectDb();
            PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO users (login,password, nickname) VALUES (?, ?, ?);");
            preparedStatement.setString(1, login);
            preparedStatement.setString(2, password);
            preparedStatement.setString(3, nickname);
            return preparedStatement.executeUpdate() == 1;
        } catch (SQLException | ClassNotFoundException e) {
            System.out.println("Пользователь с такими данными уже существует");
        } finally {
            disconnectDb();
        }
        return false;
    }

    public String selectNickname(String login, String password) {
        try {
            connectDb();
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT nickname FROM users WHERE login = ? AND password = ?;");
            preparedStatement.setString(1, login);
            preparedStatement.setString(2, password);
            ResultSet resultSet = preparedStatement.executeQuery();
            return resultSet.getString("nickname");
        } catch (SQLException | ClassNotFoundException e) {
            System.out.println("Неверный логин / пароль");
        } finally {
            disconnectDb();
        }
        return null;
    }

    public boolean changeNickname(String lastNickname, String newNickname) {
        try {
            connectDb();
            PreparedStatement preparedStatement = connection.prepareStatement("UPDATE users SET nickname = ? WHERE nickname = ?;");
            preparedStatement.setString(1, newNickname);
            preparedStatement.setString(2, lastNickname);
            return preparedStatement.executeUpdate() == 1;
        } catch (SQLException | ClassNotFoundException e) {
            System.out.println("Никнейм занят");
        } finally {
            disconnectDb();
        }
        return false;
    }
}
