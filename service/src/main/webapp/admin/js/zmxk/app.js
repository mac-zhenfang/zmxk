var currentModule;
var zmxk = angular.module('zmxk', [ 'ngResource', 'datePicker', 'ui.bootstrap',
		'dialogs', 'ngRoute', 'ngCookies'], function(
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
					event_type_rest_uri : "/service/smartool/api/v1/eventtypes/:eventTypeId",
					event_type_site_rest_uri : "/service/smartool/api/v1/:siteId/eventtypes/",
					event_add_attendee_uri : "/service/smartool/api/v1/events/:eventId/enroll",
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
					team_list_members_uri : "/service/smartool/api/v1/teams/:teamId/members"
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

					return deferred.promise;
				}

			}); // change

		} // link

	}; // return

}) // appFilereader
;

zmxk
		.service(
				'imageService',
				function($http, $q, $timeout) {
					var NUM_LOBES = 3
					var lanczos = lanczosGenerator(NUM_LOBES)

					// resize via lanczos-sinc convolution
					this.resize = function(imgData, width, height) {
						var img = document.createElement('img');
						img.src = imgData;
						var self = {}

						self.type = "image/png"
						self.quality = 1.0
						self.resultD = $q.defer()

						self.canvas = document.createElement('canvas')

						self.ctx = getContext(self.canvas)
						self.ctx.imageSmoothingEnabled = true
						self.ctx.mozImageSmoothingEnabled = true
						self.ctx.oImageSmoothingEnabled = true
						self.ctx.webkitImageSmoothingEnabled = true

						if (img.naturalWidth <= width
								|| img.naturalHeight <= height) {
							console.log("FAST resizing image",
									img.naturalWidth, img.naturalHeight, "=>",
									width, height)

							self.canvas.width = width
							self.canvas.height = height
							self.ctx.drawImage(img, 0, 0, width, height)
							resolveLanczos(self)
						} else {
							console.log("SLOW resizing image",
									img.naturalWidth, img.naturalHeight, "=>",
									width, height)

							self.canvas.width = img.naturalWidth
							self.canvas.height = img.naturalHeight
							self.ctx.drawImage(img, 0, 0, self.canvas.width,
									self.canvas.height)

							self.img = img
							self.src = self.ctx.getImageData(0, 0,
									self.canvas.width, self.canvas.height)
							self.dest = {
								width : width,
								height : height
							}
							self.dest.data = new Array(self.dest.width
									* self.dest.height * 4)

							self.ratio = img.naturalWidth / width
							self.rcpRatio = 2 / self.ratio
							self.range2 = Math.ceil(self.ratio * NUM_LOBES / 2)
							self.cacheLanc = {}
							self.center = {}
							self.icenter = {}

							$timeout(function() {
								applyLanczosColumn(self, 0)
							})
						}

						return self.resultD.promise
					}

					function applyLanczosColumn(self, u) {
						self.center.x = (u + 0.5) * self.ratio
						self.icenter.x = self.center.x | 0

						for (var v = 0; v < self.dest.height; v++) {
							self.center.y = (v + 0.5) * self.ratio
							self.icenter.y = self.center.y | 0

							var a, r, g, b
							a = r = g = b = 0

							var norm = 0
							var idx

							for (var i = self.icenter.x - self.range2; i <= self.icenter.x
									+ self.range2; i++) {
								if (i < 0 || i >= self.src.width)
									continue
								var fX = (1000 * Math.abs(i - self.center.x)) | 0
								if (!self.cacheLanc[fX]) {
									self.cacheLanc[fX] = {}
								}

								for (var j = self.icenter.y - self.range2; j <= self.icenter.y
										+ self.range2; j++) {
									if (j < 0 || j >= self.src.height)
										continue

									var fY = (1000 * Math
											.abs(j - self.center.y)) | 0
									if (self.cacheLanc[fX][fY] === undefined) {
										self.cacheLanc[fX][fY] = lanczos(Math
												.sqrt(Math.pow(fX
														* self.rcpRatio, 2)
														+ Math
																.pow(
																		fY
																				* self.rcpRatio,
																		2)) / 1000)
									}

									var weight = self.cacheLanc[fX][fY]
									if (weight > 0) {
										idx = (j * self.src.width + i) * 4
										norm += weight

										r += weight * self.src.data[idx + 0]
										g += weight * self.src.data[idx + 1]
										b += weight * self.src.data[idx + 2]
										a += weight * self.src.data[idx + 3]
									}
								}
							}

							idx = (v * self.dest.width + u) * 4
							self.dest.data[idx + 0] = r / norm
							self.dest.data[idx + 1] = g / norm
							self.dest.data[idx + 2] = b / norm
							self.dest.data[idx + 3] = a / norm
						}

						if (++u < self.dest.width) {
							if (u % 16 === 0) {
								$timeout(function() {
									applyLanczosColumn(self, u)
								})
							} else {
								applyLanczosColumn(self, u)
							}
						} else {
							$timeout(function() {
								finalizeLanczos(self)
							})
						}
					}

					function finalizeLanczos(self) {
						self.canvas.width = self.dest.width
						self.canvas.height = self.dest.height
						// self.ctx.drawImage(self.img, 0, 0, self.dest.width,
						// self.dest.height)
						self.src = self.ctx.getImageData(0, 0, self.dest.width,
								self.dest.height)
						var idx
						for (var i = 0; i < self.dest.width; i++) {
							for (var j = 0; j < self.dest.height; j++) {
								idx = (j * self.dest.width + i) * 4
								self.src.data[idx + 0] = self.dest.data[idx + 0]
								self.src.data[idx + 1] = self.dest.data[idx + 1]
								self.src.data[idx + 2] = self.dest.data[idx + 2]
								self.src.data[idx + 3] = self.dest.data[idx + 3]
							}
						}
						self.ctx.putImageData(self.src, 0, 0)
						resolveLanczos(self)
					}

					function resolveLanczos(self) {
						var result = new Image()

						result.onload = function() {
							self.resultD.resolve(result)
						}

						result.onerror = function(err) {
							self.resultD.reject(err)
						}

						result.src = self.canvas.toDataURL(self.type,
								self.quality)
					}

					// resize by stepping down
					this.resizeStep = function(img, width, height, quality) {
						quality = quality || 1.0

						var resultD = $q.defer()
						var canvas = document.createElement('canvas')
						var context = getContext(canvas)
						var type = "image/png"

						var cW = img.naturalWidth
						var cH = img.naturalHeight

						var dst = new Image()
						var tmp = null

						// resultD.resolve(img)
						// return resultD.promise

						function stepDown() {
							cW = Math.max(cW / 2, width) | 0
							cH = Math.max(cH / 2, height) | 0

							canvas.width = cW
							canvas.height = cH

							context.drawImage(tmp || img, 0, 0, cW, cH)

							dst.src = canvas.toDataURL(type, quality)

							if (cW <= width || cH <= height) {
								return resultD.resolve(dst)
							}

							if (!tmp) {
								tmp = new Image()
								tmp.onload = stepDown
							}

							tmp.src = dst.src
						}

						if (cW <= width || cH <= height || cW / 2 < width
								|| cH / 2 < height) {
							canvas.width = width
							canvas.height = height
							context.drawImage(img, 0, 0, width, height)
							dst.src = canvas.toDataURL(type, quality)

							resultD.resolve(dst)
						} else {
							stepDown()
						}

						return resultD.promise
					}

					function getContext(canvas) {
						var context = canvas.getContext('2d')

						context.imageSmoothingEnabled = true
						context.mozImageSmoothingEnabled = true
						context.oImageSmoothingEnabled = true
						context.webkitImageSmoothingEnabled = true

						return context
					}

					// returns a function that calculates lanczos weight
					function lanczosGenerator(lobes) {
						var recLobes = 1.0 / lobes

						return function(x) {
							if (x > lobes)
								return 0
							x *= Math.PI
							if (Math.abs(x) < 1e-16)
								return 1
							var xx = x * recLobes
							return Math.sin(x) * Math.sin(xx) / x / xx
						}
					}
				})