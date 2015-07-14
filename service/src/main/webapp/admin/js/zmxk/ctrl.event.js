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
			
			$scope.formatDate = function(time) {
				  var date = new Date(time);
				  var hours = date.getHours();
				  var minutes = date.getMinutes();
				  var ampm = hours >= 12 ? 'pm' : 'am';
				  hours = hours % 12;
				  hours = hours ? hours : 12; // the hour '0' should be '12'
				  minutes = minutes < 10 ? '0'+minutes : minutes;
				  var strTime = hours + ':' + minutes + ' ' + ampm;
				  return date.getMonth()+1 + "/" + date.getDate() + "/" + date.getFullYear() + "  " + strTime;
				}
			
			var init = function() {
				if (angular.isUndefined($scope.eventId)) {
					eventService.list().then(function(data) {
						$scope.listEvents = data;
						angular.forEach($scope.listEvents, function(event, index) {
							
							var enrolledCount = 0;
							var applyScoreCount = 0;
							var leftCount = 0;
							angular.forEach(event.attendees, function(attendee, index2){
								var attStatus = attendee.status;
								//console.log(attStatus);
								if(attStatus == 1) {
									enrolledCount+=1;
								} else if (attStatus == 2) {
									applyScoreCount+=1;
								}
								
							});
							event.leftCount = event.quota - enrolledCount - applyScoreCount;
							event.applyScoreCount= applyScoreCount;
							event.enrolledCount = enrolledCount;
						});
						 console.log("~~~~");
						 console.log($scope.listEvents);
					}, function(data) {
					});
				}
			}

			init();
		} ]);
zmxk
.controller(
		'EventCreateCtrl',
		[
				'$scope',
				'userService',
				'eventService',
				'$interval',
				'$timeout',
				'$routeParams',
				function($scope, userService, eventService, $interval,
						$timeout, $routeParams) {
					$scope.totalSteps  = 3;
					$scope.selectStep = function(step) {

						if ($scope.currentStep > 0
								&& $scope.currentStep + step <= $scope.totalSteps) {
							$scope.currentStep = $scope.currentStep
									+ step;
						}
						// change label
						if ($scope.currentStep == $scope.totalSteps) {
							$scope.next_label = "提交";
						} else {
							$scope.next_label = "下一步";
						}
					}
				}]);
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
						function($scope, userService, eventService, $interval,
								$timeout, $routeParams) {
							$scope.eventId = $routeParams.eventId;
							console.log("~~~~~" + $scope.eventId);
							$scope.eventInit = {};
							$scope.eventInit.attendees = [];
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
														angular.forEach($scope.eventInit.attendees, function(attendee, index2){
															var attStatus = attendee.status;
															//console.log(attStatus);
															if(attStatus == 1) {
																enrolledCount+=1;
															} else if (attStatus == 2) {
																applyScoreCount+=1;
															}
															
														});
														$scope.eventInit.leftCount = event.quota - enrolledCount - applyScoreCount;
														$scope.eventInit.applyScoreCount= applyScoreCount;
														$scope.eventInit.enrolledCount = enrolledCount;
														
													}, function(data) {
														console.log(data);
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
								angular
										.forEach(
												$scope.eventInit.attendees,
												function(attendee, index) {
													if ((attendee.rank == 0 && attendee.score >= 0)
															|| (attendee.score == 0 && attendee.rank >= 0)) {
														error_msg = "请核对选手的成绩与排名";
														error = true;
														return;
													}
												})
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

							$scope.init();
						} ]);