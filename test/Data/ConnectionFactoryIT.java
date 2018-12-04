/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Data;

import java.sql.Connection;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author acucena
 */
public class ConnectionFactoryIT{

    @Test
    public void testGetConnection() throws Exception {
        Connection con = ConnectionFactory.getConnection();
        assert(con != null);
    }
    
}
