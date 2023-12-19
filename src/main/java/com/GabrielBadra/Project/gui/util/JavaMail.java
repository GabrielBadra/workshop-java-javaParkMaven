package com.GabrielBadra.Project.gui.util;

import java.time.LocalTime;
import java.util.List;
import java.util.Properties;

import javax.mail.Address;
import javax.mail.Message;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import com.GabrielBadra.Project.model.entities.Car;

import javafx.application.Platform;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
public class JavaMail {

	public void sendMail(List<Car> cars, Alert alert) throws Exception {
		String remetente = "gabrielfreitasalves9@gmail.com";
		String senha = "tkrfavzhciiksaih";
		String destination = "gabrielfreitasalves755@gmail.com";
		
		String carsText = FormatListToMessage(cars);
		
		
		 Properties props = new Properties();
		 
		 	props.put("mail.smtp.user", remetente); 
	        props.put("mail.smtp.host", "smtp.gmail.com"); 
	        props.put("mail.smtp.port", "25"); 
	        props.put("mail.debug", "true"); 
	        props.put("mail.smtp.auth", "true"); 
	        props.put("mail.smtp.starttls.enable","true"); 
	        props.put("mail.smtp.EnableSSL.enable","true");

	        props.setProperty("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");   
	        props.setProperty("mail.smtp.socketFactory.fallback", "false");   
	        props.setProperty("mail.smtp.port", "465");   
	        props.setProperty("mail.smtp.socketFactory.port", "465");
	        
	      Session session = Session.getDefaultInstance(props, new javax.mail.Authenticator() {
	    	  protected PasswordAuthentication getPasswordAuthentication() {
	    		  return new PasswordAuthentication(remetente, senha);
	    	  }
	      });
	        
	      session.setDebug(true);
	      
	      try {
	    	  
	    	  Message message = new MimeMessage(session);
	    	  message.setFrom(new InternetAddress(remetente));
	    	  
	    	  Address[] toUser = InternetAddress.parse(destination);
	    	  
	    	  message.setRecipients(Message.RecipientType.TO, toUser);
	    	  message.setSubject("Estacionamento");
	    	  message.setText(carsText);
	    	  Transport.send(message);
	      }catch(Exception e) {
	    	 throw new Exception(e.getMessage());
	      }
	}

	private String FormatListToMessage(List<Car> cars) {
		String greeting = greetings();
		String message = greeting + ", segue a lista de veiculos identificados sem TAG:";
		
		if(cars.size() > 0) {
			for(Car car : cars) {
				message += "\n" + car;
			}
		}else {
			message += "\n\n" + "Nenhum carro identificado sem TAG.";
		}
		
		//"Mensagens padroes"
		message += "\n\n" + "Att. Gabriel Freitas Alves de Moura.";
		message += "\n\n\n" + "Sistema de Gabriel";
		
		return message;
	}

	private String greetings() {
		LocalTime currentTime = LocalTime.now();
        int hour = currentTime.getHour();
        System.out.println(hour);
        
        if(hour > 5 && hour < 13) {
        	return "Bom dia";
        }else if(hour > 12 && hour < 18) {
        	return "Boa tarde";
        }else {
        	return "Boa noite";
        }
	}
}



