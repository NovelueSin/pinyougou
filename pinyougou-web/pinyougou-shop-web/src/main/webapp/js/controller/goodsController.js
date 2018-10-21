/** 定义控制器层 */
app.controller('goodsController', function ($scope, $controller, baseService) {

    /*定义商品状态数组*/
    $scope.status = ['未审核','已审核','审核未通过','关闭'];


    /** 指定继承baseController */
    $controller('baseController', {$scope: $scope});

    /** 查询条件对象 */
    $scope.searchEntity = {};
    /** 分页查询(查询条件) */
    $scope.search = function (page, rows) {
        baseService.findByPage("/goods/findByPage", page,
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

        /*获取富文本编辑器的内容*/
        $scope.goods.goodsDesc.introduction = editor.html();

        var url = "save";
        if ($scope.goods.id) {
            url = "update";
        }
        /** 发送post请求 */
        baseService.sendPost("/goods/" + url, $scope.goods)
            .then(function (response) {
                if (response.data) {
                    alert("保存成功");
                    /*清空表单*/
                    $scope.goods = {};
                    /*清空富文本*/
                    editor.html('');
                } else {
                    alert("操作失败！");
                }
            });
    };


    /*上传图片*/
    $scope.uploadFile = function () {
        baseService.uploadFile().then(function (value) {
            /*如果上传成功*/
            if (value.data.status == 200) {
                /*设置图片访问地址*/
                $scope.picEntity.url = value.data.url;
            } else {
                alert("上传失败")
            }
        });
    }


    /** 显示修改 */
    $scope.show = function (entity) {
        /** 把json对象转化成一个新的json对象 */
        $scope.entity = JSON.parse(JSON.stringify(entity));
    };

    /** 批量删除 */
    $scope.delete = function () {
        if ($scope.ids.length > 0) {
            baseService.deleteById("/goods/delete", $scope.ids)
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

    //图片数组存储结构
    $scope.goods = {goodsDesc: {itemImages: []}};
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
        baseService.sendGet("/itemCat/findItemCatByParentId", "parentId=" + parentId).then(function (value) {
            $scope[name] = value.data;
        });
    }

    /*监控 goods.category1Id变量，查询二级分类*/
    $scope.$watch('goods.category1Id', function (newValue, oldValue) {
        if (newValue) {
            /*根据选择查询二级分类*/
            $scope.findItemCatByParentId(newValue, "itemCatList2");
        } else {
            $scope.itemCatList2 = [];
        }
    });
    /*监控 goods.category1Id变量，查询二级分类*/
    $scope.$watch('goods.category2Id', function (newValue, oldValue) {
        if (newValue) {
            /*根据选择查询二级分类*/
            $scope.findItemCatByParentId(newValue, "itemCatList3");
        } else {
            $scope.itemCatList3 = [];
        }
    });

    $scope.$watch('goods.category3Id', function (newValue, oldValue) {
        if (newValue) {
            // 循环三级分类数组 List<ItemCat> : [{},{}]
            for (var i = 0; i < $scope.itemCatList3.length; i++) {
                var itemCat = $scope.itemCatList3[i];
                /*判断id*/
                if (itemCat.id == newValue) {
                    $scope.goods.typeTemplatedId = itemCat.typeId;
                    break;
                }
            }
        }
    });

    /*监控goods.typeTemplateId 模板id ,查询模板对应品牌*/
    $scope.$watch('goods.typeTemplatedId', function (newValue, oldValue) {
        if (!newValue) {
            return;
        }
        baseService.findOne("/typeTemplate/findOne", newValue).then(function (response) {
            /*获取模板中的品牌列表*/
            $scope.brandIds = JSON.parse(response.data.brandIds);
            /*设置扩展属性*/
            $scope.goods.goodsDesc.customAttributeItems = JSON.parse(response.data.customAttributeItems);
        });
        /*查询该模板对应的规格和规格选项*/
        baseService.findOne("/typeTemplate/findSpecByTemplateId", newValue).then(function (response) {
            $scope.specList = response.data;
        });
    });

    /*定义数据存储结构*/
    $scope.goods = {goodsDesc:{itemImages:[],specificationItems:[]}};
    /*定义修改规格选项方法*/
    $scope.updateSpecAttr = function ($event, name, value) {
        /*根据json对象的key 到json数组中搜索key值对应的对象*/
        var obj = $scope.searchJsonByKey($scope.goods.goodsDesc.specificationItems,'attributeName',name);
        /*判断是否为空*/
        if (obj) {
            if ($event.target.checked) {
                /*添加该规格选项到数组中*/
                obj.attributeValue.push(value);
            }else {
                /*取消勾选*/
                obj.attributeValue.splice(obj.attributeValue.indexOf(value), 1);
                /*如果选项取消，将此记录删除*/
                if (obj.attributeValue.length == 0) {
                    $scope.goods.goodsDesc.specificationItems.splice($scope.goods.goodsDesc.specificationItems.indexOf(obj), 1);
                }
            }
        }else {
            $scope.goods.goodsDesc.specificationItems.push({"attributeName":name,
            "attributeValue":[value]})
        }
    }

    /*从json数组中根据key查询指定的json对象*/
    $scope.searchJsonByKey = function (jsonArr, key, value) {
        /*迭代json 数组*/
        for(var i = 0; i< jsonArr.length;i++) {
            if (jsonArr[i][key] == value) {
                return jsonArr[i];
            }
        }
    }

    /*创建sku商品方法*/
    $scope.createItems  = function () {
        /*定义sku数组，初始化*/
        $scope.goods.items = [{spec:{},prive:0,num:9999,status:'0',isDefault:'0'}];
        /*定义选中的规格数组*/
        var specItems = $scope.goods.goodsDesc.specificationItems;
        for(var i = 0;i<specItems.length;i++) {
            /*扩充sku数组*/
            $scope.goods.items = swapItems($scope.goods.items,specItems[i].attributeName,
                specItems[i].attributeValue);
        }
    }

    /*扩充sku数组方法*/
    var swapItems = function (items, attributeName, attributeValue) {
        /*创建新的sku数组*/
        var newItems = new Array();
        for (var i = 0; i< items.length;i++) {
            var item = items[i];
            for(var j = 0 ;j<attributeValue.length;j++) {
                /*克隆旧的sku商品，产生新的sku商品*/
                var newItem = JSON.parse(JSON.stringify(item));
                newItem.spec[attributeName] = attributeValue[j];
                newItems.push(newItem);
            }
        }
        return newItems;
    }

    /*商家商品上下架修改*/
    $scope.updateMarketable = function (status) {
        if ($scope.ids.length>0) {
            baseService.sendGet("/goods/updateMarketable","ids="+$scope.ids+"&status="+status).then(
                function (value) {
                    if (value.data) {
                        alert("操作成功")
                        $scope.reload();
                        $scope.ids=[];
                    }else {
                        alert("操作失败")
                    }
                }
            );
        }else {
            alert("选择要操作的商品...")
        }
    }

});
