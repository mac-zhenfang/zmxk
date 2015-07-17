zmxk.controller('UserCtrl', [
		'$scope',
		'$location',
		'userService',
		'$window',
		'$dialogs',
		'$routeParams',
		function($scope, $location, userService, $window, $dialogs,
				$routeParams) {
			$scope.updateLabel = "修改";
			$scope.deleteLabel = "删除";
			$scope.userList = [];

			$scope.roles_option = [ {
				value : 0,
				label : "用户"
			}, {
				value : 1,
				label : "操作人员"
			}, {
				value : 2,
				label : "管理员"
			} ];

			$scope.credit_option = [ {
				label : "首次充值-多次体验(10次)",
				value : 1000
			}, {
				label : "首次充值-单次体验",
				value : 100
			}, {
				label : "首次充值-VIP",
				value : 1050000
			}, {
				label : "日常充值",
				value : 100
			}, {
				label : "活动充值",
				value : 100
			} ]

			$scope.init = function() {
				// FIXME FAKE data
				/*
				 * var user = { id : "mac-test-user-id-" + i, name : "Mac Test
				 * User " + i, roleId : i % 3, mobileNum : "13706516651", credit :
				 * 100 * i, createdTime : 1436339694000, existed : true, changed :
				 * false, showInput : false }
				 */

				userService.list().then(function(data) {
					angular.forEach(data, function(attendee, index) {
						attendee["existed"] = true;
						attendee["changed"] = false;
						attendee["showInput"] = false;
						$scope.userList.push(attendee);
					})
				}, function(data) {
				});

				// console.log($scope.userList);
			}

			// FIXME can not use id, need to use index
			$scope.updateUser = function(userIndex) {
				console.log(userIndex)
				var updateUser;
				newUsers = [];
				angular.forEach($scope.userList, function(user, index) {
					// console.log(index + "~~~" + userIndex);
					if (index == userIndex) {
						updateUser = angular.copy(user);
						console.log(updateUser);
						newUsers.push(updateUser);
					} else {
						newUsers.push(angular.copy(user));
					}
				});
				if (updateUser.existed) {
					updateUser.changed = !updateUser.changed;
				}

				if (updateUser.changed) {
					$scope.updateLabel = "确定";
				} else {
					$scope.updateLabel = "修改";
				}
				updateUser.showInput = !updateUser.showInput;
				$scope.userList = newUsers;
				console.log($scope.userList);
				// console.log($scope.eventUsers);
			}

			$scope.deleteUser = function(userIndex) {
				var updateUser;
				console.log(userIndex)
				var newUsers = [];
				angular.forEach($scope.userList, function(user, index) {
					if (index != userIndex) {
						updateUser = angular.copy(user);
						newUsers.push(updateUser);
					}
				});
				$scope.userList = newUsers;
				console.log($scope.userList)
			}

			$scope.saveUser = function() {

				// create all existed == false
				// update all changed == true
			}

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
				console.log($scope.schoolOptions);
				// FIXME FAKE data
				/*
				 * var user = { id : "mac-test-user-id-" + i, name : "Mac Test
				 * User " + i, roleId : i % 3, mobileNum : "13706516651", credit :
				 * 100 * i, createdTime : 1436339694000, existed : true, changed :
				 * false, showInput : false }
				 */
				$scope.userId = $routeParams.userId;
				console.log($scope.userId)
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
				if (!updateKid.existed) {
					kidService.createKid($scope.userId, updateKid).then(
							function(data) {
								updateKid = data;
							}, function(data) {

							})
				} else if (updateKid.changed) {
					kidService
							.updateKid($scope.userId, updateKid.id, updateKid)
							.then(function(data) {
								updateKid = data;

							}, function(data) {

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
				console.log(toDeleteKid);
				if (!toDeleteKid.existed) {
					toDeleteKid = null;
				} else {
					kidService.deleteKid($scope.userId, toDeleteKid.id).then(function(data){
						toDeleteKid = null;
					}, function(data){
						
					})
				}
				$scope.$watch(toDeleteKid, function() {
					var newUsers = [];
					angular.forEach($scope.kids, function(kid, i) {
						if (idx != i) {
							newUsers.push(angular.copy(kid));
						}
					});
					$scope.kids = newUsers;
					console.log($scope.kids)
				})
				
				
			}

			$scope.createKid = function() {
				var toCreateKid = {

				}
				toCreateKid["existed"] = false;
				toCreateKid["changed"] = true;
				toCreateKid["showInput"] = true;
				toCreateKid["updateLabel"] = $scope.updateLabel;
				toCreateKid["userId"] = $scope.userId;
				$scope.kids.push(toCreateKid);
				/*
				 * angular.forEach($scope.kids, function(kid, index){ })
				 */
				// create all existed == false
				// update all changed == true
			}

		} ]);