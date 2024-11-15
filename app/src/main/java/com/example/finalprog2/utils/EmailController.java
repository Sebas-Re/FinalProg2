package com.example.finalprog2.utils;
import androidx.annotation.OptIn;
import androidx.media3.common.util.Log;
import androidx.media3.common.util.UnstableApi;

import java.util.Properties;
import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class EmailController {

    @OptIn(markerClass = UnstableApi.class)
    public static void enviarEmailUsuario(String recipientEmail, String subject, String body) {
        Log.i("EmailController", "Iniciando envio de email");
        // Se crea un nuevo hilo, para ejecutar la funcion en segundo plano
        new Thread(() -> {

                // 1. Creacion propiedades
                Properties props = new Properties();
                props.put("mail.smtp.auth", "true");
                props.put("mail.smtp.starttls.enable", "true");
                props.put("mail.smtp.host", "smtp.gmail.com");
                props.put("mail.smtp.port", "587");

                // 2. Creacion sesion
                Session session = Session.getInstance(props, new Authenticator(){
                    @Override
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication("finalprog2firebase@gmail.com", "oskr uevh nxtc kfah");
                    }
                });

            try {
                // 3. Se crea el mensaje
                Message message = new MimeMessage(session);
                message.setFrom(new InternetAddress("finalprog2firebase@gmail.com"));
                message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(recipientEmail));
                message.setSubject(subject);
                message.setText(body);

                // 4. Send message
                Transport.send(message);
                Log.i("EmailController", "Email enviado exitosamente");
                // 5. manejar caso de exito

            } catch (MessagingException e) {
                // 6. manejar caso de error
                Log.e("EmailController", "Error al enviar el email: " + e.getMessage());
            }
        }).start();
    }



}
