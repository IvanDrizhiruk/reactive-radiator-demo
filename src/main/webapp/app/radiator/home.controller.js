(function() {
    'use strict';

    angular
        .module('radiatorDemoApp')
        .controller('HomeController', HomeController);

    HomeController.$inject = ['$scope', 'BuildState'];

    function HomeController ($scope, BuildState) {
        var vm = this;

        //
        //BuildState.query(function(result) {
        //    vm.buildStates = result;
        //
        //    vm.labels = ["Download Sales", "In-Store Sales", "Mail-Order Sales"];
        //    vm.data = [300, 500, 100];
        //});

    }
})();
