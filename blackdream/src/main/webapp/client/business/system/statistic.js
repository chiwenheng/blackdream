define(
    ["business/module", "business/api"],
    function (module) {
        "use strict";

        var api = {};

        module.controller("statisticController", [
            "$scope", "systemApi", "viewPage",
            function($scope, systemApi, viewPage){
                viewPage.setViewPageTitle("数据统计");

                systemApi.statistic({}).success(function(result){
                    $scope.result = result;
                });

                api.generatorInstanceIsRun = false;
                api.generatorIsRun = false;
                api.dynamicModelIsRun = false;
                api.templateIsRun = false;
                api.templateStrategyIsRun = false;

                $scope.select = function(domain){
                    if(!api[domain + "IsRun"]){
                        api[domain + "Fun"]();
                        api[domain + "IsRun"] = true;
                    }
                }
            }
        ]);

        module.controller("statisticUserController", [
            "$scope", "userApi",
            function($scope, userApi){
                $scope.searchRequest = {page:1, pageSize:10};

                $scope.isDisableOptions = [
                    {isDisable:false,name:"激活"},
                    {isDisable:true,name:"冻结"}
                ];

                $scope.isDeveloperOptions = [
                    {isDeveloper:false,name:"使用者"},
                    {isDeveloper:true,name:"开发者"}
                ];

                $scope.search = function(searchRequest){
                    if(searchRequest){
                        for(var k in searchRequest){
                            $scope.searchRequest[k] = searchRequest[k];
                        }
                    }
                    userApi.search($scope.searchRequest).success(function(pagerResult){
                        $scope.pagerResult = pagerResult;
                    });
                };

                $scope.search();
            }
        ]);

        module.controller("statisticGeneratorInstanceController", [
            "$scope", "generatorInstanceApi",
            function($scope, generatorInstanceApi){
                $scope.searchRequest = {page:1, pageSize:10};

                $scope.search = function(searchRequest){
                    if(searchRequest){
                        for(var k in searchRequest){
                            $scope.searchRequest[k] = searchRequest[k];
                        }
                    }
                    generatorInstanceApi.search($scope.searchRequest).success(function(pagerResult){
                        $scope.pagerResult = pagerResult;
                    });
                };

                api.generatorInstanceFun = $scope.search;
            }
        ]);

        module.controller("statisticGeneratorController", [
            "$scope", "generatorApi",
            function($scope, generatorApi){
                $scope.searchRequest = {page:1, pageSize:10};

                $scope.statuses = [
                    {isOpen:true,name:"发布"},
                    {isOpen:false,name:"维护"}
                ];

                $scope.search = function(searchRequest){
                    if(searchRequest){
                        for(var k in searchRequest){
                            $scope.searchRequest[k] = searchRequest[k];
                        }
                    }
                    generatorApi.search($scope.searchRequest).success(function(pagerResult){
                        $scope.pagerResult = pagerResult;
                    });
                };

                api.generatorFun = $scope.search;
            }
        ]);

        module.controller("statisticDynamicModelController", [
            "$scope", "dynamicModelApi",
            function($scope, dynamicModelApi){
                $scope.searchRequest = {page:1, pageSize:10};

                $scope.search = function(searchRequest){
                    if(searchRequest){
                        for(var k in searchRequest){
                            $scope.searchRequest[k] = searchRequest[k];
                        }
                    }
                    dynamicModelApi.search($scope.searchRequest).success(function(pagerResult){
                        $scope.pagerResult = pagerResult;
                    });
                };

                api.dynamicModelFun = $scope.search;
            }
        ]);

        module.controller("statisticTemplateController", [
            "$scope", "templateApi",
            function($scope, templateApi){
                $scope.searchRequest = {page:1, pageSize:10};

                $scope.search = function(searchRequest){
                    if(searchRequest){
                        for(var k in searchRequest){
                            $scope.searchRequest[k] = searchRequest[k];
                        }
                    }
                    templateApi.search($scope.searchRequest).success(function(pagerResult){
                        $scope.pagerResult = pagerResult;
                    });
                };

                api.templateFun = $scope.search;
            }
        ]);

        module.controller("statisticTemplateStrategyController", [
            "$scope", "templateStrategyApi",
            function($scope, templateStrategyApi){
                $scope.searchRequest = {page:1, pageSize:10};

                $scope.search = function(searchRequest){
                    if(searchRequest){
                        for(var k in searchRequest){
                            $scope.searchRequest[k] = searchRequest[k];
                        }
                    }
                    templateStrategyApi.search($scope.searchRequest).success(function(pagerResult){
                        $scope.pagerResult = pagerResult;
                    });
                };

                api.templateStrategyFun = $scope.search;
            }
        ]);

    }
);