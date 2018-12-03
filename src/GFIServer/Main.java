/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package GFIServer;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;

/**
 *
 * @author Adriano
 */
public class Main {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        try {
            //int port = Integer.parseInt(JOptionPane.showInputDialog("Número da porta:"));
            int port = 25565;
            ServerSocket server = new ServerSocket(port);
            System.out.println("Servidor rodando na porta " + port);
            
            while(true) {
                Socket client = server.accept();
                System.out.println("Conectado com o cliente " + client.getInetAddress());
                ConnectionHandler con = new ConnectionHandler(client);
                Thread t = new Thread(con);
                t.start();
            }
        } catch (IOException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
}
