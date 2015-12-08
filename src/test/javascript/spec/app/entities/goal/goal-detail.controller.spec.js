'use strict';

describe('Goal Detail Controller', function() {
    var $scope, $rootScope;
    var MockEntity, MockGoal, MockUser, MockEntry;
    var createController;

    beforeEach(inject(function($injector) {
        $rootScope = $injector.get('$rootScope');
        $scope = $rootScope.$new();
        MockEntity = jasmine.createSpy('MockEntity');
        MockGoal = jasmine.createSpy('MockGoal');
        MockUser = jasmine.createSpy('MockUser');
        MockEntry = jasmine.createSpy('MockEntry');
        

        var locals = {
            '$scope': $scope,
            '$rootScope': $rootScope,
            'entity': MockEntity ,
            'Goal': MockGoal,
            'User': MockUser,
            'Entry': MockEntry
        };
        createController = function() {
            $injector.get('$controller')("GoalDetailController", locals);
        };
    }));


    describe('Root Scope Listening', function() {
        it('Unregisters root scope listener upon scope destruction', function() {
            var eventType = '21pointsApp:goalUpdate';

            createController();
            expect($rootScope.$$listenerCount[eventType]).toEqual(1);

            $scope.$destroy();
            expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
        });
    });
});
