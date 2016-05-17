/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package kullervo16.jmxstats;

import java.lang.management.ManagementFactory;
import javax.management.InstanceNotFoundException;
import javax.management.IntrospectionException;
import javax.management.MBeanAttributeInfo;
import javax.management.MBeanInfo;
import javax.management.MBeanServer;
import javax.management.MalformedObjectNameException;
import javax.management.ObjectName;
import javax.management.ReflectionException;
import kullervo16.jmxstats.api.Counter;
import kullervo16.jmxstats.factories.CounterFactory;
import kullervo16.jmxstats.factories.JmxFactoryProducer;
import kullervo16.jmxstats.impl.CounterImpl;
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
        CounterImpl instance = new CounterImpl(null);
        
        CounterImpl result = instance.increment();
        assertEquals(1, result.getValue());  
        result = instance.increment();
        assertEquals(2, result.getValue()); 
        
        assertNotNull(result.toString());
    }

    /**
     * Test of increment method, of class Counter.
     */
    @org.junit.Test
    public void testIncrement_int() {
        CounterImpl instance = new CounterImpl(null);
        
        CounterImpl result = instance.increment(42);
        assertEquals(42, result.getValue());
        result = instance.increment(666);
        assertEquals(42+666, result.getValue());
                        
    }

    @org.junit.Test
    public void testReset() {
        CounterImpl instance = new CounterImpl(null);
        
        CounterImpl result = instance.increment();
        assertEquals(1, result.getValue());  
        result = instance.increment();
        assertEquals(2, result.getValue());
        result = instance.reset();
        assertEquals(0, result.getValue());
    }
    
    @org.junit.Test
    public void testReadAndReset() {
        CounterImpl instance = new CounterImpl(null);
        
        CounterImpl result = instance.increment(42);
        assertEquals(42, result.readValueAndReset());          
        assertEquals(0, result.getValue()); 
    }

    
    @org.junit.Test
    public void testCreateFactory() {        
        assertNotNull(new JmxFactoryProducer().getCounterFactory());        
    }
    
    @org.junit.Test
    public void testCreateCounterViaFactory()  {      
        CounterFactory factory = new JmxFactoryProducer().getCounterFactory();
        Counter c1 = factory.createCounter();
        Counter c2 = factory.createCounter();
        assertNotNull(c1); 
        c1.increment(60);
        assertNotNull(c2);
        c2.increment();
        assertFalse(c1.equals(c2));   
        assertNotEquals(c1.getValue(), c2.getValue());
        
        // now with jmx counters
        c1 = factory.getJmxCounter("type=test");
        c1.setDescription("Description 1");
        c2 = factory.getJmxCounter("type=test");
        assertNotNull(c1);  
        c1.increment(60);
        assertNotNull(c2);
        assertTrue(c1.equals(c2)); 
        assertEquals(60, c2.getValue());
        assertEquals("Description 1", c1.getDescription());
        
        try {
            factory.getJmxCounter("invalid");
            fail("Should trow invalidargument");
        }catch(IllegalArgumentException iae) {
            
        }
    }
    
    @org.junit.Test
    public void testUndeployCounterCleanup() throws MalformedObjectNameException, InstanceNotFoundException, IntrospectionException, ReflectionException  {
        CounterFactory factory = new JmxFactoryProducer().getCounterFactory();
        Counter c1 =  factory.getJmxCounter(CLEANUP_TEST1);
        Counter c2 =  factory.getJmxCounter(CLEANUP_TEST2);
                        
        MBeanServer mbs = ManagementFactory.getPlatformMBeanServer();
        ObjectName mxbeanName1 = new ObjectName(factory.getPrefix()+CLEANUP_TEST1);        
        assertNotNull(mbs.getMBeanInfo(mxbeanName1));
        
        ObjectName mxbeanName2 = new ObjectName(factory.getPrefix()+CLEANUP_TEST2);        
        assertNotNull(mbs.getMBeanInfo(mxbeanName2));
        
        factory.unregisterBeans();
        try {
            mbs.getMBeanInfo(mxbeanName1);
            fail("Instance1 found that should be gone");
        } catch(InstanceNotFoundException infe) {
            try {
                mbs.getMBeanInfo(mxbeanName2);
                fail("Instance2 found that should be gone");
            } catch(InstanceNotFoundException infe2) {
                return; // ok
            }
        }
        fail("Wrong exceptions thrown");
    }
    
    @org.junit.Test
    public void testCounterCleanup() throws MalformedObjectNameException, InstanceNotFoundException, IntrospectionException, ReflectionException  {
        CounterFactory factory = new JmxFactoryProducer().getCounterFactory();
        Counter c1 = factory.getJmxCounter(CLEANUP_TEST1);
        assertEquals(CLEANUP_TEST1, c1.getId());
        Counter c2 = factory.getJmxCounter(CLEANUP_TEST2);
        
        MBeanServer mbs = ManagementFactory.getPlatformMBeanServer();
        ObjectName mxbeanName1 = new ObjectName(factory.getPrefix()+CLEANUP_TEST1);        
        assertNotNull(mbs.getMBeanInfo(mxbeanName1));
        
        ObjectName mxbeanName2 = new ObjectName(factory.getPrefix()+CLEANUP_TEST2);        
        assertNotNull(mbs.getMBeanInfo(mxbeanName2));
        
        factory.unregisterCounter(CLEANUP_TEST2);
        try {
            mbs.getMBeanInfo(mxbeanName2);
            fail("Instance1 found that should be gone");
        } catch(InstanceNotFoundException infe) {
            assertNotNull(mbs.getMBeanInfo(mxbeanName1));
            return; //ok
        }
        fail("Wrong exceptions thrown");
    }
    
    @org.junit.Test
    public void testDescription() {
        CounterFactory factory = new JmxFactoryProducer().getCounterFactory();
        Counter c1 = factory.getJmxCounter(CLEANUP_TEST1);
        c1.setDescription("My description");
        try {
            MBeanServer mbs = ManagementFactory.getPlatformMBeanServer();
            ObjectName mxbeanName1 = new ObjectName(factory.getPrefix()+CLEANUP_TEST1);        
            MBeanInfo info = mbs.getMBeanInfo(mxbeanName1);
            for(MBeanAttributeInfo attr : info.getAttributes()) {
                if(attr.getName().equals("Description")) {
                    assertEquals("My description",mbs.getAttribute(mxbeanName1, attr.getName()));
                    return;
                }
            }
            fail("Description not found");
        }catch(Exception e) {
            fail(e.getMessage());
        }
    }
    
    @org.junit.Test
    public void testUnit() {
        CounterFactory factory = new JmxFactoryProducer().getCounterFactory();
        Counter c1 =  factory.getJmxCounter(CLEANUP_TEST1);
        c1.setUnit("flurps per millisecond");
        try {
            MBeanServer mbs = ManagementFactory.getPlatformMBeanServer();
            ObjectName mxbeanName1 = new ObjectName(factory.getPrefix()+CLEANUP_TEST1);        
            MBeanInfo info = mbs.getMBeanInfo(mxbeanName1);
            for(MBeanAttributeInfo attr : info.getAttributes()) {
                if(attr.getName().equals("Unit")) {
                    assertEquals("flurps per millisecond",mbs.getAttribute(mxbeanName1, attr.getName()));
                    return;
                }
            }
            fail("Unit not found");
        }catch(Exception e) {
            fail(e.getMessage());
        }
    }
    
    private static final String CLEANUP_TEST1 = "type=cleanupTest1";
    private static final String CLEANUP_TEST2 = "type=cleanupTest2";
}
