/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package kullervo16.jmxstats.features.counter;

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
public class MinValueTest {
    
    public MinValueTest() {
    }

    
    @Test
    public void testCreate() {
         CounterFactory factory = new JmxFactoryProducer().getCounterFactory();
         List<Class<? extends CounterDecorator>> featureList = new LinkedList<>();
         featureList.add(Gauge.class);
         featureList.add(MinValue.class);
         CounterDecorator decoratedCounter =  factory.createCounter(null, featureList);
         decoratedCounter.setDescription("boe");
         assertEquals(0, decoratedCounter.getValue());                 
    }
    
    @Test
    public void testMinValue() {
         CounterFactory factory = new JmxFactoryProducer().getCounterFactory();
         List<Class<? extends CounterDecorator>> featureList = new LinkedList<>();
         featureList.add(MinValue.class);
         featureList.add(Gauge.class);
         
         CounterDecorator decoratedCounter =  factory.createCounter(null, featureList);
         Gauge gauge = (Gauge)decoratedCounter.getSpecificFeature(Gauge.class);
         MinValue mv = (MinValue)decoratedCounter.getSpecificFeature(MinValue.class);
         
         
         try {
             gauge.decrement();
             fail("should have thrown an exception");
         }catch(IllegalStateException ise) {
             assertEquals("Requested increment (-1) cannot be applied to 0 as it would surpass the minimum value (0)", ise.getMessage());
         }
         gauge.increment(10);
         assertEquals(9, gauge.decrement().getValue());         
                  
         try {
             mv.setMinValue(10);
             fail("should have thrown an exception");
         }catch(IllegalStateException ise) {
             assertEquals("Current value (9) is lower than the requested minimum (10)", ise.getMessage());
         }
         mv.setMinValue(5);
         try {
             gauge.decrement(20);
             fail("should have thrown an exception");
         }catch(IllegalStateException ise) {
             assertEquals("Requested increment (-20) cannot be applied to 9 as it would surpass the minimum value (5)", ise.getMessage());
         }
    }
    
    
}
