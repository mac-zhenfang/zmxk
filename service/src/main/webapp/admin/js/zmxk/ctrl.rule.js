zmxk.controller('EventRuleCtrl', [
		'$scope',
		'userService',
		'eventService',
		'eventTypeService',
		'serieService',
		'eventRuleService',
		'$interval',
		'$timeout',
		'$routeParams','$q','eventSerieDefService','tagService','roundService',
		function($scope, userService, eventService, eventTypeService, serieService, eventRuleService, $interval, $timeout,
				$routeParams,  $q, eventSerieDefService, tagService, roundService) {
			$scope.deleteLabel = "删除";
			$scope.eventRules = [];
			$scope.seriesMap = {};
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
				value : null,
				label : "------"
			} ];
			$scope.tagList = [];
			$scope.roundList = [];
			var init = function() {
				eventTypeService.list().then(function(data) {
					$scope.eventTypeList = data;
					var getSeries = function() {
						var defer = $q.defer(); 
						var recievedSerieMaps = 0;
						angular.forEach($scope.eventTypeList, function(eventType, index) {
							serieService.list(eventType.id).then(function(data) {
								data.unshift({id: null, name: "---------"});
								$scope.seriesMap[eventType.id] = data;
								recievedSerieMaps++;
								if($scope.eventTypeList.length == recievedSerieMaps) {
									defer.resolve($scope.seriesMap);
								}
							});
						});
						return defer.promise;
					};
					var tagMap = {};
					/*tagService.search("eventGroup").then(function(returnTags){
						angular.forEach(returnTags, function(tag, index){
							if(angular.isUndefined(tagMap[tag.level])) {
								$scope.tagList.push(tag);
								tagMap[tag.level] = tag.tagGroup;
							}
						})
						//console.log($scope.tagList)
						//console.log(tagMap)
					},function(data){
					alsert(data.data.message);
					});*/
					var roundMap = {};
					roundService.list().then(function(data){
						angular.forEach(data, function(round, index) {
							if(angular.isUndefined(roundMap[round.level])) {
								roundMap[round.level] = round;
							}
						});
						angular.forEach(roundMap, function(round, index2) {
							$scope.roundList.push(round);
						})
						console.log($scope.roundList);
					},function(error){
						
					});
					
					
					getSeries().then(function(returnList){
						//console.log(returnList);
						eventRuleService.listAll().then(function(rules) {
							angular.forEach(rules, function(rule, idx){
								$scope.getEventDefs(rule);
								$scope.eventRules.push(rule);
							})
								console.log($scope.eventRules);
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
			
			$scope.getEventDefs = function(rule){
				console.log(rule.eventTypeId);
				rule.stages = [ {
					stage : null,
					name : "------"
				} ];
				eventTypeService.getEventDefs(
						rule.eventTypeId).then(
								
						function(data) {
							angular.forEach(data, function(
									stage, index) {
								stage.name = stage.name + "(" + stage.shortName + ")";
								rule.stages.push(stage);
							})
							// console.log($scope.stages)
						}, function(data) {

						})
			}
			$scope.createNewRule = function() {
				var newRule = {
					showInput : true
				};
				$scope.eventRules.unshift(newRule);
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
								$scope.getEventDefs(updateRule);
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
								$scope.getEventDefs(updateRule);
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
				if($scope.criteria.mobileNum){
					tmp.mobileNum = $scope.criteria.mobileNum;
				} else {
					tmp.mobileNum = null;
				}
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
			};
			$scope.withdraw = function(creditRecordId, creditRecordIndex) {
				if(confirm("确定要撤销该记录吗？")){
					creditRecordService.withdraw(creditRecordId).then(function(data) {
						var newCreditRecords = [];
						angular.forEach($scope.creditRecords, function(creditRecord,index) {
							if (index == creditRecordIndex) {
								var updatedCreditRecord = angular.copy(data);
								newCreditRecords.push(updatedCreditRecord);
							} else {
								newCreditRecords.push(angular.copy(creditRecord));
							}
						});
						$scope.creditRecords = newCreditRecords;
					});
				}
			};
			$scope.recoverWithdraw = function(creditRecordId, creditRecordIndex) {
				if(confirm("确定要恢复该记录吗？")){
					creditRecordService.recoverWithdraw(creditRecordId).then(function(data) {
						var newCreditRecords = [];
						angular.forEach($scope.creditRecords, function(creditRecord,index) {
							if (index == creditRecordIndex) {
								var updatedCreditRecord = angular.copy(data);
								newCreditRecords.push(updatedCreditRecord);
							} else {
								newCreditRecords.push(angular.copy(creditRecord));
							}
						});
						$scope.creditRecords = newCreditRecords;
					});
				}
			};
			var init = function() {
				$scope.search();
			};
			init();
		} ]);
