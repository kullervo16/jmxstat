sudo docker stop jmxstat-locust
sudo docker rm jmxstat-locust
sudo docker run -d --name=jmxstat-locust -p 8089:8089 --net="host" jmxstat-locust --host=http://127.0.0.1:8080 

