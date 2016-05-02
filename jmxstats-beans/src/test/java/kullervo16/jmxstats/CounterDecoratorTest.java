/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package kullervo16.jmxstats;

import java.util.LinkedList;
import java.util.List;
import kullervo16.jmxstats.api.CounterDecorator;
import kullervo16.jmxstats.factories.CounterFactory;
import kullervo16.jmxstats.factories.JmxFactoryProducer;
import kullervo16.jmxstats.features.MaxValue;
import kullervo16.jmxstats.impl.CounterImpl;
import kullervo16.jmxstats.impl.CounterDecoratorImpl;
import static org.junit.Assert.assertEquals;
import org.junit.Test;

/**
 *
 * @author jeve
 */
public class CounterDecoratorTest {
    
    public CounterDecoratorTest() {
    }

    @Test
    public void testDefaultDecorationLogic() {
        CounterImpl counter = new CounterImpl("decoration");
        CounterDecoratorImpl decorator = new CounterDecoratorImpl(counter) {
            // just testing the defaults
        };
        assertEquals(counter.getValue(), decorator.getValue());
        counter.increment();
        assertEquals(counter.getValue(), decorator.getValue());
        decorator.increment();
        assertEquals(counter.getValue(), decorator.getValue());
        counter.increment(100);
        assertEquals(counter.getValue(), decorator.getValue());
        decorator.increment(50);
        assertEquals(counter.getValue(), decorator.getValue());
        decorator.reset();
        assertEquals(counter.getValue(), decorator.getValue());
        decorator.setDescription("description");
        assertEquals("description", counter.getDescription());
        decorator.setUnit("UNIT");
        assertEquals("UNIT", counter.getUnit());
        
    }
    
    @Test
    public void testCreateWithDecorator() {
         CounterFactory factory = new JmxFactoryProducer().getCounterFactory();
         List<Class<? extends CounterDecorator>> featureList = new LinkedList<>();
         featureList.add(MaxValue.class);
         CounterDecorator decoratedCounter =  factory.createDecoratedCounter(null, featureList);
         decoratedCounter.setDescription("boe");
         assertEquals(0, decoratedCounter.getValue());                 
    }
    
    @Test
    public void testGetSpecificFeature() {
         CounterFactory factory = new JmxFactoryProducer().getCounterFactory();
         List<Class<? extends CounterDecorator>> featureList = new LinkedList<>();
         featureList.add(MaxValue.class);
         CounterDecorator decoratedCounter =  factory.createDecoratedCounter(null, featureList);
         
         assertEquals(MaxValue.class, decoratedCounter.getSpecificFeature(MaxValue.class).getClass());
         
    }
}
