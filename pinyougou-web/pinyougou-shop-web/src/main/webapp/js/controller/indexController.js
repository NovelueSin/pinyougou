app.controller('indexController',function ($scope, baseService) {

    /*获取用户名*/
    $scope.showLoginName = function () {
        baseService.sendGet("/showLoginName").then(function (value) {
            $scope.loginName = value.data.loginName;
        });
    };

});