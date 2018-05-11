/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gam.configuration;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.mysql.jdbc.DatabaseMetaData;
/**
 * 
 * @author Alejandro Fajardo
 * 
 * 
 */
public class ConexionDB 
{ 
    private static String usuario;
    private static int rol;	
    public static String getUsuario() {
        return usuario;
    }
    public static int getRol() {
        return rol;
    } 
    //Elementos conexion
    public static String driver="com.mysql.jdbc.Driver";
     
  //Servidor Real Jelastic
    
    public static String user="root";
    public static String password="SNMkzb69112"; 
    public static String  url="jdbc:mysql://mariadb36081-proinsoft.jl.serv.net.mx/gam";
         
    //Conexion local 
     /*
    public static String user="root"; 
    public static String password="sandman7";
    public static String  url="jdbc:mysql://localhost:3306/gam"; 
     */
  
    public static Connection conexion=null;
    public static Statement sentencia;
    public static int numFilas;

    //-------------------------------CONEXION-------------------------------
    public static boolean conectarDB()
    {		
        boolean valida=false;
        try
        {
        	Class.forName(driver);
            conexion= DriverManager.getConnection(url,user,password);
            sentencia = conexion.createStatement();
            valida=true;
        }
        catch(Exception e)
        {
            e.printStackTrace();
            conexion=null;
            valida=false;
        }
        return valida;
    }
    
    //-----------------------------------------------------------SQL CRUD---------------------------------------------------------------------
    //Consulta Que devuelve un Array de Objetos
    public static Object[][] Consulta(String campos,String tabla,String clausula,int imprimir)
    {
    	ResultSet resultado;
    	String queryCount="SELECT count(*) as numRows FROM "+tabla+" WHERE "+clausula;
        String query="SELECT "+campos+" FROM "+tabla+" WHERE "+clausula;
        if(imprimir==1)
    	{
    		System.out.println(query);
    	}
        Object[][] obj=null;
        try
        {
        	conectarDB();
            resultado=sentencia.executeQuery(queryCount);
            resultado.next();
            int numRows=resultado.getInt("numRows");
            if(numRows==0)
            {          	
            	return null;
            }
            resultado.close();
            resultado=sentencia.executeQuery(query);
            resultado.next();
            ResultSetMetaData metaData=resultado.getMetaData();
            int numColum = metaData.getColumnCount();
            obj=new Object[numRows][numColum];
            resultado=sentencia.executeQuery(query);
            int j=0;
            while (resultado.next())
            {
                for(int i=0;i<numColum;i++)
                {	
                    obj[j][i]=resultado.getObject(i+1);
                }
                j++;
            }
            resultado.close();
            conexion.close();
        }
        catch(SQLException e)
        {
            e.printStackTrace();
        }
        return obj;
    }		
    
    
    //Consulta directa de una tabla, devuelve in List<Map> con los indices de los campos de las tablas
    public static List<Map<String , Object>> Query(String tabla,String clausula,String alias,int imprimir)
    {
        String query="SELECT * FROM "+tabla+" "+alias+" WHERE "+clausula;            
        List<Map<String , Object>> list  = new ArrayList<Map<String,Object>>();
        ResultSet resultado;
        if(imprimir==1)
    	{
    		System.out.println(query);
    	}     
        try
        {
        	conectarDB();
            resultado=sentencia.executeQuery(query);
            
            DatabaseMetaData metadatos=(DatabaseMetaData) conexion.getMetaData();
        	ResultSet rsc = metadatos.getColumns(null, null, tabla, null); //Se abstraen los campos de la tabla
        	
        	//Se asignan en un Array de Strings
        	ArrayList<String> camposTabla = new ArrayList<String>();
        	while(rsc.next())
        	{
        		camposTabla.add(rsc.getString(4));
            }       	
        	//System.out.println("Campos Tabla: "+camposTabla);       	
            int j=0; //Indice del List
            while (resultado.next())
            {
            	Map<String,Object> temp = new HashMap<String,Object>();
            	int i=1;//Indice del campo
            	
            	for (String campo : camposTabla) //Se recorren los campos para asignar su respectivo valor
            	{          		
            		temp.put(campo,resultado.getObject(i)); //Se asignan los valores al Map
            		i++;
    			}
            	list.add(j,temp); //Se agrega el Map a la Lista
            	j++;          	
            	//System.out.println("Objeto unitario:"+temp);
            }
            //System.out.println("Lista de objetos:"+list);
            //resultado.close();  
            //conexion.close(); //Queda en pruebas el no cierre de la session, ya que el cierre afecta las conexion Ajax pero se debe validar si no envia error de conexiones multiples
            rsc=null;
        }
        catch(SQLException e)
        {
            e.printStackTrace();
        }
        return list;
    }
    
    //Metodo de actualizar datos en tabla
    public static boolean Update(String tabla,String cadena,String clausula,int imprimir)
    {    	
    	int valida=0;
        boolean retorna=false;
        String query="UPDATE "+tabla+" SET "+cadena+" WHERE "+clausula+";";
        if(imprimir==1)
    	{
    		System.out.println(query);
    	}
        try
        {
        	conectarDB();
            valida=sentencia.executeUpdate(query);
            if(valida==1)
            {
            	retorna=true;
            }
            conexion.close();
        }
        catch(SQLException e)
        {
                e.printStackTrace();
        }
        return retorna;
    }  
    
    //Metodo para insertar datos
    public static boolean Insert(String tabla,String campos,String cadena,int imprimir)
    {  	
    	int valida=0;
        boolean retorna=false;
        String query="INSERT INTO "+tabla+" ("+campos+") VALUES ("+cadena+");";
        if(imprimir==1)
    	{
    		System.out.println(query);
    	}
        try
        {
        	conectarDB();
            valida=sentencia.executeUpdate(query);
            if(valida==1)
            {
            	retorna=true;
            }  
            conexion.close();
        }
        catch(SQLException e)
        {
                e.printStackTrace();
        }
        return retorna;
    }
    
    //Metodo para borrar datos
    public static boolean Delete(String tabla,String clausula,int imprimir)
    {
    	int valida=0;
        boolean retorna=false;
        String query="DELETE FROM "+tabla+" WHERE "+clausula+";";
        if(imprimir==1)
    	{
    		System.out.println(query);
    	}
        try
        {
        	conectarDB();
            valida=sentencia.executeUpdate(query);
            if(valida==1)
            {
            	retorna=true;
            }
            conexion.close();
        }
        catch(SQLException e)
        {
                e.printStackTrace();
        }
        return retorna;
    }  
    
    //Guarda actividad de un usuario
    public static boolean guardaAction(Integer idusuario,String modulo,String accion,int imprimir)
	{
    	int valida=0;
        boolean retorna=false;
        String query="INSERT INTO users_logs (idusuario,modulo,accion) VALUES ('"+idusuario+"','"+modulo+"','"+accion+"');";
        if(imprimir==1)
    	{
    		System.out.println(query);
    	}
        try
        {
        	conectarDB();
            valida=sentencia.executeUpdate(query);
            if(valida==1)
            {
            	retorna=true;
            }  
            conexion.close();
        }
        catch(SQLException e)
        {
                e.printStackTrace();
        }
        return retorna;	
	} 
     
    //Funcion para consultar item por medio de Ajax,devuelve un String en formato JSON
    public static String ajaxQuery(String termino,String tabla,String nombreId,String campo1,String campo2,String clausula,int imprimir)throws Exception
    {
        String data ="";
        ResultSet resultado;
        try
        {
        	conectarDB();     	
        	String queryCount="SELECT count(*) as numRows FROM "+tabla+"  WHERE ("+campo1+" REGEXP '"+termino+"' OR "+campo2+" REGEXP '"+termino+"') AND "+clausula;
        	resultado=sentencia.executeQuery(queryCount);
        	resultado.next();
            int numRows=resultado.getInt("numRows");
            //System.out.println(numRows);
            if(numRows==0)
            {          	
            	return "[]";
            }
            String query="SELECT * FROM "+tabla+" WHERE ("+campo1+" REGEXP '"+termino+"' OR "+campo2+" REGEXP '"+termino+"') AND "+clausula;
            if(imprimir!=0)
            {
            	System.out.println(query);
            }
            resultado=sentencia.executeQuery(query);
            int i=0;
            data="[";
            while(resultado.next()) 
            {
            	  data+="{";
            	  data += "\"id\":\""+resultado.getObject(nombreId)+"\",";
	              data += "\"label\":\""+resultado.getObject(campo1)+" - "+resultado.getObject(campo2)+"\",";
	              data += "\"value\":\""+resultado.getObject(campo1)+"\""; 
	              data+="}";
	              if(numRows>1)
	              {
	            	  if(i<(numRows-1))//Para no colocar la coma en el ultimo elemento,genera Error
		              {
	            		  data+=","; 
		              }
	              }              
	              i++;
            }
            data+="]";
            resultado.close();
            conexion.close();
        }
        catch(SQLException e)
        {
            e.printStackTrace();
        }
        return data;
    }
}
