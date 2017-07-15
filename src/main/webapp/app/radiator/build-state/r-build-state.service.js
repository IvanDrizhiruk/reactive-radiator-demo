(function() {
    'use strict';
    angular
        .module('radiatorDemoApp')
        .factory('RBuildState', rBuildState);

    rBuildState.$inject = ['$http'/*, 'DateUtils'*/];

    function rBuildState ($http /*, DateUtils*/) {

        var service = {
            loadLastBuildStatuses: loadLastBuildStatuses
        }

        return service;

        function loadLastBuildStatuses() {
            return $http({
                url: 'api/report/build-states/last',
                method: 'GET',
                isArray: true
            });
        }
    }
})();
