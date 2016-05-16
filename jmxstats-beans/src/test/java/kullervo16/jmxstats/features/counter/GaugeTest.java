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
import org.junit.Test;

/**
 *
 * @author jeve
 */
public class GaugeTest {
    
    public GaugeTest() {
    }

    
    @Test
    public void testCreate() {
         CounterFactory factory = new JmxFactoryProducer().getCounterFactory();
         List<Class<? extends CounterDecorator>> featureList = new LinkedList<>();
         featureList.add(Gauge.class);
         CounterDecorator decoratedCounter =  factory.createCounter(null, featureList);
         decoratedCounter.setDescription("boe");
         assertEquals(0, decoratedCounter.getValue());                 
    }
    
    @Test
    public void testDecrement() {
         CounterFactory factory = new JmxFactoryProducer().getCounterFactory();
         List<Class<? extends CounterDecorator>> featureList = new LinkedList<>();
         featureList.add(Gauge.class);
         CounterDecorator decoratedCounter =  factory.createCounter(null, featureList);
         Gauge gauge = (Gauge)decoratedCounter.getSpecificFeature(Gauge.class);
         
         assertEquals(0, gauge.getValue());
         gauge.increment(1);
         assertEquals(1, gauge.getValue());
         gauge.decrement();
         assertEquals(0, gauge.getValue());
         gauge.decrement();
         assertEquals(-1, gauge.getValue());
         gauge.decrement(5);
         assertEquals(-6, gauge.getValue());
         gauge.increment(10);
         assertEquals(4, gauge.getValue());
    }
}
