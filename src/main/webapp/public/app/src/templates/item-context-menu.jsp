<div id="context-menu" class="dropdown clearfix animated fast fadeIn">
    <ul class="dropdown-menu dropdown-right-click" role="menu" aria-labelledby="dropdownMenu" ng-show="temps.length">

        <li ng-show="singleSelection() && singleSelection().isFolder()">
            <a href="" tabindex="-1" ng-click="smartClick(singleSelection())">
                <i class="glyphicon glyphicon-folder-open"></i> {{'open' | translate}}
            </a>
        </li>

        <li ng-show="config.pickCallback && singleSelection() && singleSelection().isSelectable()">
            <a href="" tabindex="-1" ng-click="config.pickCallback(singleSelection().model)">
                <i class="glyphicon glyphicon-hand-up"></i> {{'select_this' | translate}}
            </a>
        </li>

        <li ng-show="config.allowedActions.download && !selectionHas('dir') && singleSelection()">
            <a href="" tabindex="-1" ng-click="download()">
                <i class="glyphicon glyphicon-cloud-download"></i> {{'download' | translate}}
            </a>
        </li>

        <li ng-show="config.allowedActions.downloadMultiple && !selectionHas('dir') && !singleSelection()">
            <a href="" tabindex="-1" ng-click="download()">
                <i class="glyphicon glyphicon-cloud-download"></i> {{'download_as_zip' | translate}}
            </a>
        </li>

        <li ng-show="config.allowedActions.preview && singleSelection().isImage() && singleSelection()">
            <a href="" tabindex="-1" ng-click="openImagePreview()">
                <i class="glyphicon glyphicon-picture"></i> {{'view_item' | translate}}
            </a>
        </li>

        <li ng-show="config.allowedActions.rename && singleSelection()">
            <a href="" tabindex="-1" ng-click="modal('rename')">
                <i class="glyphicon glyphicon-edit"></i> {{'rename' | translate}}
            </a>
        </li>

        <li ng-show="config.allowedActions.move">
            <a href="" tabindex="-1" ng-click="modalWithPathSelector('move')">
                <i class="glyphicon glyphicon-arrow-right"></i> {{'move' | translate}}
            </a>
        </li>

        <li ng-show="config.allowedActions.copy && !selectionHas('dir')">
            <a href="" tabindex="-1" ng-click="modalWithPathSelector('copy')">
                <i class="glyphicon glyphicon-log-out"></i> {{'copy' | translate}}
            </a>
        </li>

        <li ng-show="config.allowedActions.edit && singleSelection() && singleSelection().isEditable()">
            <a href="" tabindex="-1" ng-click="openEditItem()">
                <i class="glyphicon glyphicon-pencil"></i> {{'edit' | translate}}
            </a>
        </li>

        <!-- <li ng-show="config.allowedActions.changePermissions">
            <a href="" tabindex="-1" ng-click="modal('changepermissions')">
                <i class="glyphicon glyphicon-lock"></i> {{'permissions' | translate}}
            </a>
        </li> -->

        <!-- <li ng-show="config.allowedActions.compress && (!singleSelection() || selectionHas('dir'))">
            <a href="" tabindex="-1" ng-click="modal('compress')">
                <i class="glyphicon glyphicon-compressed"></i> {{'compress' | translate}}
            </a>
        </li> -->

        <!-- <li ng-show="config.allowedActions.extract && singleSelection() && singleSelection().isExtractable()">
            <a href="" tabindex="-1" ng-click="modal('extract')">
                <i class="glyphicon glyphicon-export"></i> {{'extract' | translate}}
            </a>
        </li> -->

        <li class="divider" ng-show="config.allowedActions.remove"></li>
        
        <li ng-show="config.allowedActions.remove">
            <a href="" tabindex="-1" ng-click="modal('remove')">
                <i class="glyphicon glyphicon-trash"></i> {{'remove' | translate}}
            </a>
        </li>

    </ul>

    <ul class="dropdown-menu dropdown-right-click" role="menu" aria-labelledby="dropdownMenu" ng-show="!temps.length">
        <li ng-show="config.allowedActions.createFolder">
            <a href="" tabindex="-1" ng-click="modal('newfolder') && prepareNewFolder()">
                <i class="glyphicon glyphicon-plus"></i> {{'new_folder' | translate}}
            </a>
        </li>
        <li ng-show="config.allowedActions.upload">
            <a href="" tabindex="-1" ng-click="modal('uploadfile')">
                <i class="glyphicon glyphicon-cloud-upload"></i> {{'upload_files' | translate}}
            </a>
        </li>
    </ul>
</div>