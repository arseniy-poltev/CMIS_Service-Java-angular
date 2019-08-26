<div ng-controller="FileManagerCtrl">
    <!-- <div class="container-fluid"> -->
    <!-- <div class="row"> -->
    <div class="wrapper">
        <div class="sidebar file-tree animated slow fadeIn" ng-include="config.tplPath + '/sidebar.jsp'"
            ng-show="config.sidebar &amp;&amp; fileNavigator.history[0]">
        </div>
        <!-- col-sm-8 col-md-9 -->
        <div id="content">
            <div ng-include="config.tplPath + '/navbar.jsp'"></div>

            <div class="main clearfix" ng-class="config.sidebar &amp;&amp; fileNavigator.history[0] &amp;&amp; ''"
                ngf-model-options="{updateOn: 'drop', allowInvalid: false, debounce: 0}" ngf-drop="addForUpload($files)"
                ngf-drag-over-class="'upload-dragover'" ngf-multiple="true">

                <div ng-include="config.tplPath + '/' + viewTemplate" class="main-navigation clearfix" prevent="true"
                    ng-click="selectOrUnselect(null, $event)" ng-right-click="selectOrUnselect(null, $event)">
                </div>

            </div>
        </div>
    </div>
    <!-- </div> -->
    <!-- </div> -->
    <div ng-include="config.tplPath + '/modals.jsp'"></div>
    <div ng-include="config.tplPath + '/item-context-menu.jsp'"></div>
</div>