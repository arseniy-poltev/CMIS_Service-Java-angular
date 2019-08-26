<!-- <ul class="nav nav-sidebar file-tree-root">
    <li ng-repeat="item in fileNavigator.history" ng-include="'folder-branch-item'" ng-class="{'active': item.name == fileNavigator.currentPath.join('/')}"></li>
</ul>

<script type="text/ng-template" id="folder-branch-item">
    <a href="" ng-click="fileNavigator.folderClick(item.item)" class="animated fast fadeInDown">

        <span class="point">
            <i class="glyphicon glyphicon-chevron-down" ng-show="isInThisPath(item.name)"></i>
            <i class="glyphicon glyphicon-chevron-right" ng-show="!isInThisPath(item.name)"></i>
        </span>

        <i class="glyphicon glyphicon-folder-open mr2" ng-show="isInThisPath(item.name)"></i>
        <i class="glyphicon glyphicon-folder-close mr2" ng-show="!isInThisPath(item.name)"></i>
        {{ (item.name.split('/').pop() || fileNavigator.getBasePath().join('/') || '/') | strLimit : 30 }}
    </a>
    <ul class="nav nav-sidebar">
        <li ng-repeat="item in item.nodes" ng-include="'folder-branch-item'" ng-class="{'active': item.name == fileNavigator.currentPath.join('/')}"></li>
    </ul>
</script> -->
<nav id="sidebar">
    <div class="sidebar-header">
        <h3 style="color:aliceblue">Phenix</h3>
    </div>
    <ul class="list-unstyled components">
        <li>
            <a href="">My Files</a>
        </li>
        <li>
            <a href="">Shared Files</a>
        </li>

        <li>
            <a href="">About</a>
        </li>
    </ul>
</nav>