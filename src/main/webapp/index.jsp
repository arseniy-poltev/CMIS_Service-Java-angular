

<!DOCTYPE html>
<html ng-app="FileManagerApp">

<head>
    <meta charset="utf-8" />
    <title>Phenix Share System</title>
    <base href="/">

    <link href="https://fonts.googleapis.com/css?family=Roboto+Condensed:300,400,700" rel="stylesheet" type="text/css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/public/assets/css/bootstrap.min.css" />
    <link href="${pageContext.request.contextPath}/public/assets/css/app.css" rel="stylesheet" />
    <script src="${pageContext.request.contextPath}/public/assets/js/jquery.min.js"></script>
    <script src="${pageContext.request.contextPath}/public/assets/js/bootstrap.min.js"></script>
    <script src="${pageContext.request.contextPath}/public/assets/js/angular.min.js"></script>
    <script src="${pageContext.request.contextPath}/public/assets/js/angular-route.min.js"></script>
    <script src="${pageContext.request.contextPath}/public/assets/js/angular-cookies.min.js"></script>
    <script src="${pageContext.request.contextPath}/public/assets/js/angular-translate.min.js"></script>
    <script src="${pageContext.request.contextPath}/public/assets/js/ng-file-upload.min.js"></script>
    
    <script src="${pageContext.request.contextPath}/app.js"></script>
    <script src="${pageContext.request.contextPath}/public/app/app-services/authentication.service.js"></script>
    <script src="${pageContext.request.contextPath}/public/app/app-services/flash.service.js"></script>

    <!-- Real user service that uses an api -->
    <script src="${pageContext.request.contextPath}/public/app/app-services/user.service.js"></script>

    <!-- Fake user service for demo that uses local storage -->
    <!-- <script src="app-services/user.service.local-storage.js"></script> -->

    <script src="${pageContext.request.contextPath}/public/app/home/home.controller.js"></script>
    <script src="${pageContext.request.contextPath}/public/app/login/login.controller.js"></script>
    <script src="${pageContext.request.contextPath}/public/app/register/register.controller.js"></script>
    <script src="${pageContext.request.contextPath}/public/app/admin/admin.controller.js"></script>

    <!-- <script src="src/js/app.js"></script> -->
    <script src="${pageContext.request.contextPath}/public/app/src/js/directives/directives.js"></script>
    <script src="${pageContext.request.contextPath}/public/app/src/js/filters/filters.js"></script>
    <script src="${pageContext.request.contextPath}/public/app/src/js/providers/config.js"></script>
    <script src="${pageContext.request.contextPath}/public/app/src/js/entities/chmod.js"></script>
    <script src="${pageContext.request.contextPath}/public/app/src/js/entities/item.js"></script>
    <script src="${pageContext.request.contextPath}/public/app/src/js/services/apihandler.js"></script>
    <script src="${pageContext.request.contextPath}/public/app/src/js/services/apimiddleware.js"></script>
    <script src="${pageContext.request.contextPath}/public/app/src/js/services/filenavigator.js"></script>
    <script src="${pageContext.request.contextPath}/public/app/src/js/providers/translations.js"></script>
    <script src="${pageContext.request.contextPath}/public/app/src/js/controllers/main.js"></script>
    <script src="${pageContext.request.contextPath}/public/app/src/js/controllers/selector-controller.js"></script>
    <link href="${pageContext.request.contextPath}/public/app/src/css/animations.css" rel="stylesheet">
    <link href="${pageContext.request.contextPath}/public/app/src/css/dialogs.css" rel="stylesheet">
    <link href="${pageContext.request.contextPath}/public/app/src/css/main.css" rel="stylesheet">
</head>

<body class="ng-cloak">
    <div ng-view></div>
</body>

</html>