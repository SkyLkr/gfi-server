/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package GFIServer;

import java.io.IOException;
import java.net.Socket;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

/**
 *
 * @author Adriano
 */
public class ConnectionHandler implements Runnable {
    
    private final Socket client;
    
    public ConnectionHandler(Socket client) {
        this.client = client;
    }

    @Override
    public void run() {
        try {
            Scanner s = new Scanner(client.getInputStream());
            
            while(s.hasNextLine()) {
                String command = s.nextLine();
                System.out.println(client.getInetAddress() + "> " + command);
                runCommand(command);
            }
        } catch (IOException ex) {
            Logger.getLogger(ConnectionHandler.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    private void runCommand(String command) {
        String[] cmd = command.split(" ");
        String op = cmd[0];
        
    }
}
