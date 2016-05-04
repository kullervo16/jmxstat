/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package kullervo16.jmxstats.features.counter;

import kullervo16.jmxstats.features.counter.MaxValue;
import java.util.LinkedList;
import java.util.List;
import kullervo16.jmxstats.api.CounterDecorator;
import kullervo16.jmxstats.factories.CounterFactory;
import kullervo16.jmxstats.factories.JmxFactoryProducer;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import org.junit.Test;

/**
 *
 * @author jeve
 */
public class MaxValueTest {
    
    public MaxValueTest() {
    }

    
    @Test
    public void testCreate() {
         CounterFactory factory = new JmxFactoryProducer().getCounterFactory();
         List<Class<? extends CounterDecorator>> featureList = new LinkedList<>();
         featureList.add(MaxValue.class);
         CounterDecorator decoratedCounter =  factory.createDecoratedCounter(null, featureList);
         decoratedCounter.setDescription("boe");
         assertEquals(0, decoratedCounter.getValue());                 
    }
    
    @Test
    public void testMaxValue() {
         CounterFactory factory = new JmxFactoryProducer().getCounterFactory();
         List<Class<? extends CounterDecorator>> featureList = new LinkedList<>();
         featureList.add(MaxValue.class);
         CounterDecorator decoratedCounter =  factory.createDecoratedCounter(null, featureList);
         
         try {
             decoratedCounter.increment();
             fail("should have thrown an exception");
         }catch(IllegalStateException ise) {
             assertEquals("Requested increment (1) cannot be added to 0 as it would surpass the maximum value (0)", ise.getMessage());
         }
         ((MaxValue)decoratedCounter).setMaxValue(10);
         assertEquals(1, decoratedCounter.increment().getValue());
         try {
             decoratedCounter.increment(20);
             fail("should have thrown an exception");
         }catch(IllegalStateException ise) {
             assertEquals("Requested increment (20) cannot be added to 1 as it would surpass the maximum value (10)", ise.getMessage());
         }
         assertEquals(7, decoratedCounter.increment(6).getValue());
         try {
             ((MaxValue)decoratedCounter).setMaxValue(5);
         }catch(IllegalStateException ise) {
             assertEquals("Current value (7) is higher than the requested maximum (5)", ise.getMessage());
         }
    }
}
