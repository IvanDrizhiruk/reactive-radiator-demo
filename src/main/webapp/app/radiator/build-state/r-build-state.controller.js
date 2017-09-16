(function () {
    'use strict';

    angular
        .module('radiatorDemoApp')
        .controller('RBuildStateController', RBuildStateController);

    RBuildStateController.$inject = ['$scope', 'RBuildState'];

    function RBuildStateController($scope, RBuildState) {
        var vm = this;
        vm.buildStates = [];

        RBuildState.loadLastBuildStatuses(buildStates => {
            const oldBuildStatesIndex = vm.buildStates.findIndex(value => value.instancesName === buildStates.instancesName);
            if (-1 === oldBuildStatesIndex) {
                vm.buildStates.push(buildStates);
            } else {
                vm.buildStates[oldBuildStatesIndex] = buildStates;
            }
        });
    }
})();