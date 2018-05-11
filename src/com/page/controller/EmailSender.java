package com.page.controller;

import java.util.List;
import java.util.Map;
import java.util.Properties;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import com.gam.configuration.ConexionDB;
 
public class EmailSender 
{
	private final static Properties properties = new Properties();	
	
	private static String password;
	static List<Map<String, Object>> company = ConexionDB.Query("company", "id_company='1'", "", 0);
	private static Session session;
	private static void init(String remitente) 
	{
		String user="";
		if(remitente.equals(""))
		{
			user=(String) company.get(0).get("email");
		}
		else
		{
			user=remitente;
		}	
		
		properties.put("mail.smtp.host", "smtp.gmail.com");
		properties.put("mail.smtp.starttls.enable", "true");
		properties.put("mail.smtp.port",25);
		properties.put("mail.smtp.mail.sender",user);
		properties.put("mail.smtp.user", user);
		properties.put("mail.smtp.auth", "true");

		session = Session.getDefaultInstance(properties);
	}
	public static boolean sendEmail(String asunto,String cuerpo,String[] destinatarios,String remitente,String passwordLocal)
	{		
		if(passwordLocal.equals(""))
		{
			password=(String) company.get(0).get("passEmail");
			password=Desencripta(password);
		}
		else
		{
			password=passwordLocal;
		}		
		init(remitente);
		try
		{
			int lenght=destinatarios.length;
			InternetAddress[] internetAddresses = new InternetAddress[lenght];
			int i=0;
			for (String destinatario : destinatarios)
			{
				internetAddresses[i]=new InternetAddress(destinatario);
				i++;
			}	
			
			MimeMessage message = new MimeMessage(session);
			message.setFrom(new InternetAddress((String)properties.get("mail.smtp.mail.sender")));		
			message.setRecipients(Message.RecipientType.TO,internetAddresses);	// Agregar los destinatarios al mensaje	
			message.setSubject(asunto);
			message.setText(cuerpo); 
			Transport t = session.getTransport("smtp");
			t.connect((String)properties.get("mail.smtp.user"), password); 
			t.sendMessage(message, message.getAllRecipients());
			t.close();
			return true;
		}
		catch (MessagingException me)
		{
            System.out.println("Error!! "+me); 
			return false;
		}		
	}
	
	public static String Encripta(String mensaje)
	{ 
        char array[]=mensaje.toCharArray(); 
         
        for(int i=0;i<array.length;i++)
        { 
            array[i]=(char)(array[i]+(char)5); 
        } 
        String encriptado =String.valueOf(array); 
		return  encriptado;
	}
	
	public static String Desencripta(String encriptado)
	{
		char arrayD[]=encriptado.toCharArray(); 
        for(int i=0;i<arrayD.length;i++)
        { 
            arrayD[i]=(char)(arrayD[i]-(char)5); 
        } 
        String desencriptado =String.valueOf(arrayD); 
        return desencriptado;
	}
}
