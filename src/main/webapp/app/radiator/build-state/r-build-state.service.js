(function () {
  'use strict';
  angular
    .module('radiatorDemoApp')
    .factory('RBuildState', rBuildState);

  rBuildState.$inject = ['$http'];

  function rBuildState($http) {

    var service = {
      loadLastBuildStatuses: loadLastBuildStatuses
    };

    return service;

    function loadLastBuildStatuses(callback) {
      //console.log("init x1");
      var eventSource = new EventSource("/api/report/stream/build-states");
      eventSource.onmessage = function(e) {
        //console.log("eventSource.onmessage ", callback)
        callback(JSON.parse(e.data));
      };;
    }
  }
})();
