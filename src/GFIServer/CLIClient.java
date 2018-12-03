/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package GFIServer;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.net.Socket;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Adriano
 */
public class CLIClient {
    public static void main(String[] args) {
        try {
            Scanner s = new Scanner(System.in);
            //System.out.print("Porta: ");
            int port = 25565;
            Socket client = new Socket("localhost", port);
            
            PrintStream out = new PrintStream(client.getOutputStream());
            
            Scanner in = new Scanner(client.getInputStream());
            
            String loginMsg = in.nextLine();
            System.out.println("Login: " + loginMsg);
            
            while(true) {
                // Send command to server
                System.out.print(":> ");
                String cmd = s.nextLine();
                
                if (cmd.equalsIgnoreCase("EXIT")) {
                    System.exit(0);
                } else {
                    out.println(cmd);
                }
                
                // Wait for response
                String msg = in.nextLine();
                String[] m = msg.split(":");
                switch(m[0].toUpperCase()) {
                    case "SUCCESS":
                        System.out.println(m[1]);
                        break;
                    case "ERROR":
                        System.err.println(m[1]);
                        break;
                    default:
                        System.out.println(msg);
                }
            }
        } catch (IOException ex) {
            Logger.getLogger(CLIClient.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
