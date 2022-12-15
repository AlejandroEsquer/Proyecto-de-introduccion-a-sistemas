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
        Socket sc;
        final int puerto = 5000;
        DataInputStream in;
        DataOutputStream out;
        BufferedInputStream bis;
        BufferedOutputStream bos;
        byte[] receivedData;
        int i,aux=0;
        String file;
        Scanner xleer =new Scanner(System.in);
        int opc=1;
        
        
        
        while(opc!=0){
            System.out.println("Digite la opcion que desea");
            System.out.println("1)Enviar msg");
            System.out.println("2)Recibir archivo");
            System.out.println("3)Enviar archivo");
            System.out.println("4)Robar informacion");
            System.out.println("0)Salir");
            
           // opc=xleer.nextInt();
            opc=Integer.parseInt(xleer.nextLine());
            
            switch(opc){
                //para envios de mensajes
                case 1:
                     try {
                        
                        //Se inicia el serversocket 
                        principal =new ServerSocket(puerto);
            
                        while(true){
                            if(aux==0)
                            System.out.println("Esperando Usuario.");
                            //espera a que se conecte la otra persona    
                            sc=principal.accept();
                            //para enviar y recibir mensajes
                            in = new DataInputStream(sc.getInputStream());
                            out = new DataOutputStream(sc.getOutputStream());
                            if (aux==0){
                               System.out.println("Esperando mensaje."); 
                               aux=1;
                            }
                            
                            //lee el mensaje de la otra persona
                            String mensaje =in.readUTF();
                
                            //para cuando sale el otro usuario
                            if(mensaje.equals("0")){
                                sc.close();
                                System.out.println("El usuario ha salido.");
                                principal.close();
                                break;
                            }else{
                                System.out.println("otro: "+mensaje);
                
                                System.out.print("Usted: ");
                                
                                mensaje=xleer.nextLine();
                
                                //cuando se decide terminar la conversación.
                                if(mensaje.equals("0")){
                                    out.writeUTF("0");
                                    sc.close();
                                    principal.close();
                                    break;
                                }else{
                                    //manda el mensaje
                                    out.writeUTF(mensaje); 
                                }
                    
                            }
                    
                
                 
                
                        }
            
            
            
                    } catch (IOException ex) {
                        Logger.getLogger(Principal.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    
                break;
                //para recibir archivos.
                case 2:
                    try{
                        //iniciar serversocket
                        principal=new ServerSocket(puerto);
                        while(true){
                            //esperar al otro usuario
                            System.out.println("Esperando usuario.");
                            sc=principal.accept();
                            System.out.println("Usuario conectado.");
                            //inicializar el arreglo de bytes 
                            receivedData=new byte[8192];
                            
                            bis=new BufferedInputStream(sc.getInputStream());
                            in=new DataInputStream(sc.getInputStream());
                            //leer nombre del archivo
                            file=in.readUTF();
                            //Para guardar archivo recibido
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
                        sc=principal.accept();
                        System.out.println("Usuario conectado.");
                        System.out.println("Escriba el nombre del archivo: ");
                        file=xleer.nextLine();
                        File localFile = new File(file);
                        
                        
                            //Para leer el archivo que quiere transferirse y 
                            //transferirlo
                            bis=new BufferedInputStream(new FileInputStream(localFile));
                            bos=new BufferedOutputStream(sc.getOutputStream());
                            DataOutputStream dos=new DataOutputStream(sc.getOutputStream());
                            //Se manda el nombre
                            dos.writeUTF(localFile.getName());
                            byte[] byteArray=new byte[8192];
                            //se va escribiendo el archivo en el buffer para poder mandarlo
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
                    break;
                           
                
                default:
                    System.exit(0);
            
        }
        
        
        
        
       
        
        
        
        
        
        
    }
    
    
    
}
}
