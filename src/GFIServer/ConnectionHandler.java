/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package GFIServer;

import Data.Category;
import Data.CategoryDAO;
import Data.Receipt;
import Data.ReceiptDAO;
import Data.User;
import Data.UserDAO;
import Exceptions.InvalidCommandException;
import java.io.IOException;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.sql.SQLException;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Adriano
 */
public class ConnectionHandler implements Runnable {
    
    private final Socket client;
    private User user;
    private PrintStream out;
    
    public ConnectionHandler(Socket client) {
        this.client = client;
    }

    @Override
    public void run() {
        try {
            Scanner s = new Scanner(client.getInputStream());
            out = new PrintStream(client.getOutputStream());
            //out = System.out;
            out.println("bem bindo");
            
            while(s.hasNextLine()) {
                String command = s.nextLine();
                System.out.println(client.getInetAddress() + "> " + command);
                runCommand(command);
            }
        } catch (IOException ex) {
            Logger.getLogger(ConnectionHandler.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InvalidCommandException ex) {
            // TODO: Send message to client
            out.println("ERROR:" + ex.getMessage());
            //Logger.getLogger(ConnectionHandler.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    private void runCommand(String command) throws InvalidCommandException {
        try {
            String[] cmd = command.split(" ");
            String op = cmd[0];
            switch(op.toUpperCase()) {
                case "ADD":
                    String type = cmd[1];
                    double valueFactor;
                    if (type.equalsIgnoreCase("EXPENSE")) {
                        valueFactor = -1;
                    } else if (type.equalsIgnoreCase("INCOME")) {
                        valueFactor = 1;
                    } else {
                        throw new InvalidCommandException();
                    }
                    
                    double value = Double.parseDouble(cmd[2]) * valueFactor;
                    long categoryId = Long.parseLong(cmd[3]);
                    CategoryDAO cdao = new CategoryDAO();
                    Category cat = cdao.get(categoryId);
                    String name = cmd[4];
                    boolean paid = Boolean.parseBoolean(cmd[5]);
                    
                    Receipt receipt = new Receipt();
                    receipt.setName(name);
                    receipt.setValue(value);
                    receipt.setCategory(cat);
                    receipt.setPaid(paid);
                    
                    ReceiptDAO rdao = new ReceiptDAO();
                    rdao.add(receipt, this.user);
                    
                    out.println("SUCESS:Cadastrado com sucesso!");
                    break;
                    
                case "USER":
                    String username = cmd[1];
                    String password = "";
                    if (cmd[2].equalsIgnoreCase("PASSWORD")) {
                        password = cmd[3];
                    } else {
                        throw new InvalidCommandException();
                    }
                    
                    UserDAO udao = new UserDAO();
                    
                    User user = udao.get(username);
                    
                    if (user != null) {
                        if (user.getPassword().equals(password)) {
                            this.user = user;
                            out.println("SUCCESS:Loged in successfuly.");
                        } else {
                            out.println("ERROR:Incorrect Password");
                        }
                    } else {
                        out.println("ERROR:User don\'t exist");
                    }
                    
                    break;
                default:
                    throw new InvalidCommandException();
            }
        } catch (SQLException ex) {
            Logger.getLogger(ConnectionHandler.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
