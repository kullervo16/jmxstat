(function() {
    var app = angular.module('demo',['ui.bootstrap']);
    
    app.config(['$locationProvider', function($locationProvider){
        $locationProvider.html5Mode({
              enabled: true,
              requireBase: false
            });
    }]);

    app.controller('demoController', function($scope,$http,$window,$interval) {
        
        function init() {
            $interval(function(){reloadDemoCounter();},1000);     
        }
        
        function reloadDemoCounter() {
            var j4p = new Jolokia("../jolokia-war-1.3.3/");
            j4p.request(
              [ 
                { type: "read", mbean: "kullervo16.jmx:type=demoCounter", attribute: "Value"},
                { type: "read", mbean: "kullervo16.jmx:type=demoGauge", attribute: "Value"}
              ],
              { success: [ 
                           function(response) {                              
                              $scope.demoCounter = response; 
                           }, 
                           function(response) {                              
                              $scope.demoGauge = response; 
                              if(response.value < 8) {
                                  $scope.progressStyle = 'success';
                              } else if (response.value < 10) {
                                  $scope.progressStyle = 'warning';
                              } else {
                                  $scope.progressStyle = 'danger';
                              }
                           }
                         ],
                error: function(response) {
                   console.log("Jolokia request failed: " + response.error);
                } 
              }
            );
           
        }
        
        function produce() {
            console.log("Produce");
            $http.get('/jolokia-war-1.3.3/exec/kullervo16.jmx:type=demoGauge/increment()')            
                .success(function (data,status,headers,config) {   
                    if(data.error === undefined) {
                      console.log("OK");
                      reloadDemoCounter();  
                    } else {
                        alert("Cannot produce anymore : "+data.error);
                    }
                    
                }).error(function (data,status,headers,config) {
                    alert("Cannot produce anymore");
                });             
        }
        
        function consume() {
            console.log("Consume");
            console.log("Produce");
            $http.get('/jolokia-war-1.3.3/exec/kullervo16.jmx:type=demoGauge/increment(long)/-1')            
                .success(function (data,status,headers,config) {                    
                    if(data.error === undefined) {
                      console.log("OK");
                      reloadDemoCounter();  
                    } else {
                        alert("Cannot consume anymore : "+data.error);
                    }
                }).error(function (data,status,headers,config) {
                    alert("Cannot consume anymore");
                }); 
        }
                
        $scope.produce = produce;
        $scope.consume = consume;
        
        init();        
    }
    );

   
})();