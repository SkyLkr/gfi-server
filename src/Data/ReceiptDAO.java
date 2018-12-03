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
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Adriano
 */
public class ReceiptDAO {
    public Connection connection;
    
    public ReceiptDAO() throws SQLException {
        this.connection = ConnectionFactory.getConnection();
    }
    
    public void add(Receipt receipt, User user) throws SQLException {
        String sql = "INSERT INTO receipt (value, category_id, user_id, name, paid) VALUES (?,?,?,?,?)";
        PreparedStatement stmt = connection.prepareStatement(sql);
        
        stmt.setDouble(1, receipt.getValue());
        stmt.setLong(2, receipt.getCategory().getId());
        stmt.setLong(3, user.getId());
        stmt.setString(4, receipt.getName());
        stmt.setBoolean(5, receipt.isPaid());
        
        stmt.execute();
    }
    
    public List<Receipt> getList(long userId) throws SQLException{
        String sql = "SELECT * FROM receipt WHERE user_id=?";
        PreparedStatement stmt = connection.prepareStatement(sql);
        stmt.setLong(1, userId);
        ResultSet rs = stmt.executeQuery();
        
        List<Receipt> list = new ArrayList<>();
        
        while(rs.next()) {
            Receipt r = new Receipt();
            r.setId((long)rs.getInt("id"));
            r.setValue(rs.getDouble("value"));
            CategoryDAO cdao = new CategoryDAO();
            r.setCategory(cdao.get((long)rs.getInt("category_id")));
            r.setName(rs.getString("name"));
            r.setPaid(rs.getInt("paid") != 0);
            
            list.add(r);
        }
        
        stmt.close();
        rs.close();
        return list;
    }
    
    public List<Receipt> getList() throws SQLException{
        String sql = "SELECT * FROM receipt";
        PreparedStatement stmt = connection.prepareStatement(sql);
        ResultSet rs = stmt.executeQuery();
        
        List<Receipt> list = new ArrayList<>();
        
        while(rs.next()) {
            Receipt r = new Receipt();
            r.setId((long)rs.getInt("id"));
            r.setValue(rs.getDouble("value"));
            CategoryDAO cdao = new CategoryDAO();
            r.setCategory(cdao.get((long)rs.getInt("category_id")));
            r.setName(rs.getString("name"));
            r.setPaid(rs.getInt("paid") != 0);
            
            list.add(r);
        }
        
        stmt.close();
        rs.close();
        return list;
    }
    
    public Receipt get(long id) throws SQLException {
        String sql = "SELECT * FROM receipt WHERE id=?";
        PreparedStatement stmt = connection.prepareStatement(sql);
        
        stmt.setLong(1, id);
        
        ResultSet rs = stmt.executeQuery();
        
        if(rs.first()) {
            Receipt r = new Receipt();
            r.setId((long)rs.getInt("id"));
            r.setValue(rs.getDouble("value"));
            CategoryDAO cdao = new CategoryDAO();
            r.setCategory(cdao.get((long)rs.getInt("category_id")));
            r.setName(rs.getString("name"));
            r.setPaid(rs.getInt("paid") != 0);
            
            return r;
        } else {
            return null;
        }
    }
}
