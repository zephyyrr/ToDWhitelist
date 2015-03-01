package com.talesofdertinia.ToDWhitelist;

import java.util.Properties;

import javax.mail.Address;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class Emailer {
	
	public static class Settings {
		String username;
		String password;
		
		String host;
		short port;
	}

	Session session;
	
	public Emailer(Settings settings) {
		
		Properties props = new Properties();
		props.setProperty("mail.transport.protocol", "smtp");
		
		props.put("mail.smtp.starttls.enable","true");
		props.put("mail.smtp.auth", "true");
		
		props.setProperty("mail.smtp.host", settings.host);
		props.setProperty("mail.smtp.port", Short.toString(settings.port));
		
		props.setProperty("mail.user", settings.username);
		props.setProperty("mail.smtp.user", settings.username);
		props.setProperty("mail.smtp.from", settings.username);
		
		session = Session.getInstance(props, new javax.mail.Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(settings.username, settings.password);
            }
          });	
	}
	
	public void send(String recipient, String subject, String message) throws MessagingException {
		Transport trans = session.getTransport();
		trans.connect();
		MimeMessage mess = new MimeMessage(session);
		Address destination = new InternetAddress(recipient);
		mess.addRecipient(Message.RecipientType.TO, destination);
		mess.setSubject(subject);
		mess.setText(message, "utf-8", "html");
		mess.saveChanges();
		trans.sendMessage(mess, new Address[]{destination});
		trans.close();
		//Transport.send(mess);
	}
}
