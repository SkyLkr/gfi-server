/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Data;

import java.sql.SQLException;
import java.util.List;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author acucena
 */
public class UserDAOIT {
    
    User u;
    UserDAO instance;
    public UserDAOIT() throws SQLException {
        u = new User();
        u.setId(5);
        u.setMoney(3000);
        u.setName("Raimunda");
        u.setPassword("12345");
        instance = new UserDAO();
    }

    @Test
    public void testAdd() throws Exception {
        System.out.println("Adicionar Usuário");
        instance.add(u);
    }

    @Test
    public void testGet_String() throws Exception {
        System.out.println("Seleciona usuário pelo nome");
        User teste = null;
        teste = instance.get(u.getName());
        assert(teste.getName().equals(u.getName()));
    }

    @Test
    public void testGet_long() throws Exception {
        System.out.println("Seleciona usuário pelo Id");
        User teste = null;
        teste = instance.get(u.getId());
        assert(teste.getName().equals(u.getName()));
    }
    
}
