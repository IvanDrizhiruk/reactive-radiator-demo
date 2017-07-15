(function() {
    'use strict';
    angular
        .module('radiatorDemoApp')
        .factory('RRefresher', rRefresher);

    rRefresher.$inject = ['$interval'];

    function rRefresher ($interval) {

        var functionsForExecute = [];

        $interval(execute, 10000);

        return {
            registrate: registrate
        }

        function registrate(functionForExecute) {
            if (functionForExecute) {
                functionsForExecute.push(functionForExecute);
            }
        }

        function execute() {
            console.log("ISD execute");
            functionsForExecute.forEach(function (executable) {
                try{
                    executable();
                } catch(e) {
                    console.log(e);
                }
            });
        }
    }
})();
