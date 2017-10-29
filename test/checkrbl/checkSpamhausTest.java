/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package checkrbl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author utente
 */
public class checkSpamhausTest {
    
    public checkSpamhausTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
    }
    
    @After
    public void tearDown() {
    }

    /**
     * Test of checkDomain method, of class checkSpamhaus.
     */
    @Test
    public void testCheckDomain() {
        System.out.println("checkDomain");
        String domain = "mailagenziaentrate05.top";
        CheckSpamhaus instance = new CheckSpamhaus();
        String expResult = "";
        String result = instance.checkDomain(domain);
        System.out.print(domain+" - ");
        System.out.println("Result:"+result);
        assertNotEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        //fail("The test case is a prototype.");
    }

    /**
     * Test of checkIP method, of class checkSpamhaus.
     */
    @Test
    public void testCheckIP() {
        System.out.println("checkIP");
        String ip = "47.91.145.170";
        CheckSpamhaus instance = new CheckSpamhaus();
        String expResult = "";
        String result = instance.checkIp(ip);
        System.out.print(ip+" - ");
        System.out.println("Result:"+result);
        //List<String> lista = Arrays.asList("82.202.248.130", "mailagenziaentrate00.top", "mailagenziaentrate05.top", "47.91.145.170");
        List<String> lista = new ArrayList<>();
        for (int i=0;i<66;i++){
            lista.add(String.format("mailagenziaentrate%02d.top", i));            
        }
        List<String> risultati = instance.checkResources(lista, null);
        for (int i=0;i<lista.size();i++){
            System.out.println(String.format("%s - %s", lista.get(i),risultati.get(i)));
        }
                
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        //fail("The test case is a prototype.");
    }
    
}
