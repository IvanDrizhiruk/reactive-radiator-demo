(function() {
    'use strict';

    angular
        .module('radiatorDemoApp')
        .directive('rBuildState', rBuildState);

    function rBuildState() {
        var directive = {
            restrict : 'E',
            scope: { items: '=' },
            templateUrl : 'app/radiator/build-state/build-state.html'
        };

        return directive;
    }
})();
