/*
定义首页控制器*/
app.controller('indexController',function ($scope, baseService) {

    /*获取登录用户名*/
    $scope.showLoginName = function () {
        baseService.sendGet("/showLoginName").then(function (value) {
            /*获取响应数据*/
            $scope.loginName = value.data.loginName;
        })
    }
})
