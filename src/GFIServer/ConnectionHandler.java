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
import java.util.List;
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
            String[] cmd = command.split(":");
            String op = cmd[0];
            
            UserDAO udao;
            ReceiptDAO rdao;
            
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
                    
                    rdao = new ReceiptDAO();
                    rdao.add(receipt, this.user);
                    
                    out.println("SUCCESS:Cadastrado com sucesso!");
                    break;
                    
                case "GET":
                    udao = new UserDAO();
                    this.user = udao.get(this.user.getId());
                    if(cmd[1].equalsIgnoreCase("SALDO")) {
                        out.println(this.user.getMoney());
                    } else if(cmd[1].equalsIgnoreCase("RECEBIDOS")) {
                        double recebidos = 0;
                        for (Receipt r : user.getReceipts()) {
                            if (r.getValue() >= 0) {
                                recebidos += r.getValue();
                            }
                        }
                        out.println(recebidos);
                    } else if(cmd[1].equalsIgnoreCase("DESPESAS")) {
                        double despesas = 0;
                        for (Receipt r : user.getReceipts()) {
                            if (r.getValue() < 0) {
                                despesas += r.getValue();
                            }
                        }
                        out.println(Math.abs(despesas));
                    } else if(cmd[1].equalsIgnoreCase("%")) {
                        rdao = new ReceiptDAO();
                        List<Receipt> receipts = rdao.getList(user.getId());
                        
                        int[] percent = new int[] {0,0,0,0};
                        int total = receipts.size();
                        
                        for (Receipt r: receipts) {
                            percent[(int)r.getCategory().getId()-1]++;
                        }
                        
                        StringBuilder sb = new StringBuilder();
                        
                        for (int i = 0; i < percent.length; i++) {
                            percent[i] *= 100;
                            percent[i] /= total;
                            
                            sb.append(Integer.toString(percent[i]));
                            sb.append(";");
                        }
                        
                        out.println(sb.toString());
                        
                    }
                    
                    break;
                    
                case "LOGIN":
                    String username = cmd[1];
                    String password = cmd[2];
                    
                    udao = new UserDAO();
                    
                    User user = udao.get(username);
                    
                    if (user != null) {
                        if (user.getPassword().equals(password)) {
                            this.user = user;
                            out.println("SUCCESS:Logado com sucesso.");
                        } else {
                            out.println("ERROR:Senha incorreta.");
                        }
                    } else {
                        out.println("ERROR:Usuário não cadastrado.");
                    }
                    
                    break;
                    
                case "CREATEUSER":
                    String uname = cmd[1];
                    String pass = cmd[2];
                    udao = new UserDAO();
                    
                    User u = udao.get(uname);
                    
                    if (u == null) {
                        u = new User();
                        u.setName(uname);
                        u.setPassword(pass);
                        udao.add(u);
                        out.println("SUCCESS:Usuário cadastrado com sucesso.");
                    } else {
                        out.println("ERROR:Usuário já está cadastrado.");
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
