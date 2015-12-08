'use strict';

describe('Entry Detail Controller', function() {
    var $scope, $rootScope;
    var MockEntity, MockEntry, MockGoal, MockMetrica;
    var createController;

    beforeEach(inject(function($injector) {
        $rootScope = $injector.get('$rootScope');
        $scope = $rootScope.$new();
        MockEntity = jasmine.createSpy('MockEntity');
        MockEntry = jasmine.createSpy('MockEntry');
        MockGoal = jasmine.createSpy('MockGoal');
        MockMetrica = jasmine.createSpy('MockMetrica');
        

        var locals = {
            '$scope': $scope,
            '$rootScope': $rootScope,
            'entity': MockEntity ,
            'Entry': MockEntry,
            'Goal': MockGoal,
            'Metrica': MockMetrica
        };
        createController = function() {
            $injector.get('$controller')("EntryDetailController", locals);
        };
    }));


    describe('Root Scope Listening', function() {
        it('Unregisters root scope listener upon scope destruction', function() {
            var eventType = '21pointsApp:entryUpdate';

            createController();
            expect($rootScope.$$listenerCount[eventType]).toEqual(1);

            $scope.$destroy();
            expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
        });
    });
});
