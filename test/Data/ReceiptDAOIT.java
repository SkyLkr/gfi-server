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
public class ReceiptDAOIT {
    
    Receipt c;
    ReceiptDAO instance;
    User u;
    
    public ReceiptDAOIT() throws SQLException {
        c = new Receipt();
        instance = new ReceiptDAO();
        UserDAO udao = new UserDAO();
        u = udao.get(1);
        c.setId(5);
        CategoryDAO cdao = new CategoryDAO();
        c.setCategory(cdao.get(1));
        c.setName("Jantar");
        c.setValue(300.00);
        c.setPaid(true);
    }

    @Test
    public void testAdd() throws Exception {
        System.out.println("Add receita a lista do usuário");
        instance.add(c, u);
    }

    @Test
    public void testGetList_long() throws Exception {
        System.out.println("Obter Lista de Receitas do usuário");
        List<Receipt> teste = null;
        teste = instance.getList(u.getId());
        assert(teste != null);
    }

    @Test
    public void testGetList() throws Exception {
        System.out.println("Obter Lista de Receitas");
        List<Receipt> teste = null;
        teste = instance.getList();
        assert(teste != null);
    }

    @Test
    public void testGet() throws Exception {
        System.out.println("Obter Receita");
        Receipt r;
        r = instance.get(c.getId());
        assert(r.getName().equals(c.getName()));
    }
    
}
