<div class="jumbotron">
    <div class="container">
        <div class="col-sm-8 col-sm-offset-2">
            <div ng-class="{ 'alert': flash, 'alert-success': flash.type === 'success', 'alert-danger': flash.type === 'error' }"
                ng-if="flash" ng-bind="flash.message"></div>
            <div class="col-md-6 col-md-offset-3">
                <h2>Login</h2>
                <form name="form" ng-submit="vm.login()" role="form">
                    <div class="form-group"
                        ng-class="{ 'has-error': form.username.$dirty && form.username.$error.required || form.username.$error.pattern }">
                        <label for="username">Email</label>
                        <input type="email" name="username" id="username" class="form-control" ng-model="vm.username"
                            required style="padding:10px" ng-pattern="/^[a-z]+[a-z0-9._]+@[a-z]+\.[a-z.]{2,5}$/" />
                        <span ng-show="form.username.$dirty && form.username.$error.required"
                            class="help-block">Email is required</span>
                        <span class="help-block" ng-show="form.username.$dirty && form.username.$error.pattern">
                            invalid email address
                        </span>
                    </div>
                    <div class="form-group"
                        ng-class="{ 'has-error': form.password.$dirty && form.password.$error.required }">
                        <label for="password">Password</label>
                        <input type="password" name="password" id="password" class="form-control" ng-model="vm.password"
                            required style="padding:10px" />
                        <span ng-show="form.password.$dirty && form.password.$error.required"
                            class="help-block">Password is required</span>
                    </div>
                    <div class="form-actions">
                        <button type="submit" ng-disabled="form.$invalid || vm.dataLoading"
                            class="btn btn-primary">Login</button>
                        <img ng-if="vm.dataLoading"
                            src="data:image/gif;base64,R0lGODlhEAAQAPIAAP///wAAAMLCwkJCQgAAAGJiYoKCgpKSkiH/C05FVFNDQVBFMi4wAwEAAAAh/hpDcmVhdGVkIHdpdGggYWpheGxvYWQuaW5mbwAh+QQJCgAAACwAAAAAEAAQAAADMwi63P4wyklrE2MIOggZnAdOmGYJRbExwroUmcG2LmDEwnHQLVsYOd2mBzkYDAdKa+dIAAAh+QQJCgAAACwAAAAAEAAQAAADNAi63P5OjCEgG4QMu7DmikRxQlFUYDEZIGBMRVsaqHwctXXf7WEYB4Ag1xjihkMZsiUkKhIAIfkECQoAAAAsAAAAABAAEAAAAzYIujIjK8pByJDMlFYvBoVjHA70GU7xSUJhmKtwHPAKzLO9HMaoKwJZ7Rf8AYPDDzKpZBqfvwQAIfkECQoAAAAsAAAAABAAEAAAAzMIumIlK8oyhpHsnFZfhYumCYUhDAQxRIdhHBGqRoKw0R8DYlJd8z0fMDgsGo/IpHI5TAAAIfkECQoAAAAsAAAAABAAEAAAAzIIunInK0rnZBTwGPNMgQwmdsNgXGJUlIWEuR5oWUIpz8pAEAMe6TwfwyYsGo/IpFKSAAAh+QQJCgAAACwAAAAAEAAQAAADMwi6IMKQORfjdOe82p4wGccc4CEuQradylesojEMBgsUc2G7sDX3lQGBMLAJibufbSlKAAAh+QQJCgAAACwAAAAAEAAQAAADMgi63P7wCRHZnFVdmgHu2nFwlWCI3WGc3TSWhUFGxTAUkGCbtgENBMJAEJsxgMLWzpEAACH5BAkKAAAALAAAAAAQABAAAAMyCLrc/jDKSatlQtScKdceCAjDII7HcQ4EMTCpyrCuUBjCYRgHVtqlAiB1YhiCnlsRkAAAOwAAAAAAAAAAAA==" />
                        <a href="${pageContext.request.contextPath}/#!/register" class="btn btn-link">Register</a>
                    </div>
                </form>
            </div>
        </div>
    </div>
</div>
