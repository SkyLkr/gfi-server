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
public class CategoryDAOIT {
    
    Category c;
    CategoryDAO instance;
    
    public CategoryDAOIT() throws SQLException{
        c = new Category();
        c.setId(12);
        c.setName("Alimentação");
        instance = new CategoryDAO();
    }

    @Test
    public void testAdd() throws Exception {
        System.out.println("Adicionar Categoria");
        instance.add(c);
    }

    @Test
    public void testGetList() throws Exception {
        System.out.println("Obter Lista de Categorias");
        List<Category> teste = null;
        teste = instance.getList();
        assert(teste != null);
    }

    @Test
    public void testSearch() throws Exception {
        System.out.println("Busca");
        Category r;
        r = instance.search(c.getName());
        assert(r.getName().equals(c.getName()));
        r = instance.search("Não Existe");
        assert(r == null);
    }

    @Test
    public void testGet() throws Exception {
        System.out.println("Obter Categoria");
        Category r;
        r = instance.get(c.getId());
        assert(r.getName().equals(c.getName()));
    }
    
}
