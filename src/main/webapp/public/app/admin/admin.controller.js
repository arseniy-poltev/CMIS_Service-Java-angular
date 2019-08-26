(function () {
    'use strict';

    angular
        .module('FileManagerApp')
        .controller('AdminController', AdminController);

    AdminController.$inject = ['UserService', '$rootScope', 'FlashService'];
    function AdminController(UserService, $rootScope, FlashService) {
        var vm = this;

        vm.repInfo = '';
        vm.serverStarted = false;
        vm.startServer = startServer;
        vm.stopServer = stopServer;
        vm.getRepInfo = getRepInfo;
        vm.dataLoading = false;
        vm.operating = false;


        (function initController() {
            getRepInfo();
        })();

        function getRepInfo() {
            vm.dataLoading = true;
            UserService.GetRepInfo()
                .then(function (response) {
                    console.log(response);
                    if (response.data.result.success) {
                        vm.repInfo = response.data.result.message;
                        if (response.data.result.other === "CMIS server is started") {
                            vm.serverStarted = true;
                            FlashService.Success("CMIS server is already started");
                        }else{
                            FlashService.Success("Loading service information is success");
                        }
                    } else {
                        FlashService.Error(response.data.result.error);
                    }
                }, function (response) {
                    console.log(response);
                    FlashService.Error("Server error!");
                })['finally'](function () {
                    vm.dataLoading = false;
                });
        }
        function startServer() {
            vm.operating = true;
            vm.serverStarted = false;
            var time = new Date();
            UserService.StartService(vm.repInfo)
                .then(function (response) {
                    console.log(response);
                    if (response.data.result.success) {
                        var endTime = new Date() - time;
                        var now = new Date();
                        $("#serverState").append("<p>" + response.data.result.message + " in " + endTime + "ms</p><p>" + now + "</p><br>")
                        vm.serverStarted = true;
                        FlashService.Success(response.data.result.message);
                    } else {
                        FlashService.Error(response.data.result.error);
                    }
                }, function (response) {
                    console.log(response);
                    FlashService.Error("Server error!");
                })['finally'](function () {
                    vm.operating = false;
                });
        }
        function stopServer() {
            vm.operating = true;
            var time = new Date();
            UserService.StopService()
                .then(function (response) {
                    console.log(response);
                    if (response.data.result.success) {
                        var endTime = new Date() - time;
                        var now = new Date();
                        $("#serverState").append("<p>" + response.data.result.message + " in " + endTime + "ms</p><p>" + now + "</p><br>")
                        vm.serverStarted = false;
                        FlashService.Success(response.data.result.message);
                    } else {
                        FlashService.Error(response.data.result.error);
                    }
                }, function (response) {
                    console.log(response);
                    FlashService.Error("Server error!");
                })['finally'](function () {
                    vm.operating = false;
                });
        }
    }

})();