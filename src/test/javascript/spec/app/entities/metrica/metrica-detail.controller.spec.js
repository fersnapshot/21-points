'use strict';

describe('Metrica Detail Controller', function() {
    var $scope, $rootScope;
    var MockEntity, MockMetrica, MockEntry;
    var createController;

    beforeEach(inject(function($injector) {
        $rootScope = $injector.get('$rootScope');
        $scope = $rootScope.$new();
        MockEntity = jasmine.createSpy('MockEntity');
        MockMetrica = jasmine.createSpy('MockMetrica');
        MockEntry = jasmine.createSpy('MockEntry');
        

        var locals = {
            '$scope': $scope,
            '$rootScope': $rootScope,
            'entity': MockEntity ,
            'Metrica': MockMetrica,
            'Entry': MockEntry
        };
        createController = function() {
            $injector.get('$controller')("MetricaDetailController", locals);
        };
    }));


    describe('Root Scope Listening', function() {
        it('Unregisters root scope listener upon scope destruction', function() {
            var eventType = '21pointsApp:metricaUpdate';

            createController();
            expect($rootScope.$$listenerCount[eventType]).toEqual(1);

            $scope.$destroy();
            expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
        });
    });
});
