# jmxstat

Package providing an easy way to create statistic that are accessible via JMX. Types include counters, gauges, flags, stopwatches,
etc.

A sample website shows how to use the components and allows you to compare the values fetched programmatically with the values
you see in your JMX console (jconsole or jolokia/hawt.io or whathever you like)

Here you have a list of implemented items. The web page contains the same description for all elements, but also showcases their use with practical examples 
(that you can follow using a JMX console/hawt.io/...)

# Counter

## Usecase

The counter counts events... the value simply increases and increases. Either by 1 (default increment) or by the value you specify

Typical use cases are :

* Number of page hits (typically +1 at the detection point)
* Number of bytes read (typically +size of the message)

            
## Additional features

The counter class allows you to decorate it with several additional features. This way, you can create the counter with implicit behaviour at
                declaration time, without the code obtaining the counter via the factory to be aware of it (seperation of concern).

The use of the decorater pattern keeps the counters itself as light as possible, not wasting any time checking for the additional features when
                you don't need it (important as you will often use the counters to measure the performance of your system, so the overhead should be minimal)

All features below can be combined at will, sometimes the sequence in which you request them may influence the resulting behaviour


* Maximum value : this add-on will not allow the counter to increase above the specified value
* Minimum value : this add-on will not allow the counter to decrease below the specified value (typically used with the Gauge, see next section)
* EventEmitter : this add-on sends a PropertyChangeEvent to all registered PropertyChangeListeners when the update was done. The events are sent out synchonously
* ValueLogger : this add-on logs the changes to slf4j

                
# Gauge

A gauge is a special kind of counter that can both increment and decrement. It is created as a Counter add-on, so you can combine it 

## Usecase

When you launch asynchronous calls with a callbackhandler, and you want to know (or restrict) the maximum number of outstanding requests,
you can create a JMX Gauge with a maximum value and increment it when you call the asynch method, and decrement it when the callbackhandler completed          