package com.example.enumerators;

import com.sun.mail.imap.IMAPFolder;
import com.sun.mail.imap.IMAPStore;
import com.sun.mail.imap.protocol.ListInfo;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Properties;
import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

/**
 * Подключается к учетной записи портала mail.ru через сервер IMAP
 * и формирует сообщение для отправки
 */

public class SendEmail extends IMAPFolder {
    protected SendEmail(String fullName, char separator, IMAPStore store, Boolean isNamespace) {
        super(fullName, separator, store, isNamespace);
    }

    protected SendEmail(ListInfo li, IMAPStore store) {
        super(li, store);
    }

    static void sendEmail(ArrayList<String> arr) throws MessagingException, UnsupportedEncodingException {

        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", "smtp.mail.ru");
        props.put("mail.smtp.port", "465");
        props.put("mail.smtps.ssl.checkserveridentity", true);
        props.put("mail.smtps.ssl.trust", "*");
        props.put("mail.smtp.ssl.enable", "true");
        props.put("mail.debug", "false");
        props.put("mail.store.protocol", "imaps");
        props.put("mail.imap.ssl.enable", "true");
        props.put("mail.imap.port", "993");

        Session session = Session.getDefaultInstance(props);
        MimeMessage message = new MimeMessage(session);

        message.setFrom(new InternetAddress("17307@bk.ru", "Александр Кольченко"));
        message.addRecipient(Message.RecipientType.TO, new InternetAddress(arr.get(6)));
        message.setSubject("Счетчики");
        message.setText("\n\nКухня: \n    ХВС: " + arr.get(0) +
                "\n    ГВС: " + arr.get(1) +
                "\nВанная:   \n    ХВС: " + arr.get(2) +
                "\n    ГВС: " + arr.get(3) +
                "\nСвет: " + arr.get(4) +
                "\nГаз: " + arr.get(5));
        Transport tr = session.getTransport();

        //login - логин учетной записи
        //password - пароль для внешних приложений, генерируется в настройках безопасности mail.ru
        tr.connect("17307@bk.ru", "8S7wLefE4AhZ5qda6Qpq");
        tr.sendMessage(message, message.getAllRecipients());
        tr.close();
    }
}
