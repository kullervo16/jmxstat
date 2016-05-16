/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package kullervo16.jmxstats.features.counter;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.LinkedList;
import java.util.List;
import kullervo16.jmxstats.api.CounterDecorator;
import kullervo16.jmxstats.factories.CounterFactory;
import kullervo16.jmxstats.factories.JmxFactoryProducer;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 *
 * @author jeve
 */
public class EventEmitterTest {
    
    public EventEmitterTest() {
    }

    
    @Test
    public void testCreate() {
         CounterFactory factory = new JmxFactoryProducer().getCounterFactory();
         List<Class<? extends CounterDecorator>> featureList = new LinkedList<>();
         featureList.add(EventEmitter.class);
         CounterDecorator decoratedCounter =  factory.createCounter(null, featureList);
         decoratedCounter.setDescription("boe");
         assertEquals(0, decoratedCounter.getValue());                 
    }
    
    @Test
    public void testChangeListener() {
        
        CounterFactory factory = new JmxFactoryProducer().getCounterFactory();
        List<Class<? extends CounterDecorator>> featureList = new LinkedList<>();
        featureList.add(EventEmitter.class);
        CounterDecorator decoratedCounter =  factory.createCounter(null, featureList);
        
                
        EventEmitter emitter = (EventEmitter) decoratedCounter.getSpecificFeature(EventEmitter.class);
        Listener l1 = new Listener(emitter);
        Listener l2 = new Listener(emitter);
        
        
        emitter.addListener(l1);
        emitter.increment();
        assertEquals(1, l1.getCurrentValue());
        assertEquals(0, l2.getCurrentValue());
        
        emitter.addListener(l1);
        emitter.increment();
        assertEquals(2, l1.getCurrentValue());
        assertEquals(0, l2.getCurrentValue());
        
        emitter.addListener(l2);
        l2.setCurrentValue(2);
        emitter.increment();
        assertEquals(3, l1.getCurrentValue());
        assertEquals(3, l2.getCurrentValue());
                
        emitter.increment(10);
        assertEquals(13, l1.getCurrentValue());
        assertEquals(13, l2.getCurrentValue());
        
        emitter.removeListener(l2);
        emitter.increment(10);
        assertEquals(23, l1.getCurrentValue());
        assertEquals(13, l2.getCurrentValue());
    }
}

class Listener implements PropertyChangeListener {

    private long currentValue = 0;
    private EventEmitter emitter;

    public Listener(EventEmitter emitter) {
        this.emitter = emitter;
    }
        

    public long getCurrentValue() {
        return currentValue;
    }

    public void setCurrentValue(long currentValue) {
        this.currentValue = currentValue;
    }
    
    
    
    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        assertEquals(emitter, evt.getSource());
        assertEquals("value", evt.getPropertyName());
        assertEquals(currentValue, evt.getOldValue());
        currentValue = (long) evt.getNewValue();
    }
    
}