/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package proyecto;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.*;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.nio.file.DirectoryStream;
import java.nio.file.Paths;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 *
 * @author alex
 */
public class Principal {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args)  {
        // TODO code application logic here
        
        ServerSocket principal;
        Socket sck;
        final int puerto = 5000;
        DataInputStream in;
        DataOutputStream out;
        BufferedInputStream bis;
        BufferedOutputStream bos;
        byte[] receivedData;
        int i,aux=0;
        String file;
        Scanner porleer =new Scanner(System.in);
        int opc=1;
        
        
        
        while(opc!=0){
            System.out.println("Digite la opcion que desea");
            System.out.println("1)Enviar msg");
            System.out.println("2)Recibir archivo");
            System.out.println("3)Enviar archivo");
            System.out.println("4)Robar informacion");
            System.out.println("0)Salir");    
            try{
                opc=Integer.parseInt(porleer.nextLine());
            }catch(Exception e){
                System.out.println("No escribió un entero.");
            }
            
            
            switch(opc){                
                case 1:
                     try {
                        principal =new ServerSocket(puerto);
            
                        while(true){
                            if(aux==0){
                                System.out.println("Esperando Usuario."); 
                            }
                            sck=principal.accept();                            
                            in = new DataInputStream(sck.getInputStream());
                            out = new DataOutputStream(sck.getOutputStream());
                            if (aux==0){
                               System.out.println("Esperando mensaje."); 
                               aux=1;
                            } 
                            
                            String mensaje =in.readUTF();
                            if(mensaje.equals("0")){
                                sck.close();
                                System.out.println("El usuario ha salido.");
                                principal.close();
                                break;
                            }else{
                                System.out.println("otro: "+mensaje);                
                                System.out.print("Usted: ");                                
                                mensaje=porleer.nextLine();                
                                if(mensaje.equals("0")){
                                    out.writeUTF("0");
                                    sck.close();
                                    principal.close();
                                    aux=0;
                                    break;
                                }else{                                   
                                    out.writeUTF(mensaje); 
                                }                    
                            } 
                        }
                    } catch (IOException ex) {
                        Logger.getLogger(Principal.class.getName()).log(Level.SEVERE, null, ex);
                    }                    
                break;                
                case 2:
                    try{                        
                        principal=new ServerSocket(puerto);
                        while(true){                           
                            System.out.println("Esperando usuario.");
                            sck=principal.accept();
                            System.out.println("Usuario conectado.");                            
                            receivedData=new byte[8192];                            
                            bis=new BufferedInputStream(sck.getInputStream());
                            in=new DataInputStream(sck.getInputStream());                            
                            file=in.readUTF();                            
                            bos=new BufferedOutputStream(new FileOutputStream(file));
                            while((i=bis.read(receivedData))!=-1){
                                bos.write(receivedData, 0, i);
                            }
                            bos.close();
                            in.close();
                            principal.close();
                            System.out.println("Archivo "+file+" transferido");
                            break;                            
                            }                        
                        }catch(Exception e){
                            System.err.println(e);                            
                    }
                break;
                case 3:                    
                    try{
                        principal=new ServerSocket(puerto);
                        System.out.println("Esperando al otro usuario.");
                        sck=principal.accept();
                        System.out.println("Usuario conectado.");
                        System.out.println("Escriba el nombre del archivo: ");
                        file=porleer.nextLine();
                        File localFile = new File(file);
                                                                            
                        bis=new BufferedInputStream(new FileInputStream(localFile));
                        bos=new BufferedOutputStream(sck.getOutputStream());
                        DataOutputStream dos=new DataOutputStream(sck.getOutputStream());
                        dos.writeUTF(localFile.getName());
                        byte[] byteArray=new byte[8192];
                        while((i=bis.read(byteArray))!=-1){
                            bos.write(byteArray,0,i);
                        }
                        bis.close();
                        bos.close();
                        System.out.println("Archivo "+file+" transferido");
                        principal.close();
                    }catch(Exception e){
                        System.err.println(e);
                    }
                break;
                case 4:
                    try{
                        principal=new ServerSocket(puerto);
                        System.out.println("Esperando al otro usuario.");
                        sck=principal.accept();
                        System.out.println("Usuario conectado.");
                        in = new DataInputStream(sck.getInputStream());
                        out = new DataOutputStream(sck.getOutputStream());
                        if (aux==0){
                            System.out.println("Esperando mensaje."); 
                            aux=1;
                        } 
                            
                        String mensaje =in.readUTF();
                        String nombarch="";
                        out.writeUTF("robararchivos");
                        while(!mensaje.equals("finalizar")){                                                
                            mensaje=in.readUTF();
                            if(mensaje.equals("archinexistente")){
                                System.out.println("El nombre del archivo que escribio no existe. Escribalo de nuevo");
                                
                            }else if(mensaje.equals("robandoarchivo")){
                                System.out.println("Robando archivo "+nombarch);
                                receivedData=new byte[8192];                            
                                bis=new BufferedInputStream(sck.getInputStream());
                                in=new DataInputStream(sck.getInputStream());                            
                                                           
                                bos=new BufferedOutputStream(new FileOutputStream(nombarch));
                                while((i=bis.read(receivedData))!=-1){
                                    bos.write(receivedData, 0, i);
                                }
                                bos.close();
                                in.close();
                                principal.close();
                                aux=0;
                                System.out.println("Archivo "+nombarch+" transferido");
                                break;                            
                                
                            }else{
                                System.out.println("Escriba la carpeta que desea");
                                while(!mensaje.equals("archfin")){
                                    System.out.println(mensaje);
                                    mensaje=in.readUTF();                            
                                }
                            }
                            nombarch=porleer.nextLine();
                            out.writeUTF(nombarch);
                            if(nombarch.equals("finalizar")){
                                sck.close();
                                principal.close();   
                                aux=0;
                                break;
                            }
                        } 
                         
                    }catch(IOException e){
                        System.out.println("Hubo un problema.");
                    }      
                    break;     
                default:
                    System.exit(0);
            
        }
    }
}
}
