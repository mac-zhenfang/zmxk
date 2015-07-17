zmxk.controller('EventManageCtrl', [
		'$scope',
		'userService',
		'eventService',
		'$interval',
		'$timeout',
		'$routeParams',
		function($scope, userService, eventService, $interval, $timeout,
				$routeParams) {

			$scope.viewEventManagePage = function() {
				if (angular.isUndefined($scope.eventId)) {
					return "event_manage_show_list.html";
				} else {
					return "event_manage_show_detail.html";
				}
			}

			$scope.callToEvent = function(eventId) {
				$scope.hoopPage("events.html", {
					"eventId" : eventId
				})
			}

			var init = function() {
				if (angular.isUndefined($scope.eventId)) {
					eventService.list().then(
							function(data) {
								$scope.listEvents = data;
								angular.forEach($scope.listEvents, function(
										event, index) {

									var enrolledCount = 0;
									var applyScoreCount = 0;
									var leftCount = 0;
									angular.forEach(event.attendees, function(
											attendee, index2) {
										var attStatus = attendee.status;
										// console.log(attStatus);
										if (attStatus == 1) {
											enrolledCount += 1;
										} else if (attStatus == 2) {
											applyScoreCount += 1;
										}

									});
									event.leftCount = event.quota
											- enrolledCount - applyScoreCount;
									event.applyScoreCount = applyScoreCount;
									event.enrolledCount = enrolledCount;
								});
							}, function(data) {
							});
				}
			}

			init();
		} ]);
zmxk.controller('EventCreateCtrl', [
		'$scope',
		'userService',
		'eventService',
		'$interval',
		'$timeout',
		'$routeParams',
		'$dialogs',
		function($scope, userService, eventService, $interval, $timeout,
				$routeParams, $dialogs) {
			$scope.totalSteps = 3;
			$scope.selectStep = function(step) {

				if ($scope.currentStep > 0
						&& $scope.currentStep + step <= $scope.totalSteps) {
					$scope.currentStep = $scope.currentStep + step;
				}
				// change label
				if ($scope.currentStep == $scope.totalSteps) {
					$scope.next_label = "提交";
				} else {
					$scope.next_label = "下一步";
				}
			}
		} ]);
zmxk
		.controller(
				'EventDetailCtrl',
				[
						'$scope',
						'userService',
						'eventService',
						'$interval',
						'$timeout',
						'$routeParams',
						'$dialogs',
						'tagService',
						function($scope, userService, eventService, $interval,
								$timeout, $routeParams, $dialogs, tagService) {
							$scope.eventId = $routeParams.eventId;
							// console.log("~~~~~" + $scope.eventId);
							$scope.eventInit = {};
							$scope.selectedRule
							$scope.applyCreditRuleAttendeeList = [];
							$scope.eventInit.attendees = [];
							$scope.eventTags = [];
							var tagTypeClasss = ["btn btn-info", "btn btn-success", "btn btn-danger", "btn btn-active"]
							$scope.init = function() {

								if (!angular.isUndefined($scope.eventId)) {
									eventService
											.getEvent($scope.eventId)
											.then(
													function(data) {
														$scope.eventInit = data;

														var enrolledCount = 0;
														var applyScoreCount = 0;
														var leftCount = 0;
														var tagsMap = {};
														var newAttendees = [];
														angular
																.forEach(
																		$scope.eventInit.attendees,
																		function(
																				attendee,
																				index2) {
																			if(angular.isUndefined(attendee.tagId) || !attendee.tagId) {
																				if(angular.isUndefined(tagsMap["fake"])){
																					tagsMap["fake"] = [];
																				}
																				tagsMap["fake"].push(attendee);
																			} else {
																				if(angular.isUndefined(tagsMap[attendee.tagId])){
																					tagsMap[attendee.tagId] = [];
																				}
																				tagsMap[attendee.tagId].push(attendee);
																			}
																		});
														var d = 0;
														angular
														.forEach(tagsMap, function(attendeeList, index){
															d++;
															angular
															.forEach(attendeeList, function(attendee, index2){
																attendee["tagType"] = d;
																newAttendees.push(attendee);
															});
														});
														
														$scope.eventInit.attendees = angular.copy(newAttendees);
														
														console.log($scope.eventInit.attendees);
														
														$scope.eventInit.leftCount = event.quota
																- enrolledCount
																- applyScoreCount;
														$scope.eventInit.applyScoreCount = applyScoreCount;
														$scope.eventInit.enrolledCount = enrolledCount;

													}, function(data) {
														console.log(data);
													});
								}
								
								// get tags
								tagService.search("event").then(function(data){
									$scope.eventTags = data;
								},function(data){
									
								});
							}
							
							$scope.showTagClass = function(tagType) {
								return tagTypeClasss[tagType % tagTypeClasss.length];
							}
							$scope.canGiveCredit = function(attendee) {
								return attendee.existed == false || attendee.status<2;
							}
							$scope.giveCredit = function(attendee) {
								var selectedAttendee = attendee;
								/*angular.forEach($scope.eventInit.attendees,
										function(attendee, index) {
											if (attendee.id == attendeeId) {
												selectedAttendee = attendee;
												return;
											}
										});*/
								// console.log(selectedAttendee);
								if (selectedAttendee.status == 2) {
									dlg = $dialogs
											.create(
													'/admin/give_event_credit.html',
													'GiveEventCreditCtrl',
													{
														eventTypeId : $scope.eventInit.eventTypeId,
														serieId : $scope.eventInit.serieId,
														attendee : selectedAttendee
													}, 'lg');
									dlg.result.then(function(selectedRule) {
										$scope.selectedRule = selectedRule;
										$scope.applyCreditRuleAttendeeList.push({
											rule : selectedRule,
											attendee : selectedAttendee
										});
										console.log(selectedRule)
									}, function() {
										// $scope.name = 'You decided not to
										// enter in your name, that makes me
										// sad.';
									});
								} else {
									$scope.launch("error", "",
											"选手没有完成比赛，无法授予积分", function() {

											}, function() {
											});
								}
							}
							$scope.editScore = function() {
								$scope.eventInit.attendees.sort(function(a, b) {
									var key = "score";
									var x = a[key];
									var y = b[key];
									return ((x < y) ? -1 : ((x > y) ? 1 : 0));
								});
								// console.log($scope.eventInit.attendees);
								var i = 0;
								angular.forEach($scope.eventInit.attendees,
										function(attendee, index) {
											if (attendee.score != 0) {
												i += 1;
												attendee.rank = i;
											}
										})
								console.log($scope.eventInit.attendees);
							}

							$scope.applyAttendeeChanges = function() {
								var error_msg = "";
								var error = false;
								console.log($scope.applyCreditRuleAttendeeList);
								/*angular
										.forEach(
												$scope.eventInit.attendees,
												function(attendee, index) {
													if ((attendee.rank == 0 && attendee.score >= 0)
															|| (attendee.score == 0 && attendee.rank >= 0)) {
														error_msg = "请核对选手的成绩与排名";
														error = true;
														return;
													}
												})*/
								if (error) {
									$scope.launch("error", "", error_msg,
											function() {

											}, function() {
											});
								} else {
									// going to save data
									$scope
											.launch(
													"confirm",
													"确定保存",
													"确保您已经检查选手成绩，点击确定保存",
													function() {
														eventService
																.saveAttendee(
																		$scope.eventInit.id,
																		$scope.eventInit.attendees)
																.then(
																		function(
																				data) {
																			$scope.editingTag = false;
																			$scope
																					.launch(
																							"notify",
																							"成绩修改成功",
																							"成绩修改成功",
																							function() {

																							},
																							function() {
																							});
																		},
																		function(
																				error) {

																		})
													}, function() {
													});
								}
								console.log($scope.eventInit.attendees);
							}

							
						} ]);

zmxk.controller('GiveEventCreditCtrl', function($scope, $modalInstance, data) {

	// -- Variables --//
	$scope.data = data;
	$scope.selectRule = {};
	// FAKE DATA
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

	$scope.init = function() {
		var newRules = [];
		angular.forEach($scope.eventRules, function(rule, index) {
			if (angular.isUndefined(rule.rank)) {
				newRules.push(angular.copy(rule));
			}
		});
		$scope.eventRules = newRules;
		console.log($scope.eventRules);
	}

	// -- Methods --//

	$scope.cancel = function() {
		$modalInstance.dismiss('Canceled');
	}; // end cancel

	$scope.save = function(selectRule) {
		// console.log(selectRule);
		$modalInstance.close(selectRule);
	}; // end save

	/*
	 * $scope.hitEnter = function(evt) { if (angular.equals(evt.keyCode, 13) &&
	 * !(angular.equals($scope.user.name, null) || angular.equals(
	 * $scope.user.name, ''))) $scope.save(); };
	 */

})