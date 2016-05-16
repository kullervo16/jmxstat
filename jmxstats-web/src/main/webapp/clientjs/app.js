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
                              console.log("CB1"+JSON.stringify(response));
                              $scope.demoCounter = response; 
                           }, 
                           function(response) {
                              console.log("CB2"+JSON.stringify(response));
                              $scope.demoGauge = response; 
                           }
                         ],
                error: function(response) {
                   console.log("Jolokia request failed: " + response.error);
                } 
              }
            );
           
        }
        
        init();        
    }
    );

   
})();