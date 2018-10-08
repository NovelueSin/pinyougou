/*
定义品牌控制器层*/
app.controller('brandController',function ($scope, $controller,baseService) {

    /*指定集成baseController*/
    $controller("baseController",{$scope:$scope});

    /*读取品牌数据绑定到表格中*/
    $scope.findAll = function () {
        /*调用服务层查询所有品牌数据*/
        baseService.sendGet("/brand/findAll").then(function (value) {
            $scope.dataList = value.data;
        })
    }


    /** 定义搜索对象 */
    $scope.searchEntity = {};
    /** 分页查询品牌 */
    $scope.search = function(page, rows){
        baseService.findByPage("/brand/findByPage", page,
            rows, $scope.searchEntity)
            .then(function(response){
                $scope.dataList = response.data.rows;
                /** 更新总记录数 */
                $scope.paginationConf.totalItems = response.data.total;
            });
    };
    /** 添加或修改品牌 */
    $scope.saveOrUpdate = function(){
        var url = "save";
        if ($scope.entity.id){
            url = "update";
        }
        /** 发送post请求添加品牌 */
        baseService.sendPost("/brand/" + url, $scope.entity)
            .then(function(response){
                if (response.data){
                    /** 重新加载品牌数据 */
                    $scope.reload();
                }else{
                    alert("操作失败！");
                }
            });
    };
    /** 显示修改 */
    $scope.show = function(entity){
        // 把entity的json对象转化成一个新的json对象
        $scope.entity = JSON.parse(JSON.stringify(entity));
    };

    /** 定义ids数组封装删除的id */
    $scope.ids = [];
    /** 定义checkbox点击事件函数 */
    $scope.updateSelection = function($event, id){
        /** 判断checkbox是否选中 */
        if ($event.target.checked){
            $scope.ids.push(id);
        }else{
            /** 从数组中移除 */
            var idx = $scope.ids.indexOf(id);
            $scope.ids.splice(idx, 1);
        }
    };
    /** 批量删除 */
    $scope.delete = function(){
        if ($scope.ids.length > 0){
            baseService.deleteById("/brand/delete", $scope.ids)
                .then(function(response){
                    if (response.data){
                        $scope.reload();
                    }else{
                        alert("删除失败！");
                    }
                });
        }
    };
});
