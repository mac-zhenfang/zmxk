var currentModule;
var zmxk = angular.module('zmxk', [ 'ngResource', 'ui.bootstrap', 'dialogs',
		'ngRoute' ], function($httpProvider) {
	$httpProvider.interceptors.push(function($q) {
		return {
			'responseError' : function(response) {
				if (response.status == "401") {
					window.location = "login.html";
					return;
				}

				if (response.data.error.message[0]) {
					alert(response.data.error.message[0]);
				}
				return $q.reject(response);
			}
		};
	});
});

// put all labels / constants in
zmxk.constant('zmxkConstant', {

});

zmxk
		.value(
				'zmxkConfig',
				{
					user_login_uri : "/service/api/v1/users/login",
					user_logout_uri : "/service/api/v1/users/logout",
					user_rest_uri : "/service/smartool/api/v1/users/:userId",
					event_rest_uri : "/service/smartool/api/v1/events/:eventId",
					event_add_attendee_uri : "/service/smartool/api/v1/events/:eventId/attendees",
					kid_rest_uri : "/service/smartool/api/v1/users/:userId/kids/:kidId"
				});

zmxk.config([ '$resourceProvider', function($resourceProvider) {
	// Don't strip trailing slashes from calculated URLs
	$resourceProvider.defaults.stripTrailingSlashes = false;
} ]);

zmxk.config([ '$routeProvider', function($routeProvider) {
	$routeProvider.when('/users', {
		templateUrl : 'user_list.html',
		controller : "EventDetailCtrl"
	}).when('/events/:eventId', {
		templateUrl : 'event_detail.html',
		controller : "EventDetailCtrl"
	}).when('/events', {
		templateUrl : 'event_list.html',
		controller : "EventManageCtrl"
	}).when('/enroll', {
		templateUrl : 'event_enroll.html',
		controller : "EnrollController"
	}).when('/enroll/:userId', {
		templateUrl : 'event_enroll.html',
		controller : "EnrollController"
	}).otherwise({
		redirectTo : 'user_list.html'
	});
} ]);