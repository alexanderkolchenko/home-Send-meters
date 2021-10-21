package com.example.enumerators;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import javax.mail.MessagingException;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Чтение, отправка и сохранение показаний бытовых счетчиков
 */

public class MeterController implements Initializable {

    /**
     * поле список последних отправленных показаний, например в прошлом месяце, выведенных на экран
     */
    private ArrayList<Label> labelsOfLastValues;

    @FXML
    Label lastKitchenCold;
    @FXML
    Label lastKitchenHot;
    @FXML
    Label lastBathCold;
    @FXML
    Label lastBathHot;
    @FXML
    Label lastLight;
    @FXML
    Label lastGas;

    /**
     * поле список текущих показаний, введенных пользователем
     * в соответсвующие тесктовые поля для отправки и сохранения
     */
    private ArrayList<TextField> textFieldsToSending;

    @FXML
    private TextField kitchenColdToSending;
    @FXML
    private TextField kitchenHotToSending;
    @FXML
    private TextField bathColdToSending;
    @FXML
    private TextField bathHotToSending;
    @FXML
    private TextField lightToSending;
    @FXML
    private TextField gasToSending;

    //почта получателя, в нашем случае управляющей компании
    @FXML
    private TextField email;


    //regex для показаний счетчиков, например 123,456(1-3 цифры после запятой) или просто целое число
    public static final Pattern VALID_VALUE_OF_METERS_REGEX = Pattern.compile("\\d+(\\,\\d{1,3})?");

    //regex для e-mail
    public static final Pattern VALID_EMAIL_ADDRESS_REGEX =
            Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);


    /**
     * поле список текущих показаний для отправки
     */
    private ArrayList<String> currentValuesOfMeters;

    /**
     * чтение введенных показаний, их проверка, отправка получателю и запись в БД
     */
    @FXML
    protected void onSendButtonClick() throws MessagingException, UnsupportedEncodingException {

        //очистка списка в случае неудачной отправки
        currentValuesOfMeters.clear();

        for (TextField t : textFieldsToSending) {
            currentValuesOfMeters.add(t.getText());
        }

        if (checkBeforeSending(currentValuesOfMeters)) {
            DBConnection.writeIntoDBNewValues(currentValuesOfMeters);
            SendEmail.sendEmail(currentValuesOfMeters);
        }
    }

    /**
     * Обновление полей для вывода на экран отправленных показаний
     */
    @FXML
    protected void onUpdateButtonClick() throws SQLException {
        updateLastValuesAfterSending();
    }

    /**
     * Очистка всех тесктовых полей
     */
    @FXML
    protected void onClearButtonClick() {
        for (TextField t : textFieldsToSending) {
            t.clear();
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        labelsOfLastValues = new ArrayList<>();
        labelsOfLastValues.add(lastKitchenCold);
        labelsOfLastValues.add(lastKitchenHot);
        labelsOfLastValues.add(lastBathCold);
        labelsOfLastValues.add(lastBathHot);
        labelsOfLastValues.add(lastLight);
        labelsOfLastValues.add(lastGas);

        textFieldsToSending = new ArrayList<>();
        textFieldsToSending.add(kitchenColdToSending);
        textFieldsToSending.add(kitchenHotToSending);
        textFieldsToSending.add(bathColdToSending);
        textFieldsToSending.add(bathHotToSending);
        textFieldsToSending.add(lightToSending);
        textFieldsToSending.add(gasToSending);
        textFieldsToSending.add(email);


        //вывод на экран предыдущих показаний при запуске приложения
        try {
            updateLastValuesAfterSending();
        } catch (SQLException e) {
            e.getMessage();
        }
    }

    /**
     * Вывод на экран предыдущих показаний после отправки
     */
    protected void updateLastValuesAfterSending() throws SQLException {
        try {
            currentValuesOfMeters = DBConnection.insertLastValues();
            for (int i = 0; i < currentValuesOfMeters.size(); i++) {
                labelsOfLastValues.get(i).setText(currentValuesOfMeters.get(i));
            }
        } catch (Exception e) {
            for (Label l : labelsOfLastValues) {
                l.setText("no connection");
            }
        }
    }

    /**
     * Проверка показаний перед отправкой на соответствие regex и на пустые поля
     *
     * @param arr текущие показания, введенные пользователем
     * @return true, если значения корректны
     */
    protected boolean checkBeforeSending(ArrayList<String> arr) {

        int countOfErrors = 0;
        for (int i = 0; i < arr.size() - 1; i++) {
            Matcher matcher = VALID_VALUE_OF_METERS_REGEX.matcher(arr.get(i));
            if (arr.get(i).equals("") || !matcher.matches()) {
                textFieldsToSending.get(i).setStyle("-fx-text-box-border: #ff0000;");
                countOfErrors++;
            } else {
                textFieldsToSending.get(i).setStyle("-fx-border-color: none;");
            }
        }
        Matcher matcher = VALID_EMAIL_ADDRESS_REGEX.matcher(arr.get(6));
        if (!matcher.matches()) {
            textFieldsToSending.get(6).setStyle("-fx-text-box-border: #ff0000;");
            return false;
        } else {
            textFieldsToSending.get(6).setStyle("-fx-border-color: none;");
        }

        if (countOfErrors > 0) {
            return false;
        }
        return true;
    }
}

