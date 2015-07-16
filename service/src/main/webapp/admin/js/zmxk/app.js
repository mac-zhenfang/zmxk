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
				/*
				 * if (response.status == "400") {
				 * 
				 * alert(response.data.error); } if (response.data.error) {
				 * alert(response.data.error); }
				 */
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
					user_login_uri : "/service/smartool/api/v1/users/login",
					user_logout_uri : "/service/smartool/api/v1/users/logout",
					user_search_uri : "/service/smartool/api/v1/users/search?query=:query",
					user_rest_uri : "/service/smartool/api/v1/users/:userId",
					event_rest_uri : "/service/smartool/api/v1/events/:eventId",
					event_add_attendee_uri : "/service/smartool/api/v1/events/:eventId/enroll",
					event_update_attendee_uri : "/service/smartool/api/v1/events/:eventId/complete",
					rule_create_uri : "/service/smartool/api/v1/creditrules",
					rule_update_uri : "/service/smartool/api/v1/creditrules/:creditRuleId",
					rule_get_uri : "/service/smartool/api/v1/creditrules/:creditRuleId",
					rule_search_uri : "/service/smartool/api/v1/creditrules",
					rule_remove_uri : "/service/smartool/api/v1/creditrules/:creditRuleId",
					event_rule_create_uri : "/service/smartool/api/v1/eventcreditrules",
					event_rule_update_uri : "/service/smartool/api/v1/eventcreditrules/:eventCreditRuleId",
					event_rule_get_uri : "/service/smartool/api/v1/eventcreditrules/:eventCreditRuleId",
					event_rule_list_all_uri : "/service/smartool/api/v1/eventcreditrules",
					event_rule_search_ranking_uri : "/service/smartool/api/v1/eventcreditrules/ranking/search",
					event_rule_search_nonranking_uri : "/service/smartool/api/v1/eventcreditrules/nonranking/search",
					event_rule_remove_uri : "/service/smartool/api/v1/eventcreditrules/:eventCreditRuleId",
					kid_rest_uri : "/service/smartool/api/v1/users/:userId/kids/:kidId",
					tag_search_uri : "/service/smartool/api/v1/tags/search?type=:type",
					tag_rest_uri : "/service/smartool/api/v1/tags/:tagId"

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
	}).when('/event_rules', {
		templateUrl : 'event_rule_list.html',
		controller : "EventManageCtrl"
	}).when('/general_rules', {
		templateUrl : 'general_rule_list.html',
		controller : "EventManageCtrl"
	}).when('/event_create', {
		templateUrl : 'event_create.html',
		controller : "EventCreateCtrl"
	}).when('/enroll', {
		templateUrl : 'event_enroll.html',
		controller : "EnrollController"
	}).when('/enroll/:userId', {
		templateUrl : 'event_enroll.html',
		controller : "EnrollController"
	}).otherwise({
		redirectTo : '/users'
	}).when('/credit_records', {
		templateUrl : 'credit_records.html',
		controller : "CreditRecordCtrl"
	});
} ]);