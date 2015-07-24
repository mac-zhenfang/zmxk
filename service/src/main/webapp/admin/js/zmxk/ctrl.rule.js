zmxk.controller('EventRuleCtrl', [
		'$scope',
		'userService',
		'eventService',
		'eventTypeService',
		'serieService',
		'eventRuleService',
		'$interval',
		'$timeout',
		'$routeParams','$q',
		function($scope, userService, eventService, eventTypeService, serieService, eventRuleService, $interval, $timeout,
				$routeParams,  $q) {
			$scope.deleteLabel = "删除";
			$scope.eventRules = [];
			$scope.seriesMap = new Map();
			$scope.rank_option = [ {
				value : 1,
				label : "1"
			}, {
				value : 2,
				label : "2"
			}, {
				value : 3,
				label : "3"
			}, {
				value : 0,
				label : "其他"
			}, {
				value : null,
				label : "单次事件"
			} ];
			$scope.stages = [ {
				value : 1,
				label : "预赛"
			}, {
				value : 2,
				label : "季度赛"
			}, {
				value : 3,
				label : "年度赛"
			}, {
				value : null,
				label : "------"
			} ];
			
			var init = function() {
				eventTypeService.list().then(function(data) {
					$scope.eventTypeList = data;
//					$scope.eventTypeList = [ {
//						id : "eventTypeId1",
//						name : "脚踏拉力赛"
//					}, {
//						id : "eventTypeId2",
//						name : "手摇拉力赛"
//					} ];
					var getSeries = function() {
						var defer = $q.defer(); 
						var recievedSerieMaps = 0;
						angular.forEach($scope.eventTypeList, function(eventType, index) {
							serieService.list(eventType.id).then(function(data) {
								data.unshift({id: null, name: "---------"});
								console.log(data);
								$scope.seriesMap.set(eventType.id, data);
//								if(eventType.id=="eventTypeId1"){
//									var data = [{id: "serieId11", name: "serieName11"},{id: "serieId12", name: "serieName12"}];
//									data.unshift({id: null, name: "---------"});
//									$scope.seriesMap.set(eventType.id, data);
//								} else {
//									var data = [{id: "serieId21", name: "serieName21"},{id: "serieId22", name: "serieName22"}];
//									data.unshift({id: null, name: "---------"});
//									$scope.seriesMap.set(eventType.id, data);
//								}
								recievedSerieMaps++;
								if($scope.eventTypeList.length == recievedSerieMaps) {
									defer.resolve(recievedSerieMaps);
								}
							});
						});
						return defer.promise;
					};
					
					getSeries().then(function(returnList){

						eventRuleService.listAll().then(function(data) {
							$scope.eventRules = data;
						}, function(data){
							alert(data.data.message);
							return;
						});
					});
				}, function(data){
					alert(data.data.message);
					return;
				});
			};
			init();

			$scope.createNewRule = function() {
				var newRule = {
					showInput : true
				};
				$scope.eventRules.push(newRule);
			}

			$scope.updateRule = function(ruleIndex, rule) {
				var updateRule;
				$scope.newRules = [];
				angular.forEach($scope.eventRules, function(rule, index) {
					if (index == ruleIndex) {
						updateRule = angular.copy(rule);
						updateRule.showInput = true;
						$scope.newRules.push(updateRule);
					} else {
						$scope.newRules.push(angular.copy(rule));
					}
				});

				$scope.eventRules = $scope.newRules;
			}

			$scope.deleteRule = function(ruleIndex, rule) {
				if(rule.id){
					if(confirm("确定要删除吗？")){
						eventRuleService.remove(rule.id).then(function(data) {
							var updateRule;
							$scope.newRules = [];
							angular.forEach($scope.eventRules, function(rule, index) {
								if (index != ruleIndex) {
									updateRule = angular.copy(rule);
									$scope.newRules.push(updateRule);
								}
							});
							$scope.eventRules = $scope.newRules;
						}, function(data) {
							alert(data.data.message);
							return;
						});
					}
				} else {
					var updateRule;
					$scope.newRules = [];
					angular.forEach($scope.eventRules, function(rule, index) {
						if (index != ruleIndex) {
							updateRule = angular.copy(rule);
							$scope.newRules.push(updateRule);
						}
					});
					$scope.eventRules = $scope.newRules;
				}
			};

			$scope.saveRule = function(ruleIndex, rule) {
				if(rule.id){
					eventRuleService.update(rule).then(function(data) {
						var updateRule;
						$scope.newRules = [];
						angular.forEach($scope.eventRules, function(rule, index) {
							if (index == ruleIndex) {
								updateRule = angular.copy(data);
								$scope.newRules.push(updateRule);
							} else {
								$scope.newRules.push(angular.copy(rule));
							}
						});

						updateRule.showInput = false;
						$scope.eventRules = $scope.newRules;
					}, function(data){
						alert(data.data.message);
						return;
					});
				} else {
					eventRuleService.create(rule).then(function(data) {
						var updateRule;
						$scope.newRules = [];
						angular.forEach($scope.eventRules, function(rule, index) {
							if (index == ruleIndex) {
								updateRule = angular.copy(data);
								$scope.newRules.push(updateRule);
							} else {
								$scope.newRules.push(angular.copy(rule));
							}
						});

						updateRule.showInput = false;
						$scope.eventRules = $scope.newRules;
					}, function(data){
						alert(data.data.message);
						return;
					});
				}
			};

		} ]);

zmxk.controller('GeneralRuleCtrl', [
		'$scope',
		'userService',
		'eventService',
		'ruleService',
		'$interval',
		'$timeout',
		'$routeParams',
		function($scope, userService, eventService, ruleService, $interval, $timeout,
				$routeParams) {
			$scope.updateLabel = "修改";
			$scope.deleteLabel = "删除";
			$scope.generalRules = [];
			var init = function() {
				ruleService.listAll().then(function(data) {
					$scope.generalRules = data;
				});
			};
			init();

			$scope.createNewRule = function() {
				var newRule = {
					showInput : true
				};
				$scope.generalRules.push(newRule);
			}

			$scope.updateRule = function(ruleIndex, rule) {
				var updateRule;
				$scope.newRules = [];
				angular.forEach($scope.generalRules, function(rule, index) {
					if (index == ruleIndex) {
						updateRule = angular.copy(rule);
						$scope.newRules.push(updateRule);
					} else {
						$scope.newRules.push(angular.copy(rule));
					}
				});

				updateRule.showInput = true;
				$scope.generalRules = $scope.newRules;
			}

			$scope.deleteRule = function(ruleIndex, rule) {
				if(rule.id){
					if(confirm("确定要删除吗？")){
						ruleService.remove(rule.id).then(function(data) {
							var updateRule;
							$scope.newRules = [];
							angular.forEach($scope.generalRules, function(rule, index) {
								if (index != ruleIndex) {
									updateRule = angular.copy(rule);
									$scope.newRules.push(updateRule);
								}
							});
							$scope.generalRules = $scope.newRules;
						}, function(data) {
							alert(data.data.message);
							return;
						});
					}
				} else {
					var updateRule;
					$scope.newRules = [];
					angular.forEach($scope.generalRules, function(rule, index) {
						if (index != ruleIndex) {
							updateRule = angular.copy(rule);
							$scope.newRules.push(updateRule);
						}
					});
					$scope.generalRules = $scope.newRules;
				}
			}

			$scope.saveRule = function(ruleIndex, rule) {
				if(rule.id){
					ruleService.update(rule).then(function(data) {
						var updateRule;
						$scope.newRules = [];
						angular.forEach($scope.generalRules, function(rule, index) {
							if (index == ruleIndex) {
								updateRule = angular.copy(data);
								$scope.newRules.push(updateRule);
							} else {
								$scope.newRules.push(angular.copy(rule));
							}
						});

						updateRule.showInput = false;
						$scope.generalRules = $scope.newRules;
					}, function(data){
						alert(data.data.message);
						return;
					});
				} else {
					ruleService.create(rule).then(function(data) {
						var updateRule;
						$scope.newRules = [];
						angular.forEach($scope.generalRules, function(rule, index) {
							if (index == ruleIndex) {
								updateRule = angular.copy(data);
								$scope.newRules.push(updateRule);
							} else {
								$scope.newRules.push(angular.copy(rule));
							}
						});

						updateRule.showInput = false;
						$scope.generalRules = $scope.newRules;
					}, function(data){
						alert(data.data.message);
						return;
					});
				}
			};
		} ]);

zmxk.controller('CreditRecordCtrl', [
		'$scope',
		'userService',
		'eventService',
		'creditRecordService',
		'$interval',
		'$timeout',
		'$routeParams',
		function($scope, userService, eventService, creditRecordService, $interval, $timeout,
				$routeParams) {
			$scope.criteria = {};
			$scope.criteria.mobileNum = null;
			$scope.criteria.start = null;
			$scope.criteria.end = null;
			$scope.creditRecords = [];
			$scope.search = function() {
				var tmp = {}
				tmp.mobileNum = $scope.criteria.mobileNum;
				if ($scope.criteria.start) {
					tmp.start = $scope.criteria.start.getTime();
				} else {
					tmp.start = null;
				}
				if ($scope.criteria.end) {
					tmp.end = $scope.criteria.end.getTime();
				} else {
					tmp.end = null;
				}
				creditRecordService.listAll(tmp.mobileNum, tmp.start,
						tmp.end).then(function(data) {
					$scope.creditRecords = data;
				});
			}
			var init = function() {
				$scope.search();
			};
			init();
		} ]);
