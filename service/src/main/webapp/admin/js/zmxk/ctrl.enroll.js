//Enroll Start
zmxk
		.controller(
				'EnrollController',
				[
						'$scope',
						'userService',
						'eventService',
						'$interval',
						'$timeout',
						'$routeParams',
						function($scope, userService, eventService, $interval,
								$timeout, $routeParams) {
							$scope.enroll_form_data = {};
							$scope.enroll_form_data.kids = [];
							$scope.kidsToShow = [];
							$scope.found_user = {};
							$scope.totalSteps = 0;
							$scope.currentStep = 1;
							$scope.showAddChildForm = 0;
							$scope.steps = [];
							$scope.addKid = {};
							$scope.previous_label = "上一步";
							$scope.next_label = "下一步";
							$scope.events = [ {
								eventTime : "2015/07/04",
								currentAttendeeNum : 15,
								eventId : "12345"
							}, {
								eventTime : "2015/07/05",
								currentAttendeeNum : 16,
								eventId : "67890"
							}, {
								eventTime : "2015/07/06",
								currentAttendeeNum : 17,
								eventId : "24680"
							} ];
							// FIXME
							$scope.kids_school_options = [ {
								value : 0,
								label : "幼儿园"
							}, {
								value : 1,
								label : "小学"
							}, {
								value : 2,
								label : "未上幼儿园"
							} ]

							$scope.addChild = function() {
								$scope.addKid["existed"] = false;
								$scope.addKid["selected"] = true;
								$scope.addKid["userId"] = $scope.enroll_form_data.user.id;
								var kid = angular.copy($scope.addKid);
								$scope.kidsToShow.push(kid);
								$scope.showAddChildForm = 0;
							}

							$scope.showChildForm = function() {
								$scope.showAddChildForm += 1;
							}

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

							$scope.selectEvent = function(eventId) {
								$scope.enroll_form_data.id = eventId;
								// console.log($scope.enroll_form_data);
							}

							$scope.processForm = function() {
								var kidsToCreate = [];
								for (var i = 0; i < $scope.kidsToShow.length; i++) {
									var kid = $scope.kidsToShow[i];

									if (kid.selected) {
										$scope.enroll_form_data.kids.push(kid);
									}
								}
								// console.log($scope.enroll_form_data.kids);

								// TODO call Kid API to create Kid
								var returnAttendees = [];
								for (var j = 0; j < $scope.enroll_form_data.kids.length; j++) {
									var kid = $scope.enroll_form_data.kids[j];
									var attendee = {};
									attendee["userId"] = $scope.enroll_form_data.user.id;
									if (!kid.existed) {
										attendee["kid"] = kid;
									} else {
										attendee["kidId"] = kid.id;

									}
									eventService
											.addAttendee(
													$scope.enroll_form_data.id,
													attendee)
											.then(
													function(data) {
														returnAttendees
																.push(data);
														if (returnAttendees.length == $scope.enroll_form_data.kids.length) {

															$scope
																	.launch(
																			"notify",
																			"赛事录入成功",
																			"请记住您的车次:1, 5秒钟后跳转",
																			function() {
																				$scope
																						.hoopPage(
																								"events",
																								[ $scope.enroll_form_data.id ])
																			},
																			function() {
																			});
															/**/
															$timeout(
																	function() {
																		$scope
																				.hoopPage(
																						"events",
																						[ $scope.enroll_form_data.id ])
																	}, 5000)
															// var url =
															// $scope.$location.$$host;
															$scope.enroll_form_data.kids = [];
														}
													},
													function(error) {
														$scope
																.launch(
																		"error",
																		"",
																		error.data.message,
																		function() {

																		},
																		function() {
																		});
														$scope.enroll_form_data.kids = [];
													})

								}

								// console.log($scope.enroll_form_data);

							}

							$scope.searchUser = function() {

								userService
										.search(
												$scope.enroll_form_data.userQueryString)
										.then(
												function(users) {
													
													if(users.length > 1 || users.length == 0) {
														$scope
														.launch(
																"error",
																"",
																"搜索到"+ users.length + "个用户，请到用户页面检查用户",
																function() {

																},
																function() {
																});
													} else {
														console
														.log(" found users ---- ")
														$scope.found_user = users[0];
														console
														.log(users);
													}
												}, function(error) {
													$scope
													.launch(
															"error",
															"",
															error.data.message,
															function() {

															},
															function() {
															});
												});

							}

							$scope.chooseUser = function() {
								$scope.enroll_form_data.user = $scope.found_user;
								$scope.kidsToShow = [];
								for (var i = 0; i < $scope.found_user.kids.length; i++) {
									var kid = angular.copy($scope.found_user.kids[i]);
									kid["existed"] = true;
									$scope.kidsToShow
											.push(kid);
								}
								/*
								 * $scope.enroll_form_data.name =
								 * $scope.found_user.name;
								 * $scope.enroll_form_data.mobileNum =
								 * $scope.found_user.mobileNum;
								 * $scope.enroll_form_data.id =
								 * $scope.found_user.id;
								 * $scope.enroll_form_data.wcId =
								 * $scope.found_user.wcId;
								 */
							}

							$scope.init = function() {
								// TODO get from user api
								$scope.userId = $routeParams.userId;

								if (!angular.isUndefined($scope.userId)) {
									// "dd6ebe09-d00f-4bb5-b7b0-22d8ae607afc" -
									// Mac Fang
									userService
											.getUser($scope.userId)
											.then(
													function(user) {

														if (!angular
																.isUndefined(user.kids)) {

															for (var i = 0; i < user.kids.length; i++) {
																var kid = user.kids[i];
																kid["existed"] = true;
																$scope.kidsToShow
																		.push(kid);
															}
														}
														console
																.log($scope.userId);
														$scope.enroll_form_data.user = user;
														console
																.log($scope.enroll_form_data.user);
														/*
														 * $scope.enroll_form_data.id =
														 * user.id;
														 * $scope.enroll_form_data.name =
														 * user.name
														 * $scope.enroll_form_data.mobileNum =
														 * user.mobileNum;
														 * $scope.enroll_form_data.wcId =
														 * user.wcId;
														 */
													}, function(error) {
														// TODO
													});
								}
								if (!$scope.noUserIdPassed()) {
									$scope.totalSteps = 2;
									$scope.steps = [ 1, 2 ];
								} else {
									$scope.totalSteps = 3;
									$scope.steps = [ 1, 2, 3 ];
								}

								// TODO : call eventServie to list events
								eventService.list().then(function(data) {
									$scope.events = data;

									// console.log($scope.events);

								}, function(error) {
									// TODO
								});

							}

							$scope.noUserIdPassed = function() {
								return angular.isUndefined($scope.userId);
							}

							$scope.noUserIdPassedValue = $scope
									.noUserIdPassed();

							$scope.formatDate = function(timestamp) {
								var dateOut = new Date(timestamp);
								return dateOut;
							};

							$scope.init();
						} ]);

/*
 * Enroll Start zmxk.controller('EnrollWithUserController', [ '$scope',
 * 'userService', 'eventService', '$interval', '$timeout', '$routeParams',
 * function($scope, userService, eventService, $interval, $timeout,
 * $routeParams) { } ]);
 */