(function() {
    'use strict';

    angular
        .module('radiatorDemoApp')
        .directive('rDefaultImageSrc', rDefaultImageSrc);

    function rDefaultImageSrc() {
        return {
            restrict: 'A',
            link: function(scope, element, attrs) {
                element.bind('error', function() {
                        if(element.attr('src') !== attrs.rDefaultImageSrc) {
                            element.attr('src', attrs.rDefaultImageSrc);
                        }
                    }
                );
            }
        };
    }
})();