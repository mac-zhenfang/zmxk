// Serie Service
zmxk.service('serieService', [ '$resource', 'zmxkConfig', '$q',
		function($resource, zmxkConfig, $q) {

			var serieResource = $resource(zmxkConfig.serie_rest_uri, {

				serieId : '@id'
			}, {
				create : {
					url : zmxkConfig.serie_rest_uri,
					method : 'POST',
					headers : {
						'Content-Type' : 'application/json'
					}
				},
				update : {
					url : zmxkConfig.serie_rest_uri,
					method : 'PUT',
					headers : {
						'Content-Type' : 'application/json'
					}
				},
				get : {
					url : zmxkConfig.serie_rest_uri,
					method : 'GET'
				},
				remove : {
					url : zmxkConfig.serie_rest_uri,
					method : 'DELETE'
				}
			})

			this.create = function(giveEventTypeId, serie) {
				var defer = $q.defer();
				serieResource.create({
					eventTypeId : giveEventTypeId
				}, serie, function(e, headers) {
					defer.resolve(e);
				}, function(e, headers) {
					defer.reject(e);
				});
				return defer.promise;
			}

			this.update = function(giveEventTypeId, giveSerieId, serie) {
				var defer = $q.defer();
				serieResource.update({
					eventTypeId : giveEventTypeId,
					serieId : giveSerieId
				}, serie, function(e, headers) {
					defer.resolve(e);
				}, function(e, headers) {
					defer.reject(e);
				});
				return defer.promise;
			}

			this.remove = function(giveEventTypeId, giveSerieId) {
				var defer = $q.defer();
				serieResource.remove({
					eventTypeId : giveEventTypeId,
					serieId : giveSerieId
				}, function(e, headers) {
					defer.resolve(e);
				}, function(e, headers) {
					defer.reject(e);
				});
				return defer.promise;
			}

			this.removeAll = function(giveEventTypeId) {
				var defer = $q.defer();
				serieResource.remove({
					eventTypeId : giveEventTypeId
				}, function(e, headers) {
					defer.resolve(e);
				}, function(e, headers) {
					defer.reject(e);
				});
				return defer.promise;
			}

			this.list = function(giveEventTypeId) {
				var defer = $q.defer();
				serieResource.query({
					eventTypeId : giveEventTypeId
				}, function(data, headers) {
					defer.resolve(data);
				}, function(data, headers) {
					defer.reject(data);
				});

				return defer.promise;
			}

			this.listAll = function() {
				var defer = $q.defer();
				serieResource.query(function(data, headers) {
					defer.resolve(data);
				}, function(data, headers) {
					defer.reject(data);
				});

				return defer.promise;
			}

			this.get = function(giveEventTypeId, giveSerieId) {
				var defer = $q.defer();
				serieResource.get({
					eventTypeId : giveEventTypeId,
					serieId : giveSerieId
				}, function(data, headers) {
					defer.resolve(data);
				}, function(data, headers) {
					defer.reject(data);
				});
				return defer.promise;
			}

		} ]);
// Site Service
zmxk.service('siteService', [ '$resource', 'zmxkConfig', '$q',
		function($resource, zmxkConfig, $q) {
			var siteResource = $resource(zmxkConfig.site_rest_uri, {
				siteId : '@id'
			}, {
				create : {
					url : zmxkConfig.site_rest_uri,
					method : 'POST',
					headers : {
						'Content-Type' : 'application/json'
					}
				},
				update : {
					url : zmxkConfig.site_rest_uri,
					method : 'PUT',
					headers : {
						'Content-Type' : 'application/json'
					}
				},
				get : {
					url : zmxkConfig.site_rest_uri,
					method : 'GET'
				},
				remove : {
					url : zmxkConfig.site_rest_uri,
					method : 'DELETE'
				}
			})

			this.create = function(site) {
				var defer = $q.defer();
				siteResource.create(site, function(e, headers) {
					defer.resolve(e);
				}, function(e, headers) {
					defer.reject(e);
				});
				return defer.promise;
			}

			this.update = function(giveSiteId, site) {
				var defer = $q.defer();
				siteResource.update({
					siteId : giveSiteId
				}, site, function(e, headers) {
					defer.resolve(e);
				}, function(e, headers) {
					defer.reject(e);
				});
				return defer.promise;
			}

			this.remove = function(giveSiteId) {
				var defer = $q.defer();
				siteResource.remove({
					siteId : giveSiteId
				}, function(e, headers) {
					defer.resolve(e);
				}, function(e, headers) {
					defer.reject(e);
				});
				return defer.promise;
			}

			this.list = function() {
				var defer = $q.defer();
				siteResource.query(function(data, headers) {
					defer.resolve(data);
				}, function(data, headers) {
					defer.reject(data);
				});

				return defer.promise;
			}

			this.get = function(giveSiteId) {
				var defer = $q.defer();
				siteResource.get({
					siteId : giveSiteId
				}, function(data, headers) {
					defer.resolve(data);
				}, function(data, headers) {
					defer.reject(data);
				});
				return defer.promise;
			}

		} ]);
// Event Type Service
zmxk.service('eventTypeService', [ '$resource', 'zmxkConfig', '$q',
		function($resource, zmxkConfig, $q) {
			var eventTypeResource = $resource(zmxkConfig.event_type_rest_uri, {
				eventTypeId : '@id'
			}, {
				create : {
					url : zmxkConfig.event_type_rest_uri,
					method : 'POST',
					headers : {
						'Content-Type' : 'application/json'
					}
				},
				update : {
					url : zmxkConfig.event_type_rest_uri,
					method : 'PUT',
					headers : {
						'Content-Type' : 'application/json'
					}
				},
				get : {
					url : zmxkConfig.event_type_rest_uri,
					method : 'GET'
				},
				remove : {
					url : zmxkConfig.event_type_rest_uri,
					method : 'DELETE'
				},
				queryBySite : {
					url : zmxkConfig.event_type_site_rest_uri,
					method : "GET",
					isArray : true,
					headers : {
						'Content-Type' : 'application/json'
					}
				}
			})

			this.createEventType = function(eventType) {
				var defer = $q.defer();
				eventTypeResource.create(eventType, function(e, headers) {
					defer.resolve(e);
				}, function(e, headers) {
					defer.reject(e);
				});
				return defer.promise;
			}

			this.updateEventType = function(giveEventTypeId, eventType) {
				var defer = $q.defer();
				eventTypeResource.update({
					eventTypeId : giveEventTypeId
				}, eventType, function(e, headers) {
					defer.resolve(e);
				}, function(e, headers) {
					defer.reject(e);
				});
				return defer.promise;
			}

			this.deleteEventType = function(giveEventTypeId) {
				var defer = $q.defer();
				eventTypeResource.remove({
					eventTypeId : giveEventTypeId
				}, function(e, headers) {
					defer.resolve(e);
				}, function(e, headers) {
					defer.reject(e);
				});
				return defer.promise;
			}

			this.list = function(s) {
				var defer = $q.defer();
				if (angular.isUndefined(s)) {
					eventTypeResource.query(function(data, headers) {
						defer.resolve(data);
					}, function(data, headers) {
						defer.reject(data);
					});
				} else {
					eventTypeResource.queryBySite({
						siteId : s
					}, function(data, headers) {
						defer.resolve(data);
					}, function(data, headers) {
						defer.reject(data);
					});
				}

				return defer.promise;
			}

			this.getEventType = function(eventTypeId) {
				var defer = $q.defer();
				eventTypeResource.get({
					eventTypeId : eventTypeId
				}, function(data, headers) {
					defer.resolve(data);
				}, function(data, headers) {
					defer.reject(data);
				});
				return defer.promise;
			}

		} ]);
// Event Service
zmxk.service('eventService', [ '$resource', 'zmxkConfig', '$q',
		function($resource, zmxkConfig, $q) {
			var eventResource = $resource(zmxkConfig.event_rest_uri, {
				eventId : '@id'
			}, {
				create : {
					url : zmxkConfig.event_rest_uri,
					method : 'POST',
					headers : {
						'Content-Type' : 'application/json'
					}
				},
				update : {
					url : zmxkConfig.event_rest_uri,
					method : 'PUT',
					headers : {
						'Content-Type' : 'application/json'
					}
				},
				get : {
					url : zmxkConfig.event_rest_uri,
					method : 'GET'
				},
				remove : {
					url : zmxkConfig.event_rest_uri,
					method : 'DELETE'
				},
				saveAttendee : {
					url : zmxkConfig.event_update_attendee_uri,
					method : "POST",
					isArray : true,
					headers : {
						'Content-Type' : 'application/json'
					}
				},
				addAttendee : {
					url : zmxkConfig.event_add_attendee_uri,
					method : "POST",
					headers : {
						'Content-Type' : 'application/json'
					}
				},
				queryBySite : {
					url : zmxkConfig.event_site_uri,
					method : "GET",
					isArray : true,
					headers : {
						'Content-Type' : 'application/json'
					}
				},
				search : {
					url : zmxkConfig.event_search_uri,
					method : "GET",
					isArray : true,
					headers : {
						'Content-Type' : 'application/json'
					}
				}
			})

			this.search = function(criteria) {
				var defer = $q.defer();
				eventResource.search(criteria, function(e, headers) {
					defer.resolve(e);
				}, function(e, headers) {
					defer.reject(e);
				});
				return defer.promise;
			}

			this.createEvent = function(event) {
				var defer = $q.defer();
				eventResource.create(event, function(e, headers) {
					defer.resolve(e);
				}, function(e, headers) {
					defer.reject(e);
				});
				return defer.promise;
			}

			this.updateEvent = function(giveEventId, event) {
				var defer = $q.defer();
				eventResource.update({
					eventId : giveEventId
				}, event, function(e, headers) {
					defer.resolve(e);
				}, function(e, headers) {
					defer.reject(e);
				});
				return defer.promise;
			}

			this.deleteEvent = function(giveEventId) {
				var defer = $q.defer();
				eventResource.remove({
					eventId : giveEventId
				}, function(e, headers) {
					defer.resolve(e);
				}, function(e, headers) {
					defer.reject(e);
				});
				return defer.promise;
			}

			this.list = function(s) {
				var defer = $q.defer();
				if (!angular.isUndefined(s) && s != null) {
					if (angular.isNumber(s)) {
						eventResource.query({
							status : s
						}, function(data, headers) {
							defer.resolve(data);
						}, function(data, headers) {
							defer.reject(data);
						});
					} else if (angular.isString(s)) {
						eventResource.queryBySite({
							siteId : s
						}, function(data, headers) {
							defer.resolve(data);
						}, function(data, headers) {
							defer.reject(data);
						});
					}
				} else {
					eventResource.query(function(data, headers) {
						defer.resolve(data);
					}, function(data, headers) {
						defer.reject(data);
					});
				}
				return defer.promise;
			}

			this.getEvent = function(eventId) {
				var defer = $q.defer();
				eventResource.get({
					eventId : eventId
				}, function(data, headers) {
					defer.resolve(data);
				}, function(data, headers) {
					defer.reject(data);
				});
				return defer.promise;
			}

			this.addAttendee = function(eventId, attendeeData) {
				var defer = $q.defer();
				eventResource.addAttendee({
					eventId : eventId
				}, attendeeData, function(body, headers) {
					console.log(body.data);
					defer.resolve(body);
				}, function(body, headers) {
					console.log(body);
					defer.reject(body);
				})
				return defer.promise;
			}
			// event_update_attendee_uri
			this.saveAttendee = function(eventId, attendeeData) {
				var defer = $q.defer();
				eventResource.saveAttendee({
					eventId : eventId
				}, attendeeData, function(body, headers) {
					console.log(body);
					defer.resolve(body);
				}, function(body, headers) {
					console.log(body);
					defer.reject(body);
				})
				return defer.promise;
			}

		} ]);
// Tag service
zmxk.service('tagService', [ '$resource', 'zmxkConfig', '$q',
		function($resource, zmxkConfig, $q) {
			var tagResource = $resource(zmxkConfig.tag_rest_uri, {
				tagId : '@id'
			}, {
				search : {
					url : zmxkConfig.tag_search_uri,
					type : "@type",
					method : "GET",
					isArray : true
				}
			});

			this.search = function(type) {
				var defer = $q.defer();
				tagResource.search({
					type : type
				}, function(body, headers) {
					defer.resolve(body);
				}, function(body, headers) {
					console.log(body);
					defer.reject(body);
				})
				return defer.promise;
			}
		} ]);
// Kid Service
zmxk.service('kidService', [ '$resource', 'zmxkConfig', '$q',
		function($resource, zmxkConfig, $q) {
			var kidResource = $resource(zmxkConfig.kid_rest_uri, {
				kidId : '@id',
				kidId : '@kidId',
				teamId : '@teamId'
			}, {
				create : {
					url : zmxkConfig.kid_rest_uri,
					method : 'POST',
					headers : {
						'Content-Type' : 'application/json'
					}
				},
				update : {
					url : zmxkConfig.kid_rest_uri,
					method : 'PUT',
					headers : {
						'Content-Type' : 'application/json'
					}
				},
				get : {
					url : zmxkConfig.kid_rest_uri,
					method : 'GET'
				},
				remove : {
					url : zmxkConfig.kid_rest_uri,
					method : 'DELETE'
				},
				joinTeam: {
					url : zmxkConfig.kid_join_team_uri,
					method : 'POST'
				},
				leaveTeam: {
					url : zmxkConfig.kid_leave_team_uri,
					method : 'POST'
				}
			})

			this.getKid = function(giveUserId, giveKidId) {
				var defer = $q.defer();
				kidResource.get({
					kidId : giveKidId,
					userId : giveUserId
				}, function(kid, headers) {
					defer.resolve(kid);
				}, function(kid, headers) {
					defer.reject(kid);
				});
				return defer.promise;
			}
			this.createKid = function(giveUserId, kid) {
				var defer = $q.defer();
				kidResource.create({
					userId : giveUserId
				}, kid, function(kid, headers) {
					defer.resolve(kid);
				}, function(kid, headers) {
					defer.reject(kid);
				});
				return defer.promise;
			}

			this.updateKid = function(giveUserId, giveKidId, kid) {
				var defer = $q.defer();
				kidResource.update({
					userId : giveUserId,
					kidId : giveKidId
				}, kid, function(kid, headers) {
					defer.resolve(kid);
				}, function(kid, headers) {
					defer.reject(kid);
				});
				return defer.promise;
			}

			this.deleteKid = function(giveUserId, giveKidId) {
				var defer = $q.defer();
				kidResource.remove({
					userId : giveUserId,
					kidId : giveKidId
				}, function(kid, headers) {
					defer.resolve(kid);
				}, function(kid, headers) {
					defer.reject(kid);
				});
				return defer.promise;
			}

			this.joinTeam = function(kidId, teamId) {
				var defer = $q.defer();
				kidResource.joinTeam({
					kidId : kidId,
					teamId : teamId
				}, function(kid, headers) {
					defer.resolve(kid);
				}, function(kid, headers) {
					defer.reject(kid);
				});
				return defer.promise;
			}

			this.leaveTeam = function(kidId, teamId) {
				var defer = $q.defer();
				kidResource.leaveTeam({
					kidId : kidId,
					teamId : teamId
				}, function(kid, headers) {
					defer.resolve(kid);
				}, function(kid, headers) {
					defer.reject(kid);
				});
				return defer.promise;
			}
		} ]);
// User Service
zmxk.service('userService', [ '$resource', 'zmxkConfig', '$q',
		function($resource, zmxkConfig, $q) {
			var usersResource = $resource(zmxkConfig.user_rest_uri, {
				userId : '@id'
			}, {
				login : {
					url : zmxkConfig.user_login_uri,
					method : 'Post',
					headers : {
						'Content-Type' : 'application/x-www-form-urlencoded'
					}
				},
				logout : {
					url : zmxkConfig.user_logout_uri,
					method : 'Post'
				},
				search : {
					url : zmxkConfig.user_search_uri,
					query : "@id",
					method : "GET",
					isArray : true
				},
				create : {
					url : zmxkConfig.user_rest_uri,
					method : 'POST',
					headers : {
						'Content-Type' : 'application/json'
					}
				},
				update : {
					url : zmxkConfig.user_rest_uri,
					method : 'PUT',
					headers : {
						'Content-Type' : 'application/json'
					}
				},
				get : {
					url : zmxkConfig.user_rest_uri,
					method : 'GET'
				},
				remove : {
					url : zmxkConfig.user_rest_uri,
					method : 'DELETE'
				},
				queryUsers : {
					url : zmxkConfig.user_query_uri,
					isArray : true,
					method : 'POST'
				}
			})
			this.query = function(mobileNumber, wcId, kidName) {
				var defer = $q.defer();
				var users = usersResource.queryUsers({
					mobileNumber : mobileNumber,
					wcId : wcId,
					kidName : kidName
				}, function(data) {
					defer.resolve(data);
				}, function(data) {
					defer.reject(data);
				});
				return defer.promise;
			};
			this.login = function() {
				// TODO
			};

			this.register = function() {
				// TODO
			}

			this.list = function() {
				var defer = $q.defer();
				var users = usersResource.query({}, function(data) {
					defer.resolve(data);
				}, function(data) {
				});
				return defer.promise;
			}

			this.getUser = function(giveUserId) {
				var defer = $q.defer();
				usersResource.get({
					userId : giveUserId
				}, function(user, headers) {
					defer.resolve(user);
				}, function(user, headers) {
					defer.reject(user);
				});
				return defer.promise;
			}

			this.search = function(query) {
				var defer = $q.defer();
				usersResource.search({
					query : query
				}, function(body, headers) {
					defer.resolve(body);
				}, function(body, headers) {
					console.log(body);
					defer.reject(body);
				})
				return defer.promise;
			}

			this.create = function(user) {
				var defer = $q.defer();
				usersResource.create(user, function(body, headers) {
					defer.resolve(body);
				}, function(body, headers) {
					console.log(body);
					defer.reject(body);
				})
				return defer.promise;
			}
			this.update = function(giveUserId, user) {
				var defer = $q.defer();
				usersResource.update({
					userId : giveUserId
				}, user, function(u, headers) {
					defer.resolve(u);
				}, function(u, headers) {
					defer.reject(u);
				});
				return defer.promise;
			}

			this.deleteUser = function(giveUserId) {
				var defer = $q.defer();
				usersResource.remove({
					userId : giveUserId
				}, function(u, headers) {
					defer.resolve(u);
				}, function(u, headers) {
					defer.reject(u);
				});
				return defer.promise;
			}

		} ]);
zmxk.service('ruleService', [ '$resource', 'zmxkConfig', '$q',
		function($resource, zmxkConfig, $q) {
			var ruleResource = $resource(zmxkConfig.rule_get_uri, {
				creditRuleId : '@id',
				attendeeId : '@attendeeId',
				userId : '@userId'
			}, {
				create : {
					url : zmxkConfig.rule_create_uri,
					method : 'POST',
					headers : {
						'Content-Type' : 'application/json'
					}
				},
				update : {
					url : zmxkConfig.rule_update_uri,
					method : 'PUT',
					headers : {
						'Content-Type' : 'application/json'
					}
				},
				get : {
					url : zmxkConfig.rule_get_uri,
					method : 'GET'
				},
				remove : {
					url : zmxkConfig.rule_remove_uri,
					method : 'DELETE'
				},
				search : {
					url : zmxkConfig.rule_search_uri,
					method : "GET",
					isArray : true
				},
				apply : {
					url : zmxkConfig.rule_apply_uri,
					method : "POST"
				},
				applyToUser : {
					url : zmxkConfig.rule_apply_to_user_uri,
					method : "POST"
				}
				
			});
			this.create = function(creditRule) {
				var defer = $q.defer();
				ruleResource.create(creditRule, function(data, header) {
					defer.resolve(data);
				}, function(data, header) {
					defer.reject(data);
				});
				return defer.promise;
			};
			this.update = function(creditRule) {
				var defer = $q.defer();
				ruleResource.update({}, creditRule, function(data, header) {
					defer.resolve(data);
				}, function(data, header) {
					defer.reject(data);
				});
				return defer.promise;
			};
			this.remove = function(creditRuleId) {
				var defer = $q.defer();
				ruleResource.remove({}, {
					id : creditRuleId
				}, function(data, header) {
					defer.resolve(data);
				}, function(data, header) {
					defer.reject(data);
				});
				return defer.promise;
			};
			this.listAll = function() {
				var defer = $q.defer();
				ruleResource.search({}, function(data, header) {
					defer.resolve(data);
				}, function(data, header) {
					defer.reject(data);
				});
				return defer.promise;
			};
			this.apply = function(creditRule) {
				var defer = $q.defer();
				ruleResource.apply({}, creditRule, function(data, header) {
					defer.resolve(data);
				}, function(data, header) {
					defer.reject(data);
				});
				return defer.promise;
			};
			this.applyToUser = function(creditRule) {
				var defer = $q.defer();
				ruleResource.applyToUser({}, creditRule, function(data, header) {
					defer.resolve(data);
				}, function(data, header) {
					defer.reject(data);
				});
				return defer.promise;
			};
		} ]);
zmxk.service('eventRuleService', [ '$resource', 'zmxkConfig', '$q',
		function($resource, zmxkConfig, $q) {
			var eventRuleService = $resource(zmxkConfig.event_rule_get_uri, {
				eventCreditRuleId : '@id',
				attendeeId : '@attendeeId'
			}, {
				create : {
					url : zmxkConfig.event_rule_create_uri,
					method : 'POST',
					headers : {
						'Content-Type' : 'application/json'
					}
				},
				update : {
					url : zmxkConfig.event_rule_update_uri,
					method : 'PUT',
					headers : {
						'Content-Type' : 'application/json'
					}
				},
				get : {
					url : zmxkConfig.event_rule_get_uri,
					method : 'GET'
				},
				remove : {
					url : zmxkConfig.event_rule_remove_uri,
					method : 'DELETE'
				},
				search : {
					url : zmxkConfig.event_rule_search_uri,
					method : "GET",
					isArray : true
				},
				apply : {
					url : zmxkConfig.event_rule_apply_uri,
					method : "POST"
				}
			});
			this.create = function(creditRule) {
				var defer = $q.defer();
				eventRuleService.create(creditRule, function(data, header) {
					defer.resolve(data);
				}, function(data, header) {
					defer.reject(data);
				});
				return defer.promise;
			};
			this.update = function(creditRule) {
				var defer = $q.defer();
				eventRuleService.update({}, creditRule, function(data, header) {
					defer.resolve(data);
				}, function(data, header) {
					defer.reject(data);
				});
				return defer.promise;
			};
			this.remove = function(creditRuleId) {
				var defer = $q.defer();
				eventRuleService.remove({}, {
					id : creditRuleId
				}, function(data, header) {
					defer.resolve(data);
				}, function(data, header) {
					defer.reject(data);
				});
				return defer.promise;
			};
			this.listAll = function() {
				var defer = $q.defer();
				eventRuleService.search({}, function(data, header) {
					defer.resolve(data);
				}, function(data, header) {
					defer.reject(data);
				});
				return defer.promise;
			};
			this.apply = function() {
				var defer = $q.defer();
				eventRuleResource.apply({}, eventCreditRule, function(data, header) {
					defer.resolve(data);
				}, function(data, header) {
					defer.reject(data);
				});
				return defer.promise;
			};
		} ]);
zmxk
		.service(
				'creditRecordService',
				[
						'$resource',
						'zmxkConfig',
						'$q',
						function($resource, zmxkConfig, $q) {
							var creditRecordService = $resource(
									zmxkConfig.event_credit_records_search_uri,
									{
										start : '@start',
										end : '@end',
										mobileNum : '@mobileNum',
										creditRecordId : '@creditRecordId'
									},
									{
										search : {
											url : zmxkConfig.event_credit_records_search_uri,
											method : "GET",
											isArray : true
										},
										searchMine : {
											url : zmxkConfig.my_event_credit_records_search_uri,
											method : "GET",
											isArray : true
										},
										withdraw : {
											url : zmxkConfig.withdraw_credit_records_uri,
											method : "POST"
										},
										recoverWithdraw : {
											url : zmxkConfig.recover_withdraw_credit_records_uri,
											method : "POST"
										}
									});
							this.listAll = function(_mobileNum, _start, _end) {
								var defer = $q.defer();
								creditRecordService.search({
									mobileNum : _mobileNum,
									start : _start,
									end : _end
								}, function(data, header) {
									defer.resolve(data);
								}, function(data, header) {
									defer.reject(data);
								});
								return defer.promise;
							};
							this.listAllMine = function(_start, _end) {
								var defer = $q.defer();
								creditRecordService.searchMine({
									start : _start,
									end : _end
								}, function(data, header) {
									defer.resolve(data);
								}, function(data, header) {
									defer.reject(data);
								});
								return defer.promise;
							};
							this.withdraw = function(_creditRecordId) {
								var defer = $q.defer();
								creditRecordService.withdraw({
									creditRecordId : _creditRecordId
								}, function(data, header) {
									defer.resolve(data);
								}, function(data, header) {
									defer.reject(data);
								});
								return defer.promise;
							};
							this.recoverWithdraw = function(_creditRecordId) {
								var defer = $q.defer();
								creditRecordService.recoverWithdraw({
									creditRecordId : _creditRecordId
								}, function(data, header) {
									defer.resolve(data);
								}, function(data, header) {
									defer.reject(data);
								});
								return defer.promise;
							};
						} ]);
zmxk.service('teamService', [
		'$resource',
		'zmxkConfig',
		'$q',
		function($resource, zmxkConfig, $q) {
			var teamResource = $resource(zmxkConfig.team_get_uri, {
				teamId : '@teamId',
			}, {
				create : {
					url : zmxkConfig.team_create_uri,
					method : 'POST',
					headers : {
						'Content-Type' : 'application/json'
					}
				},
				update : {
					url : zmxkConfig.team_update_uri,
					method : 'PUT',
					headers : {
						'Content-Type' : 'application/json'
					}
				},
				get : {
					url : zmxkConfig.team_get_uri,
					method : 'GET'
				},
				list : {
					url : zmxkConfig.team_list_uri,
					method : 'GET',
					isArray : true
				},
				listMembers : {
					url : zmxkConfig.team_list_members_uri,
					method : 'GET',
					isArray : true
				},
				remove : {
					url : zmxkConfig.team_remove_uri,
					method : 'DELETE'
				}
			});
			this.create = function(team) {
				var defer = $q.defer();
				teamResource.create(team, function(data, header) {
					defer.resolve(data);
				}, function(data, header) {
					defer.reject(data);
				});
				return defer.promise;
			};
			this.update = function(team) {
				var defer = $q.defer();
				teamResource.update({}, team, function(data, header) {
					defer.resolve(data);
				}, function(data, header) {
					defer.reject(data);
				});
				return defer.promise;
			};
			this.get = function(teamId) {
				var defer = $q.defer();
				teamResource.get({teamId, teamId}, function(data, header) {
					defer.resolve(data);
				}, function(data, header) {
					defer.reject(data);
				});
				return defer.promise;
			};
			this.remove = function(teamId) {
				var defer = $q.defer();
				teamResource.remove({}, {
					id : teamId
				}, function(data, header) {
					defer.resolve(data);
				}, function(data, header) {
					defer.reject(data);
				});
				return defer.promise;
			};
			this.listAll = function() {
				var defer = $q.defer();
				teamResource.list({}, function(data, header) {
					defer.resolve(data);
				}, function(data, header) {
					defer.reject(data);
				});
				return defer.promise;
			};
			this.listMembers = function(teamId) {
				var defer = $q.defer();
				teamResource.listMembers({teamId, teamId}, function(data, header) {
					defer.resolve(data);
				}, function(data, header) {
					defer.reject(data);
				});
				return defer.promise;
			};
			
		} ]);