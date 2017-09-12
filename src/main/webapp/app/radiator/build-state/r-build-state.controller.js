(function() {
    'use strict';

    angular
        .module('radiatorDemoApp')
        .controller('RBuildStateController', RBuildStateController);

    RBuildStateController.$inject = ['$scope', 'RBuildState', 'RRefresher'];

    function RBuildStateController ($scope, RBuildState, RRefresher) {
        var vm = this;
        vm.buildStates = [];
        // vm.loadAll = function() {

        RBuildState.loadLastBuildStatuses(data => console.log(data))

        //       .then(function (result) {
        //           vm.buildStates = result;
        //     });
        // };
        //
        // RRefresher.registrate(vm.loadAll);

        // vm.loadAll();
    }
})();