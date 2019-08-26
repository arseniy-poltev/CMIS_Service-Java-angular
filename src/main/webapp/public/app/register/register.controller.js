(function () {
    'use strict';

    angular
        .module('FileManagerApp')
        .controller('RegisterController', RegisterController);

    RegisterController.$inject = ['UserService', '$location', '$rootScope', 'FlashService'];
    function RegisterController(UserService, $location, $rootScope, FlashService) {
        var vm = this;

        vm.register = register;

        function register() {
            console.log(vm.user);
            vm.dataLoading = true;
            UserService.Create(vm.user)
                .then(function (response) {
                    console.log(response);
                    if (response.data.result.success) {
                        FlashService.Success('Registration successful', true);
                        $location.path('/login');
                    } else {
                        FlashService.Error(response.data.result.error);
                        vm.dataLoading = false;
                    }
                }, function (response) {
                    console.log(response);
                })['finally'](function () {
                    vm.dataLoading = false;
                });
        }
    }

})();
