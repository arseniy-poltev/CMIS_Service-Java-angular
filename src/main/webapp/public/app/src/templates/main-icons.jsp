<div class="iconset noselect">
    <div ng-show="fileNavigator.requesting">
        <div ng-include="config.tplPath + '/spinner.jsp'"></div>
    </div>

    <div class="alert alert-warning"
        ng-show="!fileNavigator.requesting && fileNavigator.fileList.length < 1 && !fileNavigator.error">
        {{"no_files_in_folder" | translate}}...
    </div>

    <div class="alert alert-danger" ng-show="!fileNavigator.requesting && fileNavigator.error">
        {{ fileNavigator.error }}
    </div>
    <div class="item-list">
        <div class="col-120"
            ng-repeat="item in $parent.fileList = (fileNavigator.fileList | filter: {model:{name: query}})"
            ng-show="!fileNavigator.requesting && !fileNavigator.error">
            <a href="" class="thumbnail text-center" ng-click="selectOrUnselect(item, $event)"
                ng-dblclick="smartClick(item)" ng-right-click="selectOrUnselect(item, $event)"
                title="{{item.model.name}} ({{item.model.size | humanReadableFileSize}})"
                ng-class="{selected: isSelected(item)}">
                <div class="item-icon">
                    <i class="glyphicon glyphicon-folder-open" ng-show="item.model.type === 'dir'"></i>
                    <i class="glyphicon glyphicon-file" data-ext="{{ item.model.name | fileExtension }}"
                        ng-show="item.model.type === 'file'"
                        ng-class="{'item-extension': config.showExtensionIcons}"></i>
                </div>
                {{item.model.name | strLimit : 11 }}
            </a>
        </div>
    </div>
</div>