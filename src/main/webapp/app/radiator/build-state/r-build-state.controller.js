(function () {
    'use strict';

    angular
        .module('radiatorDemoApp')
        .controller('RBuildStateController', RBuildStateController);

    RBuildStateController.$inject = ['$scope', 'RBuildState'];

    function RBuildStateController($scope, RBuildState) {
        const vm = this;
        vm.buildStates = [];

        vm.loadData = loadData;

        RBuildState.loadLastBuildStatuses(promise => promise.then(vm.loadData));


        function loadData(data) {
          const oldBuildStatesIndex = vm.buildStates.findIndex(value => value.instancesName === data.instancesName);
          if (-1 === oldBuildStatesIndex) {
            vm.buildStates.push(data);
          } else {
            vm.buildStates[oldBuildStatesIndex] = data;
          }
        }
    }
})();