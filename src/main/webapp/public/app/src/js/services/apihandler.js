(function(angular) {
    'use strict';
    function getContextPath() {
        return window.location.pathname.substring(0, window.location.pathname.indexOf("/",2));
    }
    //alert(getContextPath());
    var contextPath = getContextPath();
    angular.module('FileManagerApp').service('apiHandler', ['$http', '$q', '$window', '$translate', '$httpParamSerializer', 'Upload',
        function ($http, $q, $window, $translate, $httpParamSerializer, Upload) {

        $http.defaults.headers.common['X-Requested-With'] = 'XMLHttpRequest';
        //var serverUrl = 'http://ec2-52-15-198-149.us-east-2.compute.amazonaws.com/Phenix-share/manage';
        //var serverUrl = 'http://localhost:8080/manage';

        var serverUrl = contextPath + '/manage';
        //$http.defaults.headers.common['Access-Control-Allow-Origin'] = '*';

        var ApiHandler = function() {
            this.inprocess = false;
            this.asyncSuccess = false;
            this.error = '';
            this.error = '';
        };

        ApiHandler.prototype.deferredHandler = function(data, deferred, code, defaultMsg) {
            if (!data || typeof data !== 'object') {
                this.error = 'Error %s - Bridge response error, please check the API docs or this ajax response.'.replace('%s', code);
            }
            if (code == 404) {
                this.error = 'Error 404 - Backend bridge is not working, please check the ajax response.';
            }
            if (data.result && data.result.error) {
                this.error = data.result.error;
            }
            if (!this.error && data.error) {    
                this.error = data.error.message;
            }
            if (!this.error && defaultMsg) {
                this.error = defaultMsg;
            }
            if (this.error) {
                return deferred.reject(data);
            }
            return deferred.resolve(data);
        };

        ApiHandler.prototype.list = function(apiUrl, path, customDeferredHandler, exts) {
            var self = this;
            var dfHandler = customDeferredHandler || self.deferredHandler;
            var deferred = $q.defer();
            var data = {
                action: 'list',
                path: path,
                fileExtensions: exts && exts.length ? exts : undefined
            };

            self.inprocess = true;
            self.error = '';
            
            $http.post(serverUrl, data,{withCredentials : true}).then(function(response) {
                console.log(response);
                dfHandler(response.data, deferred, response.status);
            }, function(response) {
                dfHandler(response.data, deferred, response.status, 'Unknown error listing, check the response');
            })['finally'](function() {
                self.inprocess = false;
            });
            return deferred.promise;
        };

        ApiHandler.prototype.copy = function(apiUrl, items, path, singleFilename) {
            var self = this;
            var deferred = $q.defer();
            var data = {
                action: 'copy',
                items: items,
                newPath: path
            };

            if (singleFilename && items.length === 1) {
                data.singleFilename = singleFilename;
            }

            self.inprocess = true;
            self.error = '';
            $http.post(serverUrl, data,{withCredentials : true}).then(function(response) {
                self.deferredHandler(response.data, deferred, response.status);
            }, function(response) {
                self.deferredHandler(response.data, deferred, response.status, $translate.instant('error_copying'));
            })['finally'](function() {
                self.inprocess = false;
            });
            return deferred.promise;
        };

        ApiHandler.prototype.move = function(apiUrl, items, path) {
            var self = this;
            var deferred = $q.defer();
            var data = {
                action: 'move',
                items: items,
                newPath: path
            };
            self.inprocess = true;
            self.error = '';
            $http.post(serverUrl, data,{withCredentials : true}).then(function(response) {
                self.deferredHandler(response.data, deferred, response.status);
            }, function(response) {
                self.deferredHandler(response.data, deferred, response.status, $translate.instant('error_moving'));
            })['finally'](function() {
                self.inprocess = false;
            });
            return deferred.promise;
        };

        ApiHandler.prototype.remove = function(apiUrl, items) {
            var self = this;
            var deferred = $q.defer();
            var data = {
                action: 'remove',
                items: items
            };

            self.inprocess = true;
            self.error = '';
            $http.post(serverUrl, data,{withCredentials : true}).then(function(response) {
                self.deferredHandler(response.data, deferred, response.status);
            }, function(response) {
                self.deferredHandler(response.data, deferred, response.status, $translate.instant('error_deleting'));
            })['finally'](function() {
                self.inprocess = false;
            });
            return deferred.promise;
        };

        ApiHandler.prototype.upload = function(apiUrl, destination, files) {
            var self = this;
            var deferred = $q.defer();
            self.inprocess = true;
            self.progress = 0;
            self.error = '';
            //***************** test *****************
          
            //****************************************
            var data = {
                parentpath: destination
            };
            console.log(data);

            for (var i = 0; i < files.length; i++) {
                data['file-' + i] = files[i];
            }

            if (files && files.length) {
                Upload.upload({
                    url: serverUrl,
                    data: data,
                    withCredentials : true
                }).then(function (data) {
                    self.deferredHandler(data.data, deferred, data.status);
                }, function (data) {
                    self.deferredHandler(data.data, deferred, data.status, 'Unknown error uploading files');
                }, function (evt) {
                    self.progress = Math.min(100, parseInt(100.0 * evt.loaded / evt.total)) - 1;
                })['finally'](function() {
                    self.inprocess = false;
                    self.progress = 0;
                });
            }

            return deferred.promise;
        };

        ApiHandler.prototype.getContent = function(apiUrl, itemPath) {
            var self = this;
            var deferred = $q.defer();
            var data = {
                action: 'getContent',
                item: itemPath
            };

            self.inprocess = true;
            self.error = '';
            $http.post(serverUrl, data).then(function(response) {
                self.deferredHandler(response.data, deferred, response.status);
            }, function(response) {
                self.deferredHandler(response.data, deferred, response.status, $translate.instant('error_getting_content'));
            })['finally'](function() {
                self.inprocess = false;
            });
            return deferred.promise;
        };

        ApiHandler.prototype.edit = function(apiUrl, itemPath, content) {
            var self = this;
            var deferred = $q.defer();
            var data = {
                action: 'edit',
                item: itemPath,
                content: content
            };

            self.inprocess = true;
            self.error = '';

            $http.post(serverUrl, data).then(function(response) {
                self.deferredHandler(response.data, deferred, response.status);
            }, function(response) {
                self.deferredHandler(response.data, deferred, response.status, $translate.instant('error_modifying'));
            })['finally'](function() {
                self.inprocess = false;
            });
            return deferred.promise;
        };

        ApiHandler.prototype.rename = function(apiUrl, itemPath, newName) {
            var self = this;
            var deferred = $q.defer();
            var data = {
                action: 'rename',
                item: itemPath,
                newItemName: newName
            };
            self.inprocess = true;
            self.error = '';
            $http.post(serverUrl, data, {withCredentials : true}).then(function(response) {
                self.deferredHandler(response.data, deferred, response.status);
            }, function(response) {
                self.deferredHandler(response.data, deferred, response.status, $translate.instant('error_renaming'));
            })['finally'](function() {
                self.inprocess = false;
            });
            return deferred.promise;
        };

        ApiHandler.prototype.getUrl = function(apiUrl, path) {
            var data = {
                action: 'download',
                path: path
            };
            return path && [apiUrl, $httpParamSerializer(data)].join('?');
        };

        ApiHandler.prototype.download = function(apiUrl, itemPath, toFilename, downloadByAjax, forceNewWindow) {
            var self = this;
            //var url = this.getUrl(apiUrl, itemPath);
            var url = this.getUrl(serverUrl, itemPath);

            if (!downloadByAjax || forceNewWindow || !$window.saveAs) {
                !$window.saveAs && $window.console.log('Your browser dont support ajax download, downloading by default');
                //return !!$window.open(url, '_blank', '');
                return !!$window.location.assign(url);
            }

            var deferred = $q.defer();
            self.inprocess = true;
            $http.get(url,{withCredentials : true}).then(function(response) {
                console.log("333");
                var bin = new $window.Blob([response.data]);
                deferred.resolve(response.data);
                $window.saveAs(bin, toFilename);
            }, function(response) {
                self.deferredHandler(response.data, deferred, response.status, $translate.instant('error_downloading'));
            })['finally'](function() {
                self.inprocess = false;
            });
            return deferred.promise;
        };

        ApiHandler.prototype.downloadMultiple = function(apiUrl, items, toFilename, downloadByAjax, forceNewWindow) {
            var self = this;
            var deferred = $q.defer();
            var data = {
                action: 'downloadMultiple',
                items: items,
                toFilename: toFilename
            };
            var url = [apiUrl, $httpParamSerializer(data)].join('?');

            if (!downloadByAjax || forceNewWindow || !$window.saveAs) {
                !$window.saveAs && $window.console.log('Your browser dont support ajax download, downloading by default');
                return !!$window.open(url, '_blank', '');
            }

            self.inprocess = true;
            $http.get(apiUrl).then(function(response) {
                var bin = new $window.Blob([response.data]);
                deferred.resolve(response.data);
                $window.saveAs(bin, toFilename);
            }, function(response) {
                self.deferredHandler(response.data, deferred, response.status, $translate.instant('error_downloading'));
            })['finally'](function() {
                self.inprocess = false;
            });
            return deferred.promise;
        };

        ApiHandler.prototype.compress = function(apiUrl, items, compressedFilename, path) {
            var self = this;
            var deferred = $q.defer();
            var data = {
                action: 'compress',
                items: items,
                destination: path,
                compressedFilename: compressedFilename
            };

            self.inprocess = true;
            self.error = '';
            $http.post(apiUrl, data).then(function(response) {
                self.deferredHandler(response.data, deferred, response.status);
            }, function(response) {
                self.deferredHandler(response.data, deferred, response.status, $translate.instant('error_compressing'));
            })['finally'](function() {
                self.inprocess = false;
            });
            return deferred.promise;
        };

        ApiHandler.prototype.extract = function(apiUrl, item, folderName, path) {
            var self = this;
            var deferred = $q.defer();
            var data = {
                action: 'extract',
                item: item,
                destination: path,
                folderName: folderName
            };

            self.inprocess = true;
            self.error = '';
            $http.post(apiUrl, data).then(function(response) {
                self.deferredHandler(response.data, deferred, response.status);
            }, function(response) {
                self.deferredHandler(response.data, deferred, response.status, $translate.instant('error_extracting'));
            })['finally'](function() {
                self.inprocess = false;
            });
            return deferred.promise;
        };

        ApiHandler.prototype.changePermissions = function(apiUrl, items, permsOctal, permsCode, recursive) {
            var self = this;
            var deferred = $q.defer();
            var data = {
                action: 'changePermissions',
                items: items,
                perms: permsOctal,
                permsCode: permsCode,
                recursive: !!recursive
            };

            self.inprocess = true;
            self.error = '';
            $http.post(apiUrl, data).then(function(response) {
                self.deferredHandler(response.data, deferred, response.status);
            }, function(response) {
                self.deferredHandler(response.data, deferred, response.status, $translate.instant('error_changing_perms'));
            })['finally'](function() {
                self.inprocess = false;
            });
            return deferred.promise;
        };

        ApiHandler.prototype.createFolder = function(apiUrl, path) {
            var self = this;
            var deferred = $q.defer();
            var data = {
                action: 'createFolder',
                newPath: path
            };

            self.inprocess = true;
            self.error = '';
            $http.post(serverUrl, data,{withCredentials : true}).then(function(response) {
                self.deferredHandler(response.data, deferred, response.status);
            }, function(response) {
                self.deferredHandler(response.data, deferred, response.status, $translate.instant('error_creating_folder'));
            })['finally'](function() {
                self.inprocess = false;
            });

            return deferred.promise;
        };

        return ApiHandler;

    }]);
})(angular);
