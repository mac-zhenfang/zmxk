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

zmxk.controller('EventDetailCtrl', [
		'$scope',
		'userService',
		'eventService',
		'$interval',
		'$timeout',
		'$routeParams',
		function($scope, userService, eventService, $interval, $timeout,
				$routeParams) {
			$scope.eventId = $routeParams.eventId;
			console.log("~~~~~" + $scope.eventId);
			$scope.eventInit = {};
			$scope.eventInit.attendees = [];
			$scope.init = function() {
				//FIXME, we should not allow edit the event 3 days after the eventtime or less than event time. 
				$scope.gridOptions = {
						data : 'eventInit.attendees',
						enableCellSelection : true,
						enableCellEdit : true,
						enableRowSelection : false,
						columnDefs : [ {
							field : 'kidName',
							displayName : '选手名',
							enableCellEdit : false
						}, {
							field : 'score',
							displayName : '分数',
							enableCellEdit : true
						}, {
							field : 'status',
							displayName : '状态',
							enableCellEdit : false,
							cellTemplate : '<div class="ngCellText" ng-class="col.colIndex()"><span ng-cell-text>{{status}}</span></div>'
						},  {
							field : 'rank',
							displayName : '排名',
							enableCellEdit : true
						}, {
							field : 'credit',
							displayName : '积分',
							enableCellEdit : true
						} ]
					};

				if (!angular.isUndefined($scope.eventId)) {
					eventService.getEvent($scope.eventId).then(function(data) {
						$scope.eventInit = data;
						for (var i =0; i<$scope.eventInit.attendees.length;i++) {
							// init credit 
							$scope.eventInit.attendees[i]["credit"] = 0;
							
						}
						console.log($scope.eventInit);
					}, function(data) {
						console.log(data);
					});
				}
			}
			
			$scope.applyAttendeeChanges = function() {
				console.log($scope.eventInit.attendees);
			}

			$scope.init();
		} ]);