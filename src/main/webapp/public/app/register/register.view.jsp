<div class="jumbotron">
    <div class="container">
        <div class="col-sm-8 col-sm-offset-2">
            <div ng-class="{ 'alert': flash, 'alert-success': flash.type === 'success', 'alert-danger': flash.type === 'error' }"
                ng-if="flash" ng-bind="flash.message"></div>
            <div class="col-md-6 col-md-offset-3">
                <h2>Register</h2>
                <form name="form" ng-submit="vm.register()" role="form">
                    <div class="form-group" ng-class="{ 'has-error': form.email.$dirty && form.email.$error.required || form.email.$error.pattern }">
                        <label for="email">Email</label>
                        <input type="email" name="email" id="email" class="form-control" ng-model="vm.user.email"
                            required style="padding:10px" ng-pattern="/^[a-z]+[a-z0-9._]+@[a-z]+\.[a-z.]{2,5}$/" />
                        <span ng-show="form.email.$dirty && form.email.$error.required" class="help-block">First
                            email is required</span>
                        <span class="help-block" ng-show="form.email.$dirty && form.email.$error.pattern">
                            invalid email address
                        </span>
                    </div>
                    <div class="form-group"
                        ng-class="{ 'has-error': form.firstName.$dirty && form.firstName.$error.required }">
                        <label for="username">First name</label>
                        <input type="text" name="firstName" id="firstName" class="form-control"
                            ng-model="vm.user.firstName" required style="padding:10px" />
                        <span ng-show="form.firstName.$dirty && form.firstName.$error.required" class="help-block">First
                            name is required</span>
                    </div>
                    <div class="form-group"
                        ng-class="{ 'has-error': form.lastName.$dirty && form.lastName.$error.required }">
                        <label for="username">Last name</label>
                        <input type="text" name="lastName" id="Text1" class="form-control" ng-model="vm.user.lastName"
                            required style="padding:10px" />
                        <span ng-show="form.lastName.$dirty && form.lastName.$error.required" class="help-block">Last
                            name is required</span>
                    </div>
                    <div class="form-group"
                        ng-class="{ 'has-error': form.username.$dirty && form.username.$error.required }">
                        <label for="username">Username</label>
                        <input type="text" name="username" id="username" class="form-control"
                            ng-model="vm.user.username" required style="padding:10px" />
                        <span ng-show="form.username.$dirty && form.username.$error.required"
                            class="help-block">Username is required</span>
                    </div>
                    <div class="form-group"
                        ng-class="{ 'has-error': form.password.$dirty && form.password.$error.required }">
                        <label for="password">Password</label>
                        <input type="password" name="password" id="password" class="form-control"
                            ng-model="vm.user.password" required style="padding:10px" />
                        <span ng-show="form.password.$dirty && form.password.$error.required"
                            class="help-block">Password is required</span>
                    </div>
                    <div class="form-actions">
                        <button type="submit" ng-disabled="form.$invalid || vm.dataLoading"
                            class="btn btn-primary">Register</button>
                        <img ng-if="vm.dataLoading"
                            src="data:image/gif;base64,R0lGODlhEAAQAPIAAP///wAAAMLCwkJCQgAAAGJiYoKCgpKSkiH/C05FVFNDQVBFMi4wAwEAAAAh/hpDcmVhdGVkIHdpdGggYWpheGxvYWQuaW5mbwAh+QQJCgAAACwAAAAAEAAQAAADMwi63P4wyklrE2MIOggZnAdOmGYJRbExwroUmcG2LmDEwnHQLVsYOd2mBzkYDAdKa+dIAAAh+QQJCgAAACwAAAAAEAAQAAADNAi63P5OjCEgG4QMu7DmikRxQlFUYDEZIGBMRVsaqHwctXXf7WEYB4Ag1xjihkMZsiUkKhIAIfkECQoAAAAsAAAAABAAEAAAAzYIujIjK8pByJDMlFYvBoVjHA70GU7xSUJhmKtwHPAKzLO9HMaoKwJZ7Rf8AYPDDzKpZBqfvwQAIfkECQoAAAAsAAAAABAAEAAAAzMIumIlK8oyhpHsnFZfhYumCYUhDAQxRIdhHBGqRoKw0R8DYlJd8z0fMDgsGo/IpHI5TAAAIfkECQoAAAAsAAAAABAAEAAAAzIIunInK0rnZBTwGPNMgQwmdsNgXGJUlIWEuR5oWUIpz8pAEAMe6TwfwyYsGo/IpFKSAAAh+QQJCgAAACwAAAAAEAAQAAADMwi6IMKQORfjdOe82p4wGccc4CEuQradylesojEMBgsUc2G7sDX3lQGBMLAJibufbSlKAAAh+QQJCgAAACwAAAAAEAAQAAADMgi63P7wCRHZnFVdmgHu2nFwlWCI3WGc3TSWhUFGxTAUkGCbtgENBMJAEJsxgMLWzpEAACH5BAkKAAAALAAAAAAQABAAAAMyCLrc/jDKSatlQtScKdceCAjDII7HcQ4EMTCpyrCuUBjCYRgHVtqlAiB1YhiCnlsRkAAAOwAAAAAAAAAAAA==" />
                        <a href="${pageContext.request.contextPath}/#!/login" class="btn btn-link">Cancel</a>
                    </div>
                </form>
            </div>
        </div>
    </div>
</div>
