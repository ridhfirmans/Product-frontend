package com.aplikasi.product.productfrontend.helper;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;

import com.aplikasi.product.productfrontend.model.Product;

public class EmailHelper {

	private static final Logger LOGGER = LoggerFactory.getLogger(EmailHelper.class);
	
	public final String emailMessageOne = "Hi, this is a reminder that your stock of <br/> Product: ";
	public final String emailMessageTwo = "<br/>has reached to the <b>minimum stock</b>, please consider to update your stock. <br/>";
	public final String emailMessageThree = "Thank you.";
	public final String emailSubject = "Reminder to Update Your Stock";
	
	public void sendEmail(Product[] product, String emailSender, String emailSenderPassword) throws AddressException, MessagingException, IOException {

		LOGGER.info("emailSender: "+emailSender );
		
		Properties props = new Properties();
		props.put("mail.smtp.auth", "true");
		props.put("mail.smtp.starttls.enable", "true");
		props.put("mail.smtp.host", "smtp.gmail.com");
		props.put("mail.smtp.port", "587");
		
		Session session = Session.getInstance(props, new javax.mail.Authenticator() {
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(emailSender, emailSenderPassword);
			}
		});

		Message msg = new MimeMessage(session);

		msg.setRecipients(Message.RecipientType.TO, InternetAddress.parse("yourrecipientsmail@test.com"));
		msg.setSubject(emailSubject);
		msg.setSentDate(new Date());

		//to get products that have reached the minimum stock
		List<String> productsName = new ArrayList<String>();
		for(Product prod : product){
			if(prod.getStok() == prod.getMinimumStok()){
				productsName.add(prod.getNamaProduct());
			}
		}
		
		MimeBodyPart messageBodyPart = new MimeBodyPart();
		messageBodyPart.setContent(emailMessageOne +productsName.toString()+ emailMessageTwo + emailMessageThree, "text/html");

		Multipart multipart = new MimeMultipart();
		multipart.addBodyPart(messageBodyPart);

		msg.setContent(multipart);
		Transport.send(msg);
	}

}
