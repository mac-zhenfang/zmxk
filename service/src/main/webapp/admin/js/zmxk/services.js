// Event Service
zmxk.service('eventService', [ '$resource', 'zmxkConfig', '$q',
		function($resource, zmxkConfig, $q) {
			var eventResource = $resource(zmxkConfig.event_rest_uri, {
				eventId : '@id'
			}, {
				saveAttendee : {
					url : zmxkConfig.event_update_attendee_uri,
					method : "POST",
					isArray: true,
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
				}
			})
			this.list = function(s) {
				var defer = $q.defer();
				if (!angular.isUndefined(s)) {
					eventResource.query({
						status : s
					}, function(data, headers) {
						defer.resolve(data);
					}, function(data, headers) {
						defer.reject(data);
					});
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
				}
			})
			this.login = function() {
				// TODO
			};

			this.register = function() {
				// TODO
			}

			this.list = function() {
				var users = usersResource.query(function(data) {
					console.log(data);
				});
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
					console.log(body);
					defer.resolve(body);
				}, function(body, headers) {
					console.log(body);
					defer.reject(body);
				})
				return defer.promise;
			}

		} ]);