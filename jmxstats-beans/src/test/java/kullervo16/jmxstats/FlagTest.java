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
import kullervo16.jmxstats.api.Flag;
import kullervo16.jmxstats.factories.FlagFactory;
import kullervo16.jmxstats.factories.JmxFactoryProducer;
import kullervo16.jmxstats.impl.FlagImpl;
import static org.junit.Assert.*;

/**
 *
 * @author jef
 */
public class FlagTest {
    
    public FlagTest() {
    }
    
    

    
    @org.junit.Test
    public void testSet() {
        FlagImpl flag = new FlagImpl(null);
        assertTrue(flag.set().isSet());
        assertTrue(flag.set().isSet());
    }
    
    @org.junit.Test
    public void testUnset() {
        FlagImpl flag = new FlagImpl(null);
        assertTrue(flag.set().isSet());
        assertFalse(flag.unset().isSet());
    }
    
    @org.junit.Test
    public void testToggle() {
        FlagImpl flag = new FlagImpl(null);
        assertFalse(flag.isSet());
        assertTrue(flag.toggle().isSet());
        assertFalse(flag.toggle().isSet());
        assertTrue(flag.toggle().isSet());
        assertFalse(flag.toggle().isSet());
    }

    

    
    @org.junit.Test
    public void testCreateFactory() {        
        assertNotNull(new JmxFactoryProducer().getFlagFactory());        
    }
    
    @org.junit.Test
    public void testCreateFlagViaFactory()  {      
        FlagFactory factory = new JmxFactoryProducer().getFlagFactory();
        Flag f1 = factory.createFlag();
        Flag f2 = factory.createFlag();
        assertNotNull(f1); 
        f1.set();
        assertNotNull(f2);
        
        assertFalse(f1.equals(f2));   
        assertNotEquals(f1.isSet(), f2.isSet());
        
        // now with jmx flag
        f1 = factory.getJmxFlag("type=test");
        f1.setDescription("Description 1");       
        f2 = factory.getJmxFlag("type=test");
        assertNotNull(f1);  
        assertEquals("type=test", f1.getId());
        assertNotNull(f1.toString());
        f1.set();
        assertNotNull(f2);
        assertTrue(f1.equals(f2)); 
        assertTrue(f2.isSet());
        assertEquals("Description 1", f2.getDescription());
        
        try {
            factory.getJmxFlag("invalid");
            fail("Should trow invalidargument");
        }catch(IllegalArgumentException iae) {
            
        }
    }
    
    @org.junit.Test
    public void testUndeployFlagCleanup() throws MalformedObjectNameException, InstanceNotFoundException, IntrospectionException, ReflectionException  {
        FlagFactory factory = new JmxFactoryProducer().getFlagFactory();
        Flag f1 =  factory.getJmxFlag(CLEANUP_TEST1);
        Flag f2 =  factory.getJmxFlag(CLEANUP_TEST2);
        
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
    public void testFlagCleanup() throws MalformedObjectNameException, InstanceNotFoundException, IntrospectionException, ReflectionException  {
        FlagFactory factory = new JmxFactoryProducer().getFlagFactory();
        Flag f1 = factory.getJmxFlag(CLEANUP_TEST1);
        Flag f2 = factory.getJmxFlag(CLEANUP_TEST2);
        
        MBeanServer mbs = ManagementFactory.getPlatformMBeanServer();
        ObjectName mxbeanName1 = new ObjectName(factory.getPrefix()+CLEANUP_TEST1);        
        assertNotNull(mbs.getMBeanInfo(mxbeanName1));
        
        ObjectName mxbeanName2 = new ObjectName(factory.getPrefix()+CLEANUP_TEST2);        
        assertNotNull(mbs.getMBeanInfo(mxbeanName2));
        
        factory.unregisterFlag(CLEANUP_TEST2);
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
        FlagFactory factory = new JmxFactoryProducer().getFlagFactory();
        Flag f1 = factory.getJmxFlag(CLEANUP_TEST1);
        f1.setDescription("My description");
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
    
    
    
    private static final String CLEANUP_TEST1 = "type=cleanupTest1";
    private static final String CLEANUP_TEST2 = "type=cleanupTest2";
}
