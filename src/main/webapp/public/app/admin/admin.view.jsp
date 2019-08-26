<nav class="navbar navbar-expand-sm bg-dark navbar-dark" style="min-height:80px">
    <h2 style="font-weight:600;margin-left: 30px;color: white">Phenix</h2>
</nav>
<a href="${pageContext.request.contextPath}/#!/login" role="menuitem" tabindex="-1" style="font-size:30px;font-weight:400;padding:20px;">
    <i class="glyphicon glyphicon-log-out"></i> {{"logout" | translate}}
</a>
<a href="${pageContext.request.contextPath}/#!/" role="menuitem" tabindex="-1" style="font-size:30px;font-weight:400;padding:20px;">
    <i class="glyphicon glyphicon-arrow-right"></i> Show Repository
</a>
<div class='container'>
    <div class="jumbotron">
        <h1>Phenix Server</h1>
        <p>Phenix server is a file sharing system.</p>
        <div ng-class="{ 'alert': flash, 'alert-success': flash.type === 'success', 'alert-danger': flash.type === 'error' }"
            ng-if="flash" ng-bind="flash.message"></div>
    </div>
    <div class="row">
        <h4>Repository URL:</h4>
        <span ng-show='vm.dataLoading'>Loading...</span>
        <input type="text" class="form-control" id="repURL" ng-model='vm.repInfo'
            ng-disabled='vm.serverStarted||vm.operating'>
    </div>
    <br>
    <div class='row'>
        <div class="col-md-4">
            <br>
            <span ng-show='vm.operating'>waiting...</span>
            <div class="row">

                <div class="col-md-4">
                    <button type="button" ng-disabled='vm.repInfo==""||vm.serverStarted||vm.operating'
                        class="btn btn-primary" id="btnStartServer" style="margin-right:10px"
                        ng-click='vm.startServer()'>Start
                        Server</button>
                </div>
                <div class="col-md-4">
                    <button type="button" class="btn btn-danger" ng-disabled='!vm.serverStarted||vm.operating'
                        id="btnStopServer" ng-click='vm.stopServer()'>Stop
                        Server</button>
                </div>
            </div>
        </div>
        <div class="col-md-4">
            <h2>Server Status</h2>
            <div id="serverState">
            </div>
        </div>
    </div>
</div>