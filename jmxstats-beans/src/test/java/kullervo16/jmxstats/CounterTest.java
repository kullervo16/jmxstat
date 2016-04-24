/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package kullervo16.jmxstats;

import javax.management.InstanceAlreadyExistsException;
import javax.management.MBeanRegistrationException;
import javax.management.MalformedObjectNameException;
import javax.management.NotCompliantMBeanException;
import kullervo16.jmxstats.factories.CounterFactory;
import kullervo16.jmxstats.factories.JmxFactoryProducer;
import kullervo16.jmxstats.impl.Counter;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author jef
 */
public class CounterTest {
    
    public CounterTest() {
    }
    
    

    /**
     * Test of increment method, of class Counter.
     */
    @org.junit.Test
    public void testIncrement_0args() {
        System.out.println("increment");
        Counter instance = new Counter();
        
        Counter result = instance.increment();
        assertEquals(1, result.getValue());  
        result = instance.increment();
        assertEquals(2, result.getValue()); 
    }

    /**
     * Test of increment method, of class Counter.
     */
    @org.junit.Test
    public void testIncrement_int() {
        System.out.println("increment");        
        Counter instance = new Counter();
        
        Counter result = instance.increment(42);
        assertEquals(42, result.getValue());
        result = instance.increment(666);
        assertEquals(42+666, result.getValue());
        
    }
    
    @org.junit.Test
    public void testCreateFactory() {        
        assertNotNull(new JmxFactoryProducer().getCounterFactory());        
    }
    
    @org.junit.Test
    public void testCreateCounterViaFactory()  {      
        CounterFactory factory = new JmxFactoryProducer().getCounterFactory();
        Counter c1 = (Counter) factory.createCounter();
        Counter c2 = (Counter) factory.createCounter();
        assertNotNull(c1); 
        c1.increment(60);
        assertNotNull(c2);
        c2.increment();
        assertFalse(c1.equals(c2));   
        assertNotEquals(c1.getValue(), c2.getValue());
        
        // now with jmx counters
        c1 = (Counter) factory.getJmxCounter("type=test");
        c2 = (Counter) factory.getJmxCounter("type=test");
        assertNotNull(c1);  
        c1.increment(60);
        assertNotNull(c2);
        assertTrue(c1.equals(c2)); 
        assertEquals(60, c2.getValue());
        
        try {
            factory.getJmxCounter("invalid");
            fail("Should trow invalidargument");
        }catch(IllegalArgumentException iae) {
            
        }
    }
        
}
