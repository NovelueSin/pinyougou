/** 定义控制器层 */
app.controller('goodsController', function($scope, $controller, baseService){

    /** 指定继承baseController */
    $controller('baseController',{$scope:$scope});

    /** 查询条件对象 */
    $scope.searchEntity = {};
    /** 分页查询(查询条件) */
    $scope.search = function(page, rows){
        baseService.findByPage("/goods/findByPage", page,
			rows, $scope.searchEntity)
            .then(function(response){
                /** 获取分页查询结果 */
                $scope.dataList = response.data.rows;
                /** 更新分页总记录数 */
                $scope.paginationConf.totalItems = response.data.total;
            });
    };

    /** 添加或修改 */
    $scope.saveOrUpdate = function(){

        /*获取富文本编辑器的内容*/
        $scope.goods.goodsDesc.introduction = editor.html();

        var url = "save";
        if ($scope.goods.id){
            url = "update";
        }
        /** 发送post请求 */
        baseService.sendPost("/goods/" + url, $scope.goods)
            .then(function(response){
                if (response.data){
                    alert("保存成功");
                    /*清空表单*/
                    $scope.goods = {};
                    /*清空富文本*/
                    editor.html('');
                }else{
                    alert("操作失败！");
                }
            });
    };

    /*上传图片*/
    $scope.uploadFile = function () {
        baseService.uploadFile().then(function (value) {
           /*如果上传成功*/
           if(value.data.status == 200) {
               /*设置图片访问地址*/
               $scope.picEntity.url = value.data.url;
           }else {
               alert("上传失败")
           }
        });
    }


    /** 显示修改 */
    $scope.show = function(entity){
       /** 把json对象转化成一个新的json对象 */
       $scope.entity = JSON.parse(JSON.stringify(entity));
    };

    /** 批量删除 */
    $scope.delete = function(){
        if ($scope.ids.length > 0){
            baseService.deleteById("/goods/delete", $scope.ids)
                .then(function(response){
                    if (response.data){
                        /** 重新加载数据 */
                        $scope.reload();
                    }else{
                        alert("删除失败！");
                    }
                });
        }else{
            alert("请选择要删除的记录！");
        }
    };
});