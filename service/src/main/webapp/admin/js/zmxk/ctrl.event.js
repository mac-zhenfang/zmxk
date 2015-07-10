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
				} else {
					eventService.getEvent($scope.eventId).then(function(data) {
						$scope.eventInit = data;
						console.log($scope.eventInit);
					}, function(data) {
						console.log(data);
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

			$scope.init = function() {
				if (!angular.isUndefined($scope.eventId)) {
					eventService.getEvent($scope.eventId).then(function(data) {
						$scope.eventInit = data;
						console.log($scope.eventInit);
					}, function(data) {
						console.log(data);
					});
				}
			}

			$scope.init();
		} ])