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
        
        final String Host="192.168.1.76";   
        final int puerto = 5000;
        DataInputStream in;
        DataOutputStream out;
        BufferedInputStream bis;
        BufferedOutputStream bos;
        int i;
        byte[] byteArray;
        String filename;
        Scanner xleer =new Scanner(System.in);
        String mensaje="1";
        int opc=1;
        
        while(opc!=0){
            System.out.println("Escriba la opcion que desea.");
            System.out.println("1)Enviar mensaje");
            System.out.println("2)Enviar archivo");
            System.out.println("3)Recibir archivo");
            System.out.println("0)Salir");    
            
            //opc=xleer.nextInt();
            opc=Integer.parseInt(xleer.nextLine());
            
            
            
            switch(opc){
                //Para intercambio de mensajes
                case 1:
                    try {
                        //checa cuando se desea terminar la conversacion
                        while(!mensaje.equals("0")){
                            //Se crea socket para poder tener la conversacion
                            // con el IP de la computadora con laque se 
                            //mandaran mensajes y un puerto
                            Socket sc = new Socket(Host,puerto);
                            //Para poder recibir y enviar mensajes
                            in = new DataInputStream(sc.getInputStream()); 
                            out = new DataOutputStream(sc.getOutputStream());
            
                            System.out.print("Usted: ");
                            mensaje=xleer.nextLine();
                            
                            //Cuando se quiere terminar la conversación
                            if(mensaje.equals("0")){
                                out.writeUTF("0");
                                mensaje="1";
                                sc.close();
                                break;
                            }else{
                                //Manda el mensaje y lee el mensaje
                                out.writeUTF(mensaje);
                                
                                mensaje=in.readUTF();
                                //checa si el otro usuario termino la conversacion
                                if(mensaje.equals("0")){
                                    sc.close();
                                    System.out.println("El usuario ha salido.");
                                    mensaje="1";
                                    break;
                                }else
                                    System.out.println("Otro: "+mensaje);
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
                //para enviar archivo
                case 2:
                    
                    try{
                       
                        //se crea ek socket para la comunicación
                        Socket sc = new Socket(Host,puerto);
                        
                        
                        System.out.println("Escriba el nombre del archivo:");
                        filename=xleer.nextLine();
                        //obtiene el archivo con dicho nombre
                        File localFile = new File(filename);
                        //Para poder leer la informacion del archivo y enviarla
                        bis=new BufferedInputStream(new FileInputStream(localFile));
                        bos=new BufferedOutputStream(sc.getOutputStream());
                        DataOutputStream dos=new DataOutputStream(sc.getOutputStream());
                        //manda el nombre del archivo
                        dos.writeUTF(localFile.getName());
                        byteArray=new byte[8192];
                        //manda el archivo
                        while((i=bis.read(byteArray))!=-1){
                            bos.write(byteArray,0,i);
                        }
                        bis.close();
                        bos.close();
                        sc.close();
                        System.out.println("Archivo "+filename+" transferido.");
                    }catch(Exception e){
                        System.out.println("El usuario no está conectado.");
                    }
                break;
                
                //Para recibir archivos
                case 3:
                    try{
                        //Se crea el socket con la IP del principal
                        
                        Socket sc = new Socket(Host,puerto);
                        System.out.println("Transfiriendo archivo.");
                        byte[] receivedData=new byte[8192];
                            //Para leer el nombre y poder leer y escribir el archivo 
                            bis=new BufferedInputStream(sc.getInputStream());
                            in=new DataInputStream(sc.getInputStream());
                            filename=in.readUTF();
                            bos=new BufferedOutputStream(new FileOutputStream(filename));
                            //se escribe el archivo
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
