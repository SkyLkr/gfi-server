/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Data;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

/**
 *
 * @author Adriano
 */
public class UserDAO {
    private Connection connection;
    
    public UserDAO() throws SQLException {
        this.connection = ConnectionFactory.getConnection();
    }
    
    public void add(User user) throws SQLException {
        String sql = "INSERT INTO user (name, password, money) values (?,?,?)";
        PreparedStatement stmt = connection.prepareStatement(sql);
        
        stmt.setString(1, user.getName());
        stmt.setString(2, user.getPassword());
        stmt.setDouble(3, user.getMoney());
        
        stmt.execute();
        stmt.close();
    }
    
    public User get(String name) throws SQLException {
        String sql = "SELECT * FROM user WHERE name=?";
        PreparedStatement stmt = connection.prepareStatement(sql);
        
        stmt.setString(1, name);
        
        ResultSet rs = stmt.executeQuery();
        if (rs.first()) {
            User user = new User();
            user.setId((long)rs.getInt("id"));
            user.setName(rs.getString("name"));
            user.setPassword(rs.getString("password"));
            user.setMoney(rs.getDouble("money"));
            
            ReceiptDAO rdao = new ReceiptDAO();
            List<Receipt> r = rdao.getList(user.getId());
            
            user.setReceipts(r);
            
            return user;
        } else {
            return null;
        }
    }
    
    public User get(long id) throws SQLException {
        String sql = "SELECT * FROM user WHERE id=?";
        PreparedStatement stmt = connection.prepareStatement(sql);
        
        stmt.setLong(1, id);
        
        ResultSet rs = stmt.executeQuery();
        if (rs.first()) {
            User user = new User();
            user.setId((long)rs.getInt("id"));
            user.setName(rs.getString("name"));
            user.setPassword(rs.getString("password"));
            user.setMoney(rs.getDouble("money"));
            
            ReceiptDAO rdao = new ReceiptDAO();
            List<Receipt> r = rdao.getList(user.getId());
            
            user.setReceipts(r);
            
            return user;
        } else {
            System.err.println("Usuario nao encontrado");
            return null;
        }
    }
}
