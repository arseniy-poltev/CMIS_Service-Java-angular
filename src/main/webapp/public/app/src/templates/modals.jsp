<div class="modal animated fadeIn" id="imagepreview">
  <div class="modal-dialog">
    <div class="modal-content">
      <div class="modal-header">
        <button type="button" class="close" data-dismiss="modal">
            <span aria-hidden="true">&times;</span>
            <span class="sr-only">{{"close" | translate}}</span>
        </button>
        <h4 class="modal-title">{{"preview" | translate}}</h4>
      </div>
      <div class="modal-body">
        <div class="text-center">
          <img id="imagepreview-target" class="preview" alt="{{singleSelection().model.name}}" ng-class="{'loading': apiMiddleware.apiHandler.inprocess}">
          <span class="label label-warning" ng-show="apiMiddleware.apiHandler.inprocess">{{'loading' | translate}} ...</span>
        </div>
        <div ng-include data-src="'error-bar'" class="clearfix"></div>
      </div>
      <div class="modal-footer">
        <button type="button" class="btn btn-default" data-dismiss="modal" ng-disabled="apiMiddleware.apiHandler.inprocess">{{"close" | translate}}</button>
      </div>
    </div>
  </div>
</div>

<div class="modal animated fadeIn" id="remove">
  <div class="modal-dialog">
    <div class="modal-content">
    <form ng-submit="remove()">
      <div class="modal-header">
        <button type="button" class="close" data-dismiss="modal">
            <span aria-hidden="true">&times;</span>
            <span class="sr-only">{{"close" | translate}}</span>
        </button>
        <h4 class="modal-title">{{"confirm" | translate}}</h4>
      </div>
      <div class="modal-body">
        {{'sure_to_delete' | translate}} <span ng-include data-src="'selected-files-msg'"></span>

        <div ng-include data-src="'error-bar'" class="clearfix"></div>
      </div>
      <div class="modal-footer">
        <button type="button" class="btn btn-default" data-dismiss="modal" ng-disabled="apiMiddleware.apiHandler.inprocess">{{"cancel" | translate}}</button>
        <button type="submit" class="btn btn-primary" ng-disabled="apiMiddleware.apiHandler.inprocess" autofocus="autofocus">{{"remove" | translate}}</button>
      </div>
      </form>
    </div>
  </div>
</div>

<div class="modal animated fadeIn" id="move">
  <div class="modal-dialog">
    <div class="modal-content">
        <form ng-submit="move()">
            <div class="modal-header">
              <button type="button" class="close" data-dismiss="modal">
                  <span aria-hidden="true">&times;</span>
                  <span class="sr-only">{{"close" | translate}}</span>
              </button>
              <h4 class="modal-title">{{'move' | translate}}</h4>
            </div>
            <div class="modal-body">
              <div ng-include data-src="'path-selector'" class="clearfix"></div>
              <div ng-include data-src="'error-bar'" class="clearfix"></div>
            </div>
            <div class="modal-footer">
              <button type="button" class="btn btn-default" data-dismiss="modal" ng-disabled="apiMiddleware.apiHandler.inprocess">{{"cancel" | translate}}</button>
              <button type="submit" class="btn btn-primary" ng-disabled="apiMiddleware.apiHandler.inprocess">{{'move' | translate}}</button>
            </div>
        </form>
    </div>
  </div>
</div>


<div class="modal animated fadeIn" id="rename">
  <div class="modal-dialog">
    <div class="modal-content">
        <form ng-submit="rename()">
            <div class="modal-header">
              <button type="button" class="close" data-dismiss="modal">
                  <span aria-hidden="true">&times;</span>
                  <span class="sr-only">{{"close" | translate}}</span>
              </button>
              <h4 class="modal-title">{{'rename' | translate}}</h4>
            </div>
            <div class="modal-body">
              <label class="radio">{{'enter_new_name_for' | translate}} <b>{{singleSelection() && singleSelection().model.name}}</b></label>
              <input class="form-control" ng-model="singleSelection().tempModel.name" autofocus="autofocus">

              <div ng-include data-src="'error-bar'" class="clearfix"></div>
            </div>
            <div class="modal-footer">
              <button type="button" class="btn btn-default" data-dismiss="modal" ng-disabled="apiMiddleware.apiHandler.inprocess">{{"cancel" | translate}}</button>
              <button type="submit" class="btn btn-primary" ng-disabled="apiMiddleware.apiHandler.inprocess">{{'rename' | translate}}</button>
            </div>
        </form>
    </div>
  </div>
</div>

<div class="modal animated fadeIn" id="copy">
  <div class="modal-dialog">
    <div class="modal-content">
        <form ng-submit="copy()">
            <div class="modal-header">
              <button type="button" class="close" data-dismiss="modal">
                  <span aria-hidden="true">&times;</span>
                  <span class="sr-only">{{"close" | translate}}</span>
              </button>
              <h4 class="modal-title">{{'copy_file' | translate}}</h4>
            </div>
            <div class="modal-body">
              <div ng-show="singleSelection()">
                <label class="radio">{{'enter_new_name_for' | translate}} <b>{{singleSelection().model.name}}</b></label>
                <input class="form-control" ng-model="singleSelection().tempModel.name" autofocus="autofocus">
              </div>

              <div ng-include data-src="'path-selector'" class="clearfix"></div>
              <div ng-include data-src="'error-bar'" class="clearfix"></div>
            </div>
            <div class="modal-footer">
              <button type="button" class="btn btn-default" data-dismiss="modal" ng-disabled="apiMiddleware.apiHandler.inprocess">{{"cancel" | translate}}</button>
              <button type="submit" class="btn btn-primary" ng-disabled="apiMiddleware.apiHandler.inprocess">{{"copy" | translate}}</button>
            </div>
        </form>
    </div>
  </div>
</div>

<div class="modal animated fadeIn" id="compress">
  <div class="modal-dialog">
    <div class="modal-content">
        <form ng-submit="compress()">
            <div class="modal-header">
              <button type="button" class="close" data-dismiss="modal">
                  <span aria-hidden="true">&times;</span>
                  <span class="sr-only">{{"close" | translate}}</span>
              </button>
              <h4 class="modal-title">{{'compress' | translate}}</h4>
            </div>
            <div class="modal-body">
              <div ng-show="apiMiddleware.apiHandler.asyncSuccess">
                  <div class="label label-success error-msg">{{'compression_started' | translate}}</div>
              </div>
              <div ng-hide="apiMiddleware.apiHandler.asyncSuccess">
                  <div ng-hide="config.allowedActions.compressChooseName">
                    {{'sure_to_start_compression_with' | translate}} <b>{{singleSelection().model.name}}</b> ?
                  </div>
                  <div ng-show="config.allowedActions.compressChooseName">
                    <label class="radio">
                      {{'enter_file_name_for_compression' | translate}}
                      <span ng-include data-src="'selected-files-msg'"></span>
                    </label>
                    <input class="form-control" ng-model="temp.tempModel.name" autofocus="autofocus">
                  </div>
              </div>

              <div ng-include data-src="'error-bar'" class="clearfix"></div>
            </div>
            <div class="modal-footer">
              <div ng-show="apiMiddleware.apiHandler.asyncSuccess">
                  <button type="button" class="btn btn-default" data-dismiss="modal" ng-disabled="apiMiddleware.apiHandler.inprocess">{{"close" | translate}}</button>
              </div>
              <div ng-hide="apiMiddleware.apiHandler.asyncSuccess">
                  <button type="button" class="btn btn-default" data-dismiss="modal" ng-disabled="apiMiddleware.apiHandler.inprocess">{{"cancel" | translate}}</button>
                  <button type="submit" class="btn btn-primary" ng-disabled="apiMiddleware.apiHandler.inprocess">{{'compress' | translate}}</button>
              </div>
            </div>
        </form>
    </div>
  </div>
</div>

<div class="modal animated fadeIn" id="extract" ng-init="singleSelection().emptyName()">
  <div class="modal-dialog">
    <div class="modal-content">
        <form ng-submit="extract()">
            <div class="modal-header">
              <button type="button" class="close" data-dismiss="modal">
                  <span aria-hidden="true">&times;</span>
                  <span class="sr-only">{{"close" | translate}}</span>
              </button>
              <h4 class="modal-title">{{'extract_item' | translate}}</h4>
            </div>
            <div class="modal-body">
              <div ng-show="apiMiddleware.apiHandler.asyncSuccess">
                  <div class="label label-success error-msg">{{'extraction_started' | translate}}</div>
              </div>
              <div ng-hide="apiMiddleware.apiHandler.asyncSuccess">
                  <label class="radio">{{'enter_folder_name_for_extraction' | translate}} <b>{{singleSelection().model.name}}</b></label>
                  <input class="form-control" ng-model="singleSelection().tempModel.name" autofocus="autofocus">
              </div>
              <div ng-include data-src="'error-bar'" class="clearfix"></div>
            </div>
            <div class="modal-footer">
              <div ng-show="apiMiddleware.apiHandler.asyncSuccess">
                  <button type="button" class="btn btn-default" data-dismiss="modal" ng-disabled="apiMiddleware.apiHandler.inprocess">{{"close" | translate}}</button>
              </div>
              <div ng-hide="apiMiddleware.apiHandler.asyncSuccess">
                  <button type="button" class="btn btn-default" data-dismiss="modal" ng-disabled="apiMiddleware.apiHandler.inprocess">{{"cancel" | translate}}</button>
                  <button type="submit" class="btn btn-primary" ng-disabled="apiMiddleware.apiHandler.inprocess">{{'extract' | translate}}</button>
              </div>
            </div>
        </form>
    </div>
  </div>
</div>

<div class="modal animated fadeIn" id="edit" ng-class="{'modal-fullscreen': fullscreen}">
  <div class="modal-dialog modal-lg">
    <div class="modal-content">
        <form ng-submit="edit()">
            <div class="modal-header">
              <button type="button" class="close" data-dismiss="modal">
                  <span aria-hidden="true">&times;</span>
                  <span class="sr-only">{{"close" | translate}}</span>
              </button>
              <button type="button" class="close fullscreen" ng-click="fullscreen=!fullscreen">
                  <i class="glyphicon glyphicon-fullscreen"></i>
                  <span class="sr-only">{{'toggle_fullscreen' | translate}}</span>
              </button>
              <h4 class="modal-title">{{'edit_file' | translate}}</h4>
            </div>
            <div class="modal-body">
                <label class="radio bold">{{ singleSelection().model.fullPath() }}</label>
                <span class="label label-warning" ng-show="apiMiddleware.apiHandler.inprocess">{{'loading' | translate}} ...</span>
                <textarea class="form-control code" ng-model="singleSelection().tempModel.content" ng-show="!apiMiddleware.apiHandler.inprocess" autofocus="autofocus"></textarea>
                <div ng-include data-src="'error-bar'" class="clearfix"></div>
            </div>
            <div class="modal-footer">
              <button type="button" class="btn btn-default" data-dismiss="modal" ng-disabled="apiMiddleware.apiHandler.inprocess">{{'close' | translate}}</button>
              <button type="submit" class="btn btn-primary" ng-show="config.allowedActions.edit" ng-disabled="apiMiddleware.apiHandler.inprocess">{{'save' | translate}}</button>
            </div>
        </form>
    </div>
  </div>
</div>

<div class="modal animated fadeIn" id="newfolder">
  <div class="modal-dialog">
    <div class="modal-content">
        <form ng-submit="createFolder()">
            <div class="modal-header">
              <button type="button" class="close" data-dismiss="modal">
                  <span aria-hidden="true">&times;</span>
                  <span class="sr-only">{{"close" | translate}}</span>
              </button>
              <h4 class="modal-title">{{'new_folder' | translate}}</h4>
            </div>
            <div class="modal-body">
              <label class="radio">{{'folder_name' | translate}}</label>
              <input class="form-control" ng-model="singleSelection().tempModel.name" autofocus="autofocus">
              <div ng-include data-src="'error-bar'" class="clearfix"></div>
            </div>
            <div class="modal-footer">
              <button type="button" class="btn btn-default" data-dismiss="modal" ng-disabled="apiMiddleware.apiHandler.inprocess">{{"cancel" | translate}}</button>
              <button type="submit" class="btn btn-primary" ng-disabled="apiMiddleware.apiHandler.inprocess">{{'create' | translate}}</button>
            </div>
        </form>
    </div>
  </div>
</div>

<div class="modal animated fadeIn" id="uploadfile">
  <div class="modal-dialog">
    <div class="modal-content">
        <form>
            <div class="modal-header">
              <button type="button" class="close" data-dismiss="modal">
                  <span aria-hidden="true">&times;</span>
                  <span class="sr-only">{{"close" | translate}}</span>
              </button>
              <h4 class="modal-title">{{"upload_files" | translate}}</h4>
            </div>
            <div class="modal-body">
              <label class="radio">
                {{"files_will_uploaded_to" | translate}} 
                <b>/{{fileNavigator.currentPath.join('/')}}</b>
              </label>
              <button class="btn btn-default btn-block" ngf-select="$parent.addForUpload($files)" ngf-multiple="true">
                {{"select_files" | translate}}
              </button>
              
              <div class="upload-list">
                <ul class="list-group">
                  <li class="list-group-item" ng-repeat="(index, uploadFile) in $parent.uploadFileList">
                    <button class="btn btn-sm btn-danger pull-right" ng-click="$parent.removeFromUpload(index)">
                        &times;
                    </button>
                    <h5 class="list-group-item-heading">{{uploadFile.name}}</h5>
                    <p class="list-group-item-text">{{uploadFile.size | humanReadableFileSize}}</p>
                  </li>
                </ul>
                <div ng-show="apiMiddleware.apiHandler.inprocess">
                  <em>{{"uploading" | translate}}... {{apiMiddleware.apiHandler.progress}}%</em>
                  <div class="progress mb0">
                    <div class="progress-bar active" role="progressbar" aria-valuenow="{{apiMiddleware.apiHandler.progress}}" aria-valuemin="0" aria-valuemax="100" style="width: {{apiMiddleware.apiHandler.progress}}%"></div>
                  </div>
                </div>
              </div>
              <div ng-include data-src="'error-bar'" class="clearfix"></div>
            </div>
            <div class="modal-footer">
              <div>
                  <button type="button" class="btn btn-default" data-dismiss="modal">{{"cancel" | translate}}</button>
                  <button type="submit" class="btn btn-primary" ng-disabled="!$parent.uploadFileList.length || apiMiddleware.apiHandler.inprocess" ng-click="uploadFiles()">{{'upload' | translate}}</button>
              </div>
            </div>
        </form>
    </div>
  </div>
</div>

<div class="modal animated fadeIn" id="changepermissions">
  <div class="modal-dialog">
    <div class="modal-content">
        <form ng-submit="changePermissions()">
            <div class="modal-header">
              <button type="button" class="close" data-dismiss="modal">
                  <span aria-hidden="true">&times;</span>
                  <span class="sr-only">{{"close" | translate}}</span>
              </button>
              <h4 class="modal-title">{{'change_permissions' | translate}}</h4>
            </div>
            <div class="modal-body">
              <table class="table mb0">
                  <thead>
                      <tr>
                          <th>{{'permissions' | translate}}</th>
                          <th class="col-xs-1 text-center">{{'read' | translate}}</th>
                          <th class="col-xs-1 text-center">{{'write' | translate}}</th>
                          <th class="col-xs-1 text-center">{{'exec' | translate}}</th>
                      </tr>
                  </thead>
                  <tbody>
                      <tr ng-repeat="(permTypeKey, permTypeValue) in temp.tempModel.perms">
                          <td>{{permTypeKey | translate}}</td>
                          <td ng-repeat="(permKey, permValue) in permTypeValue" class="col-xs-1 text-center" ng-click="main()">
                              <label class="col-xs-12">
                                <input type="checkbox" ng-model="temp.tempModel.perms[permTypeKey][permKey]">
                              </label>
                          </td>
                      </tr>
                </tbody>
              </table>
              <div class="checkbox" ng-show="config.enablePermissionsRecursive && selectionHas('dir')">
                <label>
                  <input type="checkbox" ng-model="temp.tempModel.recursive"> {{'recursive' | translate}}
                </label>
              </div>
              <div class="clearfix mt10">
                  <span class="label label-primary pull-left" ng-hide="temp.multiple">
                    {{'original' | translate}}: 
                    {{temp.model.perms.toCode(selectionHas('dir') ? 'd':'-')}} 
                    ({{temp.model.perms.toOctal()}})
                  </span>
                  <span class="label label-primary pull-right">
                    {{'changes' | translate}}: 
                    {{temp.tempModel.perms.toCode(selectionHas('dir') ? 'd':'-')}} 
                    ({{temp.tempModel.perms.toOctal()}})
                  </span>
              </div>
              <div ng-include data-src="'error-bar'" class="clearfix"></div>
            </div>
            <div class="modal-footer">
              <button type="button" class="btn btn-default" data-dismiss="modal">{{"cancel" | translate}}</button>
              <button type="submit" class="btn btn-primary" ng-disabled="">{{'change' | translate}}</button>
            </div>
        </form>
    </div>
  </div>
</div>

<div class="modal animated fadeIn" id="selector" ng-controller="ModalFileManagerCtrl">
  <div class="modal-dialog">
    <div class="modal-content">
      <div class="modal-header">
        <button type="button" class="close" data-dismiss="modal">
            <span aria-hidden="true">&times;</span>
            <span class="sr-only">{{"close" | translate}}</span>
        </button>
        <h4 class="modal-title">{{"select_destination_folder" | translate}}</h4>
      </div>
      <div class="modal-body">
        <div>
            <div ng-include="config.tplPath + '/current-folder-breadcrumb.jsp'"></div>
            <div ng-include="config.tplPath + '/main-table-modal.jsp'"></div>
            <hr />
            <button class="btn btn-sm btn-default" ng-click="selectCurrent()">
                <i class="glyphicon"></i> {{"select_this" | translate}}
            </button>
        </div>
      </div>
      <div class="modal-footer">
        <button type="button" class="btn btn-default" data-dismiss="modal" ng-disabled="apiMiddleware.apiHandler.inprocess">{{"close" | translate}}</button>
      </div>
    </div>
  </div>
</div>

<script type="text/ng-template" id="path-selector">
  <div class="panel panel-primary mt10 mb0">
    <div class="panel-body">
        <div class="detail-sources">
          <div class="like-code mr5"><b>{{"selection" | translate}}:</b>
            <span ng-include="'selected-files-msg'"></span>
          </div>
        </div>
        <div class="detail-sources">
          <div class="like-code mr5">
            <b>{{"destination" | translate}}:</b> {{ getSelectedPath() }}
          </div>
          <a href="" class="label label-primary" ng-click="openNavigator(fileNavigator.currentPath)">
            {{'change' | translate}}
          </a>
        </div>
    </div>
  </div>
</script>

<script type="text/ng-template" id="error-bar">
  <div class="label label-danger error-msg pull-left animated fadeIn" ng-show="apiMiddleware.apiHandler.error">
    <i class="glyphicon glyphicon-remove-circle"></i>
    <span>{{apiMiddleware.apiHandler.error}}</span>
  </div>
</script>

<script type="text/ng-template" id="selected-files-msg">
  <span ng-show="temps.length == 1">
    {{singleSelection().model.name}}
  </span>
  <span ng-show="temps.length > 1">
    {{'these_elements' | translate:totalSelecteds()}}
    <a href="" class="label label-primary" ng-click="showDetails = !showDetails">
      {{showDetails ? '-' : '+'}} {{'details' | translate}}
    </a>
  </span>
  <div ng-show="temps.length > 1 &amp;&amp; showDetails">
    <ul class="selected-file-details">
      <li ng-repeat="tempItem in temps">
        <b>{{tempItem.model.name}}</b>
      </li>
    </ul>
  </div>
</script>
