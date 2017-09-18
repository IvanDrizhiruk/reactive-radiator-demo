(function () {
  'use strict';
  angular
    .module('radiatorDemoApp')
    .factory('RBuildState', rBuildState);

  rBuildState.$inject = ['$http', '$q'];

  function rBuildState($http, $q) {

    var service = {
      loadLastBuildStatuses: loadLastBuildStatuses
    };

    return service;

    function loadLastBuildStatuses(callback) {
      var eventSource = new EventSource("/api/report/stream/build-states");
      eventSource.onmessage = function(e) {
        let dataObject = JSON.parse(e.data);
        callback($q.resolve(dataObject));
      };;
    }
  }
})();
