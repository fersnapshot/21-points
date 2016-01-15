'use strict';

describe('Services Tests ', function () {

    describe('Auth', function () {
        var $httpBackend, spiedLocalStorageService, authService, spiedAuthServerProvider;

        beforeEach(inject(function($injector, localStorageService, Auth, AuthServerProvider) {
            $httpBackend = $injector.get('$httpBackend');
            spiedLocalStorageService = localStorageService;
            authService = Auth;
            spiedAuthServerProvider = AuthServerProvider;
            //Request on app init
            $httpBackend.whenGET('scripts/app/main/main.html').respond({});
            $httpBackend.whenGET('scripts/components/navbar/navbar.html').respond({});
            var globalJson = new RegExp('i18n\/.*\/global.json')
            var mainJson = new RegExp('i18n\/.*\/main.json');
            var pointJson = new RegExp('i18n\/.*\/point.json');
            var bloodPressureJson = new RegExp('i18n\/.*\/bloodPressure.json');
            var weightJson = new RegExp('i18n\/.*\/weight.json');
            var preferenceJson = new RegExp('i18n\/.*\/preference.json');
            var unitsJson = new RegExp('i18n\/.*\/units.json');
            $httpBackend.whenGET(globalJson).respond({});
            $httpBackend.whenGET(mainJson).respond({});
            $httpBackend.whenGET(pointJson).respond({});
            $httpBackend.whenGET(bloodPressureJson).respond({});
            $httpBackend.whenGET(weightJson).respond({});
            $httpBackend.whenGET(preferenceJson).respond({});
            $httpBackend.whenGET(unitsJson).respond({});
          }));
        //make sure no expectations were missed in your tests.
        //(e.g. expectGET or expectPOST)
        afterEach(function() {
            $httpBackend.verifyNoOutstandingExpectation();
            $httpBackend.verifyNoOutstandingRequest();
        });
        
          it('should call LocalStorageService.clearAll on logout', function(){
            //GIVEN
            //Set spy
            spyOn(spiedLocalStorageService, "clearAll").and.callThrough();

            //WHEN
            authService.logout();
            //flush the backend to "execute" the request to do the expectedGET assertion.
            $httpBackend.flush();

            //THEN
            expect(spiedLocalStorageService.clearAll).toHaveBeenCalled();
          });

    });
});
