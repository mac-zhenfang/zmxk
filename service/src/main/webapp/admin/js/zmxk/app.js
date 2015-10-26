var currentModule;
var zmxk = angular.module('zmxk', [ 'ngResource', 'datePicker', 'ui.bootstrap',
		'dialogs', 'ngRoute', 'ngCookies','ImageCropper','angular.filter'], function(
		$httpProvider) {
	$httpProvider.interceptors.push(function($q) {
		return {
			'request' : function(config) {
				// do something on success
				// $("#loadingModal").modal().show();
				return config;
			},
			'response' : function(response) {
				// do something on success
				// $("#loadingModal").modal().hide();
				return response;
			},
			'responseError' : function(response) {
				if (response.status == "401") {
					window.location = "login.html";
					return;
				}
				if (response.status == "403") {
					window.location = "err403.html";
					return;
				}
				return $q.reject(response);
			}
		};
	});
});

// put all labels / constants in
zmxk.constant('zmxkConstant', {
	kids_school_options : [ {
		value : 0,
		label : "幼儿园"
	}, {
		value : 1,
		label : "小学"
	}, {
		value : 2,
		label : "未上幼儿园"
	} ]
});

zmxk
		.value(
				'zmxkConfig',
				{
					user_login_uri : "/service/smartool/api/v1/users/login",
					user_logout_uri : "/service/smartool/api/v1/users/logout",
					user_search_uri : "/service/smartool/api/v1/users/search?query=:query",
					user_query_uri : "/service/smartool/api/v1/users/query",
					user_rest_uri : "/service/smartool/api/v1/users/:userId",
					event_site_uri : "/service/smartool/api/v1/:siteId/events/",
					event_rest_uri : "/service/smartool/api/v1/events/:eventId",
					event_search_uri : "/service/smartool/api/v1/events/search",
					site_rest_uri : "/service/smartool/api/v1/sites/:siteId",
					serie_rest_uri : "/service/smartool/api/v1/eventtypes/:eventTypeId/series/:serieId",
					event_defs_rest_uri : "/service/smartool/api/v1/eventtypes/:eventTypeId/eventdefs",
					event_type_rest_uri : "/service/smartool/api/v1/eventtypes/:eventTypeId",
					event_type_site_rest_uri : "/service/smartool/api/v1/:siteId/eventtypes/",
					event_add_attendee_uri : "/service/smartool/api/v1/events/:eventId/enroll",
					event_promote_attendee_uri : "/service/smartool/api/v1/events/:eventId/promote",
					event_update_attendee_uri : "/service/smartool/api/v1/events/:eventId/complete",
					rule_create_uri : "/service/smartool/api/v1/creditrules",
					rule_update_uri : "/service/smartool/api/v1/creditrules/:creditRuleId",
					rule_get_uri : "/service/smartool/api/v1/creditrules/:creditRuleId",
					rule_search_uri : "/service/smartool/api/v1/creditrules",
					rule_remove_uri : "/service/smartool/api/v1/creditrules/:creditRuleId",
					rule_apply_uri : "/service/smartool/api/v1/creditrules/:creditRuleId/attendee/:attendeeId",
					rule_apply_to_user_uri : "/service/smartool/api/v1/creditrules/:creditRuleId/users/:userId",
					event_rule_create_uri : "/service/smartool/api/v1/eventcreditrules",
					event_rule_update_uri : "/service/smartool/api/v1/eventcreditrules/:eventCreditRuleId",
					event_rule_get_uri : "/service/smartool/api/v1/eventcreditrules/:eventCreditRuleId",
					event_rule_list_all_uri : "/service/smartool/api/v1/eventcreditrules",
					event_rule_search_ranking_uri : "/service/smartool/api/v1/eventcreditrules/ranking/search",
					event_rule_search_nonranking_uri : "/service/smartool/api/v1/eventcreditrules/nonranking/search",
					event_rule_remove_uri : "/service/smartool/api/v1/eventcreditrules/:eventCreditRuleId",
					event_rule_apply_uri : "/service/smartool/api/v1/eventcreditrules/:eventCreditRuleId/attendee/:attendeeId",
					kid_rest_uri : "/service/smartool/api/v1/users/:userId/kids/:kidId",
					kid_schools_uri : "/service/smartool/api/v1/users/:userId/kids/:schoolType/schools",
					kid_join_team_uri : "/service/smartool/api/v1/kids/:kidId/joinTeam/:teamId",
					kid_leave_team_uri : "/service/smartool/api/v1/kids/:kidId/leaveTeam/:teamId",
					kid_avatar_uri : "/service/smartool/api/v1/users/:userId/kids/:kidId/avatar",
					tag_search_uri : "/service/smartool/api/v1/tags/search?type=:type",
					event_credit_records_search_uri : "/service/smartool/api/v1/eventcreditrecords/search",
					withdraw_credit_records_uri : "/service/smartool/api/v1/eventcreditrecords/:creditRecordId/withdraw",
					recover_withdraw_credit_records_uri : "/service/smartool/api/v1/eventcreditrecords/:creditRecordId/recoverwithdraw",
					my_event_credit_records_search_uri : "/service/smartool/api/v1/users/me/eventcreditrecords/search",
					tag_rest_uri : "/service/smartool/api/v1/tags/:tagId",
					team_create_uri : "/service/smartool/api/v1/teams",
					team_update_uri : "/service/smartool/api/v1/teams/:teamId",
					team_remove_uri : "/service/smartool/api/v1/teams/:teamId",
					team_list_uri : "/service/smartool/api/v1/teams",
					team_get_uri : "/service/smartool/api/v1/teams/:teamId",
					team_list_members_uri : "/service/smartool/api/v1/teams/:teamId/members",
					event_serie_def_rest_uri : "/service/smartool/api/v1/eventseriedefs",
					round_rest_uri : "/service/smartool/api/v1/rounds"
				});

zmxk.config([ '$resourceProvider', function($resourceProvider) {
	// Don't strip trailing slashes from calculated URLs
	$resourceProvider.defaults.stripTrailingSlashes = false;
} ]);

zmxk.config([ '$routeProvider', function($routeProvider) {
	$routeProvider.when('/users', {
		templateUrl : 'user_list.html',
		controller : "UserCtrl"
	}).when('/users/:userId', {
		templateUrl : 'user_detail.html',
		controller : "UserDetailCtrl"
	}).when('/events/:eventId', {
		templateUrl : 'event_detail.html',
		controller : "EventDetailCtrl"
	}).when('/events', {
		templateUrl : 'event_list.html',
		controller : "EventManageCtrl"
	}).when('/sites/:siteId/events/:eventTypeId/serieses/:seriesId', {
		templateUrl : 'event_list.html',
		controller : "EventManageCtrl"
	}).when('/event_types', {
		templateUrl : 'eventtype_list.html',
		controller : "EventManageCtrl"
	}).when('/sites/:siteId/event_types/:eventTypeId', {
		templateUrl : 'eventtype_detail.html',
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
		redirectTo : '/events'
	}).when('/credit_records', {
		templateUrl : 'credit_records.html',
		controller : "CreditRecordCtrl"
	}).when('/sites', {
		templateUrl : 'site_list.html',
		controller : "CreditRecordCtrl"
	}).when('/teams', {
		templateUrl : 'team_list.html',
		controller : "TeamCtrl"
	}).when('/teams/:teamId', {
		templateUrl : 'team_member_list.html',
		controller : "TeamMemberCtrl"
	});
} ]);

zmxk.directive('appFilereader', function($q) {
	/*
	 * made by elmerbulthuis@gmail.com WTFPL licensed
	 */
	var slice = Array.prototype.slice;

	return {
		restrict : 'A',
		require : '?ngModel',
		link : function(scope, element, attrs, ngModel) {
			if (!ngModel)
				return;

			ngModel.$render = function() {
			}

			element.bind('change', function(e) {
				var element = e.target;
				if (!element.value)
					return;

				element.disabled = true;
				$q.all(slice.call(element.files, 0).map(readFile)).then(
						function(values) {
							if (element.multiple)
								ngModel.$setViewValue(values);
							else
								ngModel.$setViewValue(values.length ? values[0]
										: null);
							element.value = null;
							element.disabled = false;
						});

				function readFile(file) {
					var deferred = $q.defer();

					var reader = new FileReader()
					reader.onload = function(e) {
						deferred.resolve(e.target.result);
					}
					reader.onerror = function(e) {
						deferred.reject(e);
					}
					reader.readAsDataURL(file);
					//console.log("``````` read file ````````")
					return deferred.promise;
				}

			}); // change

		} // link

	}; // return

}) // appFilereader

/**
 * AngularJS default filter with the following expression:
 * "person in people | filter: {name: $select.search, age: $select.search}"
 * performs a AND between 'name: $select.search' and 'age: $select.search'.
 * We want to perform a OR.
 */
zmxk.filter('propsFilter', function() {
  return function(items, props) {
    var out = [];

    if (angular.isArray(items)) {
      var keys = Object.keys(props);
        
      items.forEach(function(item) {
        var itemMatches = false;

        for (var i = 0; i < keys.length; i++) {
          var prop = keys[i];
          var text = props[prop].toLowerCase();
          if (item[prop].toString().toLowerCase().indexOf(text) !== -1) {
            itemMatches = true;
            break;
          }
        }

        if (itemMatches) {
          out.push(item);
        }
      });
    } else {
      // Let the output be the input untouched
      out = items;
    }

    return out;
  };
});