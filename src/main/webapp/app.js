(function () {
    'use strict';
    function getContextPath() {
        return window.location.pathname.substring(0, window.location.pathname.indexOf("/",2)) + '/';
    }
    //alert(getContextPath());
    var contextPath = getContextPath();
    angular
        .module('FileManagerApp', ['ngRoute', 'ngCookies', 'pascalprecht.translate', 'ngFileUpload'])
        .config(config)
        .run(run);
    /**
         * jQuery inits
         */
    angular.element(window.document).on('shown.bs.modal', '.modal', function () {
        window.setTimeout(function () {
            angular.element('[autofocus]', this).focus();
        }.bind(this), 100);
    });

    angular.element(window.document).on('click', function () {
        angular.element('#context-menu').hide();
    });

    //angular.element(window.document).on('contextmenu', '.main-navigation .table-files tr.item-list:has("td"), .item-list', function (e) {
    angular.element(window.document).on('contextmenu', '.main-navigation', function (e) {
        var menu = angular.element('#context-menu');

        if (e.pageX >= window.innerWidth - menu.width()) {
            e.pageX -= menu.width();
        }
        if (e.pageY >= window.innerHeight - menu.height()) {
            e.pageY -= menu.height();
        }

        menu.hide().css({
            left: e.pageX,
            top: e.pageY
        }).appendTo('body').show();
        e.preventDefault();
    });

    if (!Array.prototype.find) {
        Array.prototype.find = function (predicate) {
            if (this == null) {
                throw new TypeError('Array.prototype.find called on null or undefined');
            }
            if (typeof predicate !== 'function') {
                throw new TypeError('predicate must be a function');
            }
            var list = Object(this);
            var length = list.length >>> 0;
            var thisArg = arguments[1];
            var value;

            for (var i = 0; i < length; i++) {
                value = list[i];
                if (predicate.call(thisArg, value, i, list)) {
                    return value;
                }
            }
            return undefined;
        };
    }

    config.$inject = ['$routeProvider', '$locationProvider'];
    function config($routeProvider,  $locationProvider) {

        $routeProvider
            .when('/', {
                controller: 'HomeController',
                templateUrl: contextPath + 'public/app/home/home.view.jsp',
                controllerAs: 'vm'
            })

            .when('/login', {
                controller: 'LoginController',
                templateUrl: contextPath + 'public/app/login/login.view.jsp',
                controllerAs: 'vm'
            })

            .when('/register', {
                controller: 'RegisterController',
                templateUrl: contextPath + 'public/app/register/register.view.jsp',
                controllerAs: 'vm'
            })

            .when('/admin',{
                controller: 'AdminController',
                templateUrl: contextPath + 'public/app/admin/admin.view.jsp',
                controllerAs: 'vm'
            })

            .otherwise({ redirectTo: '/login' });
        //$locationProvider.html5Mode(true);
        //$locationProvider.hashPrefix('');
    }

    run.$inject = ['$rootScope', '$location', '$cookies', '$http'];
    function run($rootScope, $location, $cookies, $http) {
        // keep user logged in after page refresh
        $rootScope.globals = $cookies.getObject('globals') || {};
        if ($rootScope.globals.currentUser) {
            $http.defaults.headers.common['Authorization'] = 'Basic ' + $rootScope.globals.currentUser.authdata;
        }

        $rootScope.$on('$locationChangeStart', function (event, next, current) {
            //redirect to login page if not logged in and trying to access a restricted page
            var restrictedPage = $.inArray($location.path(), ['/login', '/register']) === -1;
            var loggedIn = $rootScope.globals.currentUser;
            
            if (restrictedPage && !loggedIn) {
                $location.path('/login');
            }
            
        });
    }

})();