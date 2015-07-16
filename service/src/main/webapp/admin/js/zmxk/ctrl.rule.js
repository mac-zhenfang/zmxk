zmxk.controller('EventRuleCtrl', [
		'$scope',
		'userService',
		'eventService',
		'eventRuleService',
		'$interval',
		'$timeout',
		'$routeParams',
		function($scope, userService, eventService, eventRuleService, $interval, $timeout,
				$routeParams) {
			$scope.updateLabel = "修改";
			$scope.deleteLabel = "删除";
			// FIXME FAKE DATA
			// 1. get EventTypes
			// 2. make EventTypes select box and searchable
			// 3. get all series from the EventTypes
			// 4. make series select box and searchable
			// 5. add one default - 单次事件into series select box
			$scope.eventRules = [];
			$scope.eventTypeList = [];
			$scope.seriesList= [];
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
			} ]
			var init = function() {
				$scope.eventRules = [ {
					id : "mac-test-event-rule-1",
					name : "脚踏拉力赛规则",
					eventType : "脚踏拉力赛",
					serieName : "预赛",
					rank : 1,
					rankName : "1",
					credit : 100,
					existed : true,
					changed : false,
					showInput : false
				}, {
					id : "mac-test-event-rule-2",
					name : "脚踏拉力赛规则",
					eventType : "脚踏拉力赛",
					serieName : "预赛",
					rank : 2,
					rankName : "2",
					credit : 50,
					existed : true,
					changed : false,
					showInput : false
				}, {
					id : "mac-test-event-rule-3",
					name : "脚踏拉力赛规则",
					eventType : "脚踏拉力赛",
					serieName : "预赛",
					rank : 0,
					rankName : ">3",
					credit : 10,
					existed : true,
					changed : false,
					showInput : false
				}, {
					id : "mac-test-event-rule-4",
					name : "脚踏拉力赛规则",
					eventType : "脚踏拉力赛",
					serieName : "季度复赛",
					rank : 1,
					rankName : "1",
					credit : 400,
					existed : true,
					changed : false,
					showInput : false
				}, {
					id : "mac-test-event-rule-5",
					name : "脚踏拉力赛规则",
					eventType : "脚踏拉力赛",
					serieName : "季度复赛",
					rank : 2,
					rankName : "2",
					credit : 200,
					existed : true,
					changed : false,
					showInput : false
				}, {
					id : "mac-test-event-rule-6",
					name : "脚踏拉力赛规则",
					eventType : "脚踏拉力赛",
					serieName : "季度复赛",
					rank : 0,
					rankName : ">3",
					credit : 200,
					existed : true,
					changed : false,
					showInput : false
				}, {
					id : "mac-test-event-rule-7",
					name : "最有活力小选手",
					eventType : "脚踏拉力赛",
					serieName : "季度复赛",
					rank : null,
					rankName : "单次事件",
					credit : 300,
					existed : true,
					changed : false,
					showInput : false
				}, {
					id : "mac-test-event-rule-8",
					name : "单圈最快",
					eventType : "脚踏拉力赛",
					serieName : "季度复赛",
					rankName : "单次事件",
					credit : 200,
					existed : true,
					changed : false,
					showInput : false
				} ]
			}
			init();

			$scope.createNewRule = function() {
				var newRule = {
					existed : false,
					changed : false,
					showInput : true
				};
				$scope.eventRules.push(newRule);
			}

			// FIXME can not use id, need to use index
			$scope.updateRule = function(ruleIndex) {
				console.log(ruleIndex)
				var updateRule;
				$scope.newRules = [];
				angular.forEach($scope.eventRules, function(rule, index) {
					// console.log(index + "~~~" + ruleIndex);
					if (index == ruleIndex) {
						updateRule = angular.copy(rule);
						console.log(updateRule);
						$scope.newRules.push(updateRule);
					} else {
						$scope.newRules.push(angular.copy(rule));
					}
				});
				if (updateRule.existed) {
					updateRule.changed = !updateRule.changed;
				}
				updateRule.showInput = !updateRule.showInput;
				$scope.eventRules = $scope.newRules;
				console.log($scope.eventRules);
				if (updateRule.changed) {
					$scope.updateLabel = "确定";
				} else {
					$scope.updateLabel = "修改";
				}
				// console.log($scope.eventRules);
			}

			$scope.deleteRule = function(ruleIndex) {
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

			$scope.saveRules = function() {

				// create all existed == false
				// update all changed == true
			}

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
			}

			$scope.saveRule = function(ruleIndex, rule) {
				if(rule.id){
					ruleService.update(rule).then(function(data) {
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
								updateRule = angular.copy(rule);
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
			}
		} ]);

zmxk.controller('CreditRecordCtrl', [
		'$scope',
		'userService',
		'eventService',
		'$interval',
		'$timeout',
		'$routeParams',
		function($scope, userService, eventService, $interval, $timeout,
				$routeParams) {

		} ]);
