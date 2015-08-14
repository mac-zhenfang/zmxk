zmxk.controller('UserCtrl', [
		'$scope',
		'$location',
		'userService',
		'siteService',
		'$window',
		'$dialogs',
		'$routeParams',
		function($scope, $location, userService, siteService, $window, $dialogs,
				$routeParams) {
			$scope.updateLabel = "修改";
			$scope.deleteLabel = "删除";
			$scope.userList = [];
			$scope.sites = [ {
				id : null,
				name : "所有"
			}];
			$scope.init = function() {

				userService.list().then(function(data) {
					angular.forEach(data, function(user, index) {
						user["existed"] = true;
						user["changed"] = false;
						user["showInput"] = false;
						
						$scope.userList.push(user);
					})
				}, function(data) {
				});
				
				siteService.list().then(function(data){
					angular.forEach(data, function(site, index){
						$scope.sites.push(site);
					})
				}, function(data){});
				
				// console.log($scope.userList);
			}

			// FIXME can not use id, need to use index
			$scope.updateUser = function(user, idx) {
				// save kid
				if (!user.existed) {
					userService.create(user).then(function(data) {
						user = data;
					}, function(data) {

					})
				} else if (user.changed) {
					userService.update(user.id, user).then(function(data) {
						user = data;
					}, function(data) {

					});
				}

				$scope.$watch(user, function(oldValue, newValue) {
					if (user.existed) {
						user.changed = !user.changed;
					}

					user.showInput = !user.showInput;
					// $scope.kids = kid;
					var users = [];
					angular.forEach($scope.userList, function(u, i) {

						if (i == idx) {
							console.log(i + "~~~" + idx);
							users.push(user);
						} else {
							users.push(u);
						}
					})
					$scope.userList = angular.copy(users);
				})

			}

			$scope.deleteUser = function(toDeleteUser, idx) {

				console.log(toDeleteUser);
				if (!toDeleteUser.existed) {
					toDeleteKid = null;
				} else {
					userService.deleteUser(toDeleteUser.id).then(
							function(data) {
								toDeleteUser = null;
							},
							function(error) {
								$scope.launch("error", "", error.data.message,
										function() {

										}, function() {
										});
								toDeleteUser = toDeleteUser;
							})
				}
				$scope.$watch(toDeleteUser, function(oldValue, newValue) {
					if (oldValue != newValue) {
						var newUsers = [];
						angular.forEach($scope.userList, function(user, i) {
							if (idx != i) {
								newUsers.push(angular.copy(user));
							}
						});
						$scope.userList = newUsers;
						console.log($scope.userList)
					}
				})

			}

			/*
			 * $scope.createUser = function() { var toCreateUser = { }
			 * toCreateUser["existed"] = false; toCreateUser["changed"] = true;
			 * toCreateUser["showInput"] = true;
			 * $scope.userList.push(toCreateUser); }
			 */

		} ]);

zmxk.controller('UserDetailCtrl', [
		'$scope',
		'$location',
		'userService',
		'$window',
		'$dialogs',
		'$routeParams',
		'kidService',
		'zmxkConstant',
		function($scope, $location, userService, $window, $dialogs,
				$routeParams, kidService, zmxkConstant) {
			$scope.updateLabel = "修改";
			$scope.deleteLabel = "删除";
			$scope.kids = [];
			$scope.userId;
			$scope.schoolOptions = zmxkConstant.kids_school_options;

			$scope.init = function() {
				$scope.userId = $routeParams.userId;
				if (!angular.isUndefined($scope.userId)) {
					userService.getUser($scope.userId).then(function(data) {

						if (!angular.isUndefined(data.kids) && data.kids) {
							angular.forEach(data.kids, function(kid, index) {
								kid["existed"] = true;
								kid["changed"] = false;
								kid["showInput"] = false;
								// kid["updateLabel"] = $scope.updateLabel;
								$scope.kids.push(kid);
							})
							console.log($scope.kids);
						}

					}, function(data) {
					});
				}
			}

			// FIXME can not use id, need to use index
			$scope.updateKid = function(updateKid, idx) {

				// save kid
				if (!updateKid.id && updateKid.changed) {
					kidService.createKid($scope.userId, updateKid).then(
							function(data) {
								var createdKid;
								$scope.newKids = [];
								angular.forEach($scope.kids, function(kid, index) {
									if (index == idx) {
										createdKid = angular.copy(data);
										$scope.newKids.push(createdKid);
									} else {
										$scope.newKids.push(angular.copy(kid));
									}
								});

								createdKid.showInput = false;
								$scope.kids = $scope.newKids;
							}, function(data){
								alert(data.data.message);
								return;
							})
				} else if (updateKid.changed) {
					kidService
							.updateKid($scope.userId, updateKid.id, updateKid)
							.then(
									function(data) {
										var updatedKid;
										$scope.newKids = [];
										angular.forEach($scope.kids, function(kid, index) {
											if (index == idx) {
												updatedKid = angular.copy(data);
												$scope.newKids.push(updatedKid);
											} else {
												$scope.newKids.push(angular.copy(kid));
											}
										});

										createdKid.showInput = false;
										$scope.kids = $scope.newKids;
									}, function(data){
										alert(data.data.message);
										return;
									});
				}

				$scope.$watch(updateKid, function() {
					if (updateKid.existed) {
						updateKid.changed = !updateKid.changed;
					}

					updateKid.showInput = !updateKid.showInput;
					// $scope.kids = kid;
					var kids = [];
					angular.forEach($scope.kids, function(kid, i) {

						if (i == idx) {
							console.log(i + "~~~" + idx);
							kids.push(updateKid);
						} else {
							kids.push(kid);
						}
					})
					$scope.kids = angular.copy(kids);
				})

			}

			$scope.deleteKid = function(toDeleteKid, idx) {
				if(toDeleteKid.id){
					if(confirm("确定要删除吗？")){
						kidService.deleteKid($scope.userId, toDeleteKid.id).then(
								function(data) {
									$scope.newKids = [];
									angular.forEach($scope.kids, function(kid, index) {
										if (index != idx) {
											$scope.newKids.push(kid);
										}
									});
									$scope.kids = $scope.newKids;
								}, function(data) {
									alert(data.data.message);
									return;
								});
					}
				} else {
					$scope.newKids = [];
					angular.forEach($scope.kids, function(kid, index) {
						if (index != idx) {
							$scope.newKids.push(kid);
						}
					});
					$scope.kids = $scope.newKids;
				}
				if (!toDeleteKid.existed) {
					toDeleteKid = null;
				} else {
					kidService.deleteKid($scope.userId, toDeleteKid.id).then(
							function(data) {
								toDeleteKid = null;
							},
							function(data) {
								$scope.launch("error", "", error.data.message,
										function() {

										}, function() {
										});
							})
				}
				$scope.$watch(toDeleteKid, function(oldValue, newValue) {
					if (oldValue != newValue) {
						var newUsers = [];
						angular.forEach($scope.kids, function(kid, i) {
							if (idx != i) {
								newUsers.push(angular.copy(kid));
							}
						});
						$scope.kids = newUsers;
						console.log($scope.kids)
					}
				})

			}

			$scope.createKid = function() {
				var toCreateKid = {

				}
				toCreateKid["existed"] = false;
				toCreateKid["changed"] = true;
				toCreateKid["showInput"] = true;
				toCreateKid["userId"] = $scope.userId;
				$scope.kids.push(toCreateKid);
				/*
				 * angular.forEach($scope.kids, function(kid, index){ })
				 */
				// create all existed == false
				// update all changed == true
			}

		} ]);