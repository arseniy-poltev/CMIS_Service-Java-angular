(function () {
    'use strict';
    function getContextPath() {
        return window.location.pathname.substring(0, window.location.pathname.indexOf("/",2));
    }
    //alert(getContextPath());
    var contextPath = getContextPath();
    angular
        .module('FileManagerApp')
        .factory('UserService', UserService);

    UserService.$inject = ['$http'];
    function UserService($http) {
        var service = {};
        
        //http://ec2-52-15-198-149.us-east-2.compute.amazonaws.com
        //var server = 'http://localhost:8080';
        var server = '';
        // var server = 'http://ec2-52-15-198-149.us-east-2.compute.amazonaws.com/Phenix-share';
        var serverUrl = contextPath + '/usermanage';
        var adminUrl = contextPath + '/admin';
        
        
        
        service.Create = Create;
        service.Login = Login;
        service.Logout = Logout;
        service.GetRepInfo = GetRepInfo;
        service.StartService = StartService;
        service.StopService = StopService;


        return service;



        function Login(user) {
            return $http.post(serverUrl + '/login', user,{withCredentials : true});
        }

        function Logout(){
            return $http.get(serverUrl + '/logout');
        }

        function Create(user) {
            return $http.post(serverUrl + '/register', user);
        }

        function GetRepInfo() {
            return $http.post(adminUrl + '/get_info');
        }

        function StartService(repInfo) {
            var data = { repURL: repInfo };
            return $http.post(adminUrl + '/server_start', data);
        }

        function StopService() {
            return $http.post(adminUrl + '/server_stop');
        }



        // private functions

        function handleSuccess(res) {
            return res.data;
        }

        function handleError(error) {
            console.log(error.data);
            return error.data;
            // return function () {
            //     return { success: false, message: error };
            // };
        }
    }

})();
