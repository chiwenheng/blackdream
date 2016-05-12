define(
    ["business/module", "business/api"], function (module) {
        "use strict";

        module.controller("userManageController", [
            "$scope", "userApi", "viewPage","confirm",
            function($scope, userApi, viewPage, confirm){
                viewPage.setViewPageTitle("用户管理");

                $scope.searchRequest = {page:1, pageSize:10};

                $scope.isDisableOptions = [
                    {isDisable:false,name:"激活"},
                    {isDisable:true,name:"冻结"}
                ];

                $scope.isDeveloperOptions = [
                    {isDeveloper:false,name:"使用者"},
                    {isDeveloper:true,name:"开发者"}
                ];

                $scope.search = function(){
                    userApi.search($scope.searchRequest).success(function(pagerResult){
                        $scope.pagerResult = pagerResult;
                    });
                };

                $scope.passwordReset = function(user){
                    if(!user.creator){
                        return;
                    }
                    confirm.open({
                        title:"密码重置",
                        message:"确定重置【" + user.userName + "】密码？",
                        confirm:function(){
                            userApi.passwordReset({id: user.id});
                        }
                    });
                };

                $scope.search();

            }
        ]);
    }
);
