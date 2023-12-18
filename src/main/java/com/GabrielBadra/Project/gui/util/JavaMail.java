package com.GabrielBadra.Project.gui.util;

import java.util.List;
import java.util.Properties;

import javax.mail.Address;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import com.GabrielBadra.Project.model.entities.Car;
public class JavaMail {

	public void sendMail(List<Car> cars) {
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
	      }catch(MessagingException e) {
	    	  e.printStackTrace();
	      }
	}

	private String FormatListToMessage(List<Car> cars) {
		String message = "Bom dia, segue a lista de veiculos identificados sem TAG:";
		
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
}



