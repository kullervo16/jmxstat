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
            $http.get('../jolokia-war-1.3.3/read/kullervo16.jmx:type=demoCounter/Value')
                .success(function (data,status,headers,config) {
                    $scope.demoCounter = data;                    
                }).error(function (data,status,headers,config) {
                    console.log('Error getting rest/template/list');
                });
        }
        
        init();        
    }
    );

   
})();