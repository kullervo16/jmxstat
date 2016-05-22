#!/usr/bin/env python2
#encoding: UTF-8

from locust import HttpLocust, TaskSet, task

class JmxStatsTest(TaskSet):
    
    
    @task
    def get_demo_counter(self):
        self.client.get("/jolokia-war-1.3.3/read/kullervo16.jmx:type=demoCounter/Value")
        
    @task
    def get_demo_gauge(self):
        self.client.get("/jolokia-war-1.3.3/read/kullervo16.jmx:type=demoGauge/Value")
        
    @task
    def get_old_generation_stats(self):
        self.client.get("/jolokia-war-1.3.3/read/java.lang:type=MemoryPool,name=PS Old Gen/CollectionUsage")
        
    @task
    def get_eden_stats(self):
        self.client.get("/jolokia-war-1.3.3/read/java.lang:type=MemoryPool,name=PS Eden Space/CollectionUsage")
        
        

class WebsiteUser(HttpLocust):
    task_set = JmxStatsTest
    min_wait = 5000
    max_wait = 15000