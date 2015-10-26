//Enroll Start
zmxk
		.controller(
				'EnrollController',
				[
						'$scope',
						'userService',
						'eventService',
						'teamService',
						'$interval',
						'$timeout',
						'$routeParams',
						'kidService',
						'$cookies',
						function($scope, userService, eventService,
								teamService, $interval, $timeout, $routeParams,
								kidService, $cookies) {
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
							$scope.events = [];
							$scope.errorProcess = false;
							
							$scope.selectedDropdownItem = null;
							$scope.dropdownItems =  [];
							$scope.chooseSchoolName = function(addKid) {
								
								if(null == addKid.schoolName || addKid.schoolName.length == 0) {
									return;
								}
								
								$timeout(function(){
									var itemMatches = false;
									var matchedText = null;
									for(var i=0;i< $scope.dropdownItems.length;i++) {
										item = $scope.dropdownItems[i];
										if (item.toString().toLowerCase().indexOf(addKid.schoolName) !== -1) {
									            itemMatches = true;
									            matchedText = item;
									            break;
									    }
									}
									if(itemMatches) {
										addKid.schoolName = matchedText;
									}
								}, 1000);
								
							}
							$scope.getDistinctSchoolName = function(schoolType) {
								if(schoolType < 2) {
									$scope.addKid.schoolName = $cookies.get("school_"+schoolType);
								}
								kidService.listSchools($scope.enroll_form_data.user.id, schoolType).then(function(data) {
									$scope.dropdownItems = data;
								}, function(error){
									alert(error.data.message);
								});
							}
							$scope.addChild = function() {
								if (!angular.isUndefined($scope.addKid.name)) {
									$scope.addKid["existed"] = false;
									$scope.addKid["selected"] = true;
									$scope.addKid["userId"] = $scope.enroll_form_data.user.id;
									if($scope.addKid.schoolType < 2) {
										var now = new Date(),
									    // this will set the expiration to 6 months
									    exp = new Date(now.getFullYear(), now.getMonth(), now.getDate()+1);
										$cookies.put("school_"+$scope.addKid.schoolType, $scope.addKid.schoolName,{
											  expires: exp
										});
									}

									kidService.createKid(
											$scope.enroll_form_data.user.id,
											$scope.addKid).then(function(data) {
										var kid = angular.copy($scope.addKid);
										$scope.kidsToShow.push(kid);
										$scope.showAddChildForm = 0;
										$scope.addKid = {};
									}, function(data) {
										alert("创建失败");
									});

								}
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

							$scope.selectEvent = function(event) {
								// console.log(event);

								var kids = [];
								for (var i = 0; i < $scope.kidsToShow.length; i++) {
									var kid = $scope.kidsToShow[i];

									if (kid.selected) {
										kids.push(kid);
									}
								}
								console.log(kids);
								angular
										.forEach(
												kids,
												function(kid) {
													if (event.team
															&& (angular
																	.isUndefined(kid["teamId"]) || kid["teamId"] == null)) {
														$scope.errorProcess = true;
														$scope.launch("error",
																"",
																"选手必须已经加入战队",
																function() {

																}, function() {
																});
														return;
													}
												});
								$scope.enroll_form_data.id = event.id;
								// console.log($scope.enroll_form_data);
							}

							$scope.uploadKidAvatar = function(kid) {
								// console.log("```` return avatar ````");

								// console.log(kid.avatar);
								// console.log(data);
								if (!angular.isUndefined(kid.avatar)
										&& kid.avatar != null) {
									kidService.uploadAvatar(kid.userId, kid.id,
											kid.avatar).then(function(data) {
										alert("上传成功");
										$scope.imageCropStep = 1;
										delete kid.avatar;
										delete kid.avatarBefore;
										console.log(data);
									}, function(error) {
										alert("上传失败");
										$scope.imageCropStep = 1;
										delete kid.avatar;
										delete kid.avatarBefore;
										console.log(error);
									});
								}
							}

							$scope.processForm = function() {
								if ($scope.errorProcess) {
									$scope.launch("出现错误", "", "请仔细核对步骤中的错误信息",
											function() {

											}, function() {
											});
									return;
								}
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

										if (!angular.isUndefined(kid.avatar)
												&& kid.avatar != null) {
											attendee["kidAvatar"] = kid.avatar;
										}

									}
									

									var enrollAttendee = {};
									angular.copy(attendee, enrollAttendee);
									if (!angular
											.isUndefined(enrollAttendee["kid"])
											&& !angular
													.isUndefined(enrollAttendee["kid"]["avatar"])) {
										delete enrollAttendee["kid"]["avatar"];
									} else if (!angular
											.isUndefined(enrollAttendee["kidAvatar"])) {
										delete enrollAttendee["kidAvatar"];
									}

									var msg = "";
									eventService
											.addAttendee(
													$scope.enroll_form_data.id,
													enrollAttendee)
											.then(
													function(data) {

														returnAttendees
																.push(data);
														msg += "姓名: "
														msg += data.kidName;
														msg += " 录入成功。";
														// msg += data.seq
														// msg += " "
														if (returnAttendees.length == $scope.enroll_form_data.kids.length) {

															$scope
																	.launch(
																			"notify",
																			"赛事录入成功",
																			msg,
																			function() {
																				$scope
																						.hoopPage(
																								"events",
																								[ $scope.enroll_form_data.id ])
																			},
																			function() {
																			});
															/*
															 * $timeout(
															 * function() {
															 * $scope .hoopPage(
															 * "events", [
															 * $scope.enroll_form_data.id ]) },
															 * 5000)
															 */
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

													if (users.length > 1
															|| users.length == 0) {
														$scope
																.launch(
																		"error",
																		"",
																		"搜索到"
																				+ users.length
																				+ "个用户，请到用户页面检查用户",
																		function() {

																		},
																		function() {
																		});
													} else {
														console
																.log(" found users ---- ")
														$scope.found_user = users[0];
														console.log(users);
													}
												},
												function(error) {
													$scope.launch("error", "",
															error.data.message,
															function() {

															}, function() {
															});
												});

							}

							$scope.chooseUser = function() {
								$scope.enroll_form_data.user = $scope.found_user;
								$scope.kidsToShow = [];
								for (var i = 0; i < $scope.found_user.kids.length; i++) {
									var kid = angular
											.copy($scope.found_user.kids[i]);
									kid["existed"] = true;
									if (!angular.isUndefined(kid["teamId"])
											&& kid["teamId"] != null) {
										teamService
												.get(kid["teamId"])
												.then(
														function(data) {
															kid["teamName"] = data.name;
															$scope.kidsToShow
																	.push(kid);
														},
														function(error) {
															alert(error.data.message);
														})
									} else {
										$scope.kidsToShow.push(kid);
									}

								}
							}

							$scope.init = function() {
								// TODO get from user api
								var loginUserSiteId = null;
								if (!angular.isUndefined($scope.loginUser)) {
									loginUserSiteId = $scope.loginUser.siteId;
								}

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
								
								// FIXME, we dont need to have status

								eventService
										.list(loginUserSiteId)
										.then(
												function(data) {
													var events = [];
													events = data;
													angular
															.forEach(
																	events,
																	function(
																			event,
																			index) {

																		var enrolledCount = 0;
																		angular
																				.forEach(
																						event.attendees,
																						function(
																								attendee,
																								index2) {
																							var attStatus = attendee.status;
																							// console.log(attStatus);
																							if (attStatus >= 1) {
																								enrolledCount += 1;
																							}
																						});
																		// console.log(enrolledCount);
																		var leftCount = event.quota
																				- enrolledCount;
																		// console.log(leftCount);
																		event.leftCount = leftCount;
																		event.enrolledCount = enrolledCount;
																		if (event.status == 0) {
																			$scope.events
																					.push(event);
																		}
																	});
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