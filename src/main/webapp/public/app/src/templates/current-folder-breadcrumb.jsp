<ol class="breadcrumb">
    <li>
        <a href="" ng-click="fileNavigator.goTo(-1)">
            {{"brand" | translate}}
        </a>
    </li>
    <li ng-repeat="(key, dir) in fileNavigator.currentPath track by key" ng-class="{'active':$last}" class="animated fast fadeIn">
        <a href="" ng-show="!$last" ng-click="fileNavigator.goTo(key)">
            {{dir | strLimit : 8}}
        </a>
        <span ng-show="$last">
            {{dir | strLimit : 12}}
        </span>
    </li>
</ol>