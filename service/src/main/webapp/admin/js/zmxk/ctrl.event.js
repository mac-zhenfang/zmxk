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
			$scope.init = function() {
				if (angular.isUndefined($scope.eventId)) {
					eventService.list().then(function(data) {
						$scope.listEvents = data;
						// console.log("~~~~");
						// console.log($scope.listEvents);
					}, function(data) {
					});
				}
			}

			$scope.init();
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
						function($scope, userService, eventService, $interval,
								$timeout, $routeParams) {
							$scope.eventId = $routeParams.eventId;
							console.log("~~~~~" + $scope.eventId);
							$scope.eventInit = {};
							$scope.eventInit.attendees = [];
							$scope.init = function() {
								// FIXME, we should not allow edit the event 3
								// days after the eventtime or less than event
								// time.
								/*
								 * { field : 'status', displayName : '状态',
								 * enableCellEdit : false, cellTemplate : '<div
								 * class="ngCellText" ng-class="col.colIndex()"><span
								 * ng-class="{true:\'label label-success\',
								 * false:\'label label-warning\'}[score >
								 * 0]">{{{true:\'已完成比赛\', false:\'未完成比赛\'}[score >
								 * 0]}}</span></div>' },
								 */
								$scope.gridOptions = {
									data : 'eventInit.attendees',
									enableCellSelection : true,
									enableCellEdit : true,
									enableRowSelection : false,
									sortInfo : {
										fields : [ "score" ],
										directions : [ "asc", "desc" ]
									},
									columnDefs : [
											{
												field : 'kidName',
												displayName : '选手名',
												enableCellEdit : false
											},
											{
												field : 'status',
												displayName : '状态',
												enableCellEdit : false,
												cellTemplate : '<div  class="ngCellText" ng-class="col.colIndex()"><span ng-class="{true:\'label label-success\', false:\'label label-warning\'}[row.getProperty(\'score\') > 0]">{{{true:\'已完成比赛\', false:\'未完成比赛\'}[row.getProperty(\'score\') > 0]}}</span></div>'
											},
											{
												field : 'score',
												displayName : '分数',
												enableCellEdit : true,
												editableCellTemplate : '<input ng-class="\'colt\' + col.index" ng-input="COL_FIELD" ng-model="COL_FIELD" ng-change="editScore()"/>'
											}, {
												field : 'rank',
												displayName : '排名',
												enableCellEdit : false
											} ]
								};

								if (!angular.isUndefined($scope.eventId)) {
									eventService
											.getEvent($scope.eventId)
											.then(
													function(data) {
														$scope.eventInit = data;
														for (var i = 0; i < $scope.eventInit.attendees.length; i++) {
															// init credit
															$scope.eventInit.attendees[i]["credit"] = 0;

														}
														console
																.log($scope.eventInit);
													}, function(data) {
														console.log(data);
													});
								}

								$scope.$on('ngGridEventEndCellEdit', function(
										data) {
									/*
									 * $scope.eventInit.attendees.sort(function(a,
									 * b){ var key = "score"; var x = a[key];
									 * var y = b[key]; return ((x < y) ? -1 :
									 * ((x > y) ? 1 : 0)); });
									 */

								});
							}

							$scope.editScore = function() {
								$scope.eventInit.attendees.sort(function(a, b) {
									var key = "score";
									var x = a[key];
									var y = b[key];
									return ((x < y) ? -1 : ((x > y) ? 1 : 0));
								});
								//console.log($scope.eventInit.attendees);
								var i = 0;
								angular.forEach($scope.eventInit.attendees, function(attendee, index) {
									if(attendee.score != 0 ) {
										i+=1;
										attendee.rank = i;
									}
								})
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

								}
								console.log($scope.eventInit.attendees);
							}

							$scope.init();
						} ]);