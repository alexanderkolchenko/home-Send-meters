package com.example.enumerators;

import java.sql.*;
import java.util.ArrayList;

/**
 * Подключение к БД для сохранения отправленных показаний
 * и для вывода списка последних отправленных показаний
 */
public class DBConnection {
    private static final String URL = "jdbc:oracle:thin:@tcp://localhost:1521/root";
    private static final String USERNAME = "SYS as SYSDBA";
    private static final String PASSWORD = "123";
    private static final String INSERT_NEW = "INSERT INTO homeenum (date_add, k_cold, k_hot, b_cold, b_hot, light, gas) VALUES(sysdate, ?,?,?,?,?,?)";
    private static final String GET_ALL = "SELECT * FROM homeenum WHERE id =(SELECT max(id) FROM homeenum)";


    /**
     * Запрос последних показаний, отправленных пользователем в УК например в прошлом месяце
     * @return список последних отправленых показаний каждого счетчика
     */
    public static ArrayList<String> insertLastValues() {

        PreparedStatement preparedStatement = null;
        ArrayList<String> lastValues = new ArrayList();

        try (Connection connection = DriverManager.getConnection(URL, USERNAME, PASSWORD)) {

            Driver driver = new oracle.jdbc.driver.OracleDriver();
            DriverManager.registerDriver(driver);
            preparedStatement = connection.prepareStatement(GET_ALL);
            ResultSet rs = preparedStatement.executeQuery();

            while (rs.next()) {
                lastValues.add(rs.getString("k_cold"));
                lastValues.add(rs.getString("k_hot"));
                lastValues.add(rs.getString("b_cold"));
                lastValues.add(rs.getString("b_hot"));
                lastValues.add(rs.getString("light"));
                lastValues.add(rs.getString("gas"));
            }
        } catch (SQLException e) {
            e.getMessage();
        }
        return lastValues;
    }

    /**
     * Запись отправленных показаний счетчиков в БД
     * @param arr показания счетчиков, введенных пользователем в тесктовые поля
     */
    public static void writeIntoDBNewValues(ArrayList<String> arr) {

        PreparedStatement preparedStatement = null;

        try (Connection connection = DriverManager.getConnection(URL, USERNAME, PASSWORD)) {

            Driver driver = new oracle.jdbc.driver.OracleDriver();
            DriverManager.registerDriver(driver);
            preparedStatement = connection.prepareStatement(INSERT_NEW);
            for (int i = 1; i < arr.size(); i++) {
                preparedStatement.setString(i, arr.get(i - 1));
            }
            preparedStatement.execute();

        } catch (SQLException e) {
            e.getMessage();
        }
    }
}
