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

    //图片数组存储结构
    $scope.goods = {goodsDesc:{ itemImages:[] }};
    // 添加图片到数组
    $scope.addPic = function () {
        $scope.goods.goodsDesc.itemImages.push($scope.picEntity);
    };

    // 数组中移除图片
    $scope.removePic = function (index) {
        $scope.goods.goodsDesc.itemImages.splice(index, 1);
    }

    // 根据父级id查找分类
    $scope.findItemCatByParentId = function (parentId, name) {
        baseService.sendGet("/itemCat/findItemCatByParentId","parentId="+parentId).then(function (value) {
            $scope[name] = value.data;
        });
    }

    /*监控 goods.category1Id变量，查询二级分类*/
    $scope.$watch('goods.category1Id',function (newValue, oldValue) {
        if (newValue) {
            /*根据选择查询二级分类*/
            $scope.findItemCatByParentId(newValue, "itemCatList2");
        }else {
            $scope.itemCatList2 = [];
        }
    });
    /*监控 goods.category1Id变量，查询二级分类*/
    $scope.$watch('goods.category2Id',function (newValue, oldValue) {
        if (newValue) {
            /*根据选择查询二级分类*/
            $scope.findItemCatByParentId(newValue, "itemCatList3");
        }else {
            $scope.itemCatList3 = [];
        }
    });

    $scope.$watch('goods.category3Id',function (newValue, oldValue) {
        if (newValue) {
            // 循环三级分类数组 List<ItemCat> : [{},{}]
            for (var i=0;i < $scope.itemCatList3.length;i++) {
                var itemCat = $scope.itemCatList3[i];
                /*判断id*/
                if(itemCat.id == newValue) {
                    $scope.goods.typeTemplatedId = itemCat.typeId;
                    break;
                }
            }
        }
    });

    /*监控goods.typeTemplateId 模板id ,查询模板对应品牌*/
    $scope.$watch('goods.typeTemplatedId',function (newValue, oldValue) {
        if(!newValue) {
            return;
        }
        baseService.findOne("/typeTemplate/findOne",newValue).then(function (response) {
            /*获取模板中的品牌列表*/
            $scope.brandIds = JSON.parse(response.data.brandIds);
            /*设置扩展属性*/
            $scope.goods.goodsDesc.customAttributeItems = JSON.parse(response.data.customAttributeItems);
        });
    });

});
