zmxk.controller('UserController', [
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
			var init = function() {
				// FIXME FAKE data
				for (var i = 0; i < 10; i++) {
					var user = {
						id : "mac-test-user-id-" + i,
						name : "Mac Test User " + i,
						roleId : i % 3,
						mobileNum : "13706516651",
						credit : 100 * i,
						createdTime : 1436339694000,
						existed : true,
						changed : false,
						showInput : false
					}
					$scope.userList.push(angular.copy(user));
				}
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

			init();
		} ]);