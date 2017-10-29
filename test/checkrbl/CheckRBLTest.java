/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package checkrbl;

import java.util.List;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.Ignore;

/**
 *
 * @author utente
 */
public class CheckRBLTest {
    
    public CheckRBLTest() {
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
     * Test of main method, of class CheckRBL.
     */
    @Test
    @Ignore
    public void testMain() {
        System.out.println("main");
        String[] args = {"", ""};
        CheckRBL.main(args);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of generateDomainFromCounter method, of class CheckRBL.
     */
    @Test
    public void testGenerateDomainFromCounter() {
        System.out.println("generateDomainFromCounter");
        int from = 0;
        int to = 10;
        String prefix = "goofy";
        String suffix = ".mouse";
        String printfFormat = "02";
        CheckRBL instance = new CheckRBL();
        List<String> output=instance.generateDomainFromCounter(from, to, prefix, suffix, printfFormat);
        for (String o:output){
            System.out.println(o);
        }
        // TODO review the generated test code and remove the default call to fail.
        //fail("The test case is a prototype.");
    }
    
}
