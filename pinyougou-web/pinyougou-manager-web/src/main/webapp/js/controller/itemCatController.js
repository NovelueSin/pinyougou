/** 定义控制器层 */
app.controller('itemCatController', function ($scope, $controller, baseService) {

    /** 指定继承baseController */
    $controller('baseController', {$scope: $scope});

    /** 查询条件对象 */
    $scope.searchEntity = {};
    /** 分页查询(查询条件) */
    $scope.search = function (page, rows) {
        baseService.findByPage("/itemCat/findByPage", page,
            rows, $scope.searchEntity)
            .then(function (response) {
                /** 获取分页查询结果 */
                $scope.dataList = response.data.rows;
                /** 更新分页总记录数 */
                $scope.paginationConf.totalItems = response.data.total;
            });
    };

    /** 添加或修改 */
    $scope.saveOrUpdate = function () {

        /*因为是用select2得到的数据，需要处理下*/
        $scope.entity.typeId = $scope.entity.typeId.id;

        var url = "save";
        if ($scope.entity.id) {
            url = "update";
        }
        /** 发送post请求 */
        baseService.sendPost("/itemCat/" + url, $scope.entity)
            .then(function (response) {
                if (response.data) {
                    /** 重新加载数据 */
                    $scope.reload();
                } else {
                    alert("操作失败！");
                }
            });
    };

    /** 显示修改 */
    $scope.show = function (entity) {
        /** 把json对象转化成一个新的json对象 */
        $scope.entity = JSON.parse(JSON.stringify(entity));

        /*因为使用了select2所以数据需要处理一下*/
        var list = $scope.typeTemplateList.data;
        for(var i = 0; i<list.length;i++) {
            if ($scope.entity.typeId == list[i].id) {
                $scope.entity.typeId = list[i];
            }
        }

    };

    /** 批量删除 */
    $scope.delete = function () {
        if ($scope.ids.length > 0) {
            baseService.deleteById("/itemCat/delete", $scope.ids)
                .then(function (response) {
                    if (response.data) {
                        /** 重新加载数据 */
                        $scope.reload();
                    } else {
                        alert("删除失败！");
                    }
                });
        } else {
            alert("请选择要删除的记录！");
        }
    };

    /*根据上级id显示下级列表*/
    $scope.findItemCatByParentId = function (parentId) {
        baseService.sendGet("/itemCat/findItemCatByParentId", "parentId=" + parentId).then(function (value) {
            $scope.dataList = value.data;
        })
    }

    /*默认等级为1*/
    $scope.grade = 1;
    $scope.parentId = 0;
    /*查询下级*/
    $scope.selectList = function (entity, grade) {
        $scope.grade = grade;
        $scope.parentId = entity.id;
        if (grade == 1) {
            $scope.itemCat_1 = null;
            $scope.itemCat_2 = null;
        }
        if (grade == 2) {
            $scope.itemCat_1 = entity;
            $scope.itemCat_2 = null;
        }
        if (grade == 3) {
            $scope.itemCat_2 = entity;
        }
        /*查询下级列表*/
        $scope.findItemCatByParentId(entity.id);
    };

    /*查询可用类型模板*/
    $scope.findTypeTemplateList = function () {
        baseService.sendGet("/typeTemplate/findTypeTemplateList").then(function (value) {
            $scope.typeTemplateList = {data: value.data};
        });
    };

    /*初始化新建分类*/
    $scope.initEntity = function () {
        $scope.entity = {parentId: $scope.parentId};
    }
});