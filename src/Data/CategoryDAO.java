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
public class CategoryDAO {
    private Connection connection;
    
    public CategoryDAO() throws SQLException {
        this.connection = ConnectionFactory.getConnection();
    }
    
    public void add(Category category) throws SQLException {
        String sql = "INSERT INTO category (name) values (?)";
        PreparedStatement stmt = connection.prepareStatement(sql);
        
        stmt.setString(1, category.getName());
        
        stmt.execute();
        stmt.close();
    }
    
    public List<Category> getList() throws SQLException {
        String sql = "SELECT * FROM category";
        PreparedStatement stmt = connection.prepareStatement(sql);
        ResultSet rs = stmt.executeQuery();
        
        List<Category> list = new ArrayList<>();
        
        while(rs.next()) {
            Category cat = new Category();
            cat.setId((long)rs.getInt("id"));
            cat.setName(rs.getString("name"));
            
            list.add(cat);
        }
        
        stmt.close();
        rs.close();
        return list;
    }
    
    public Category search(String name) throws SQLException {
        String sql = "SELECT * FROM category WHERE name=?";
        PreparedStatement stmt = connection.prepareStatement(sql);
        
        stmt.setString(1, name);
        
        ResultSet rs = stmt.executeQuery();
        if (rs.first()) {
            Category cat = new Category();
            cat.setId((long)rs.getInt("id"));
            cat.setName(rs.getString("name"));
            return cat;
        } else {
            return null;
        }
    }
    
    public Category get(long id) throws SQLException {
        String sql = "SELECT * FROM category WHERE id=?";
        PreparedStatement stmt = connection.prepareStatement(sql);
        
        stmt.setLong(1, id);
        
        ResultSet rs = stmt.executeQuery();
        if (rs.first()) {
            Category cat = new Category();
            cat.setId((long)rs.getInt("id"));
            cat.setName(rs.getString("name"));
            return cat;
        } else {
            return null;
        }
    }
}
