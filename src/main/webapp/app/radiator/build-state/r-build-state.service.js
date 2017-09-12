(function () {
  'use strict';
  angular
    .module('radiatorDemoApp')
    .factory('RBuildState', rBuildState);

  rBuildState.$inject = ['$http'/*, 'DateUtils'*/];

  function rBuildState($http /*, DateUtils*/) {

    var service = {
      loadLastBuildStatuses: loadLastBuildStatuses
    }

    return service;

    function loadLastBuildStatuses(callback) {

      var eventSource = new EventSource("/api/report/stream/build-states");

      eventSource.onmessage = function(e) {
        callback(e.data);
      };

      // return $http({
      //   url: 'api/report/build-states/last',
      //   method: 'GET',
      //   isArray: true
      // }).then(function (data) {
      //   return data.data;
      // });
    }
  }
})();
