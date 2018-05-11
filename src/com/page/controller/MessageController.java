package com.page.controller;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.gam.configuration.ConexionDB;

@WebServlet("/sendMessage")
public class MessageController extends HttpServlet {
	private static final long serialVersionUID = 1L;
  
    	private String tabla="company_messages";
		private String campoId="id_message";
		private String ruta="sendMessage";
		//Campos del formulario
		private String camposTabla[] = {"nombre","email","telefono","asunto","mensaje"};
		private String valuesTabla[] = {"name","email","telefono","tema","message"};
		
		
		//Por Post se reciben todas las acciones de modificacione en la DB
		protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException 
		{	
			String respuesta="Not";
			HttpSession session = request.getSession();	
			//------------------------Se valida la accion para redirigir al metodo que realiza la operacion-----------------		
			//Accion de Form principal
			String action = request.getParameter("accion"); 	

			//CREAR O ACTUALIZAR
			if(action!=null && !action.equals("")) 
			{
				//Abstraer campos del formulario		
				String campos="";
				String valores="";		
				if(action.equals("crear"))
				{		
					campos=camposTabla[0];
					for (int i=1;i<camposTabla.length;i++) 
					{
						campos += ","+camposTabla[i];
					}
					valores="'"+request.getParameter(valuesTabla[0])+"'";
					for(int i=1;i<valuesTabla.length;i++)
					{	   
						valores +=",'"+request.getParameter(valuesTabla[i])+"'";
					}
					
					if(ConexionDB.Insert(tabla, campos, valores, 0))//
					{
						//System.out.println("Imprimiendo desde GIT"); 
						respuesta="Ok";  
						String cuerpo="Has enviado un mensaje de contacto, en la menor brevedad nos pondremos en contacto contigo para solucionar tus inquietudes.";
						EmailSender.sendEmail("Gracias por Contactarnos", cuerpo, new String[]{request.getParameter("email")}, "", "");
						EmailSender.sendEmail("Tiene un nuevo mensaje de contacto", "Tiene un nuevo mensaje de contacto de su pagina Web", new String[]{(String)EmailSender.company.get(0).get("email")}, "", "");					
					}
				}	
			}	
			ServletOutputStream out = response.getOutputStream();
			out.print(respuesta);
		}
}
