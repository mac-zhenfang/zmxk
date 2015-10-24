zmxk.controller('MainController', [
		'$scope',
		'$location',
		'userService',
		'$window',
		'$dialogs',
		'$routeParams',
		'$cookies',
		function($scope, $location, userService, $window, $dialogs,
				$routeParams, $cookies) {
			currentModule = "userList";
			$scope.$location = $location;
			$scope.onExit = function() {
				return ('请在离开页面前确保修改已经保存');
			};
			$window.onbeforeunload =  $scope.onExit;
			$scope.userId = $scope.$location.search().userId;
			$scope.eventId = $scope.$location.search().eventId;
			$scope.stages = [ {
				id : 0,
				name : "N/A"
			} ];
			/*
			 * $scope.stages = [ { value : 0, label : "N/A" }, { value : 1,
			 * label : "预赛" }, { value : 2, label : "季度赛" }, { value : 3, label :
			 * "年度赛" } ];
			 */

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

			$scope.nameStageMap = {
				"A" : 1,
				"B" : 2,
				"C" : 3
			}

			$scope.kids_school_options = [ {
				value : 0,
				label : "幼儿园"
			}, {
				value : 1,
				label : "小学"
			}, {
				value : 2,
				label : "未上幼儿园"
			} ]

			$scope.loginUser = angular.fromJson($cookies.get("loginUser"));
			// console.log($scope.loginUser);
			$scope.init = function() {

				// console.log($scope.loginUser);
				userService.getUser($scope.loginUser.id).then(function(data) {

				}, function(data) {
				});
				if (!angular.isUndefined($scope.$location.search().page)) {
					// console.log($scope.$location);
					currentModule = $scope.$location.search().page;
				}
			}

			$scope.isAdmin = function() {
				return !angular.isUndefined($scope.loginUser)
						&& !angular.isUndefined($scope.loginUser.roleId)
						&& $scope.loginUser.roleId == "2";
			}

			$scope.hoopPage = function(page, params) {
				var givenParams = "";

				angular.forEach(params, function(value) {
					givenParams += value;
					givenParams += "/";
				});
				/*
				 * angular.forEach(params, function(value, key) { givenParams +=
				 * key; givenParams += "="; givenParams += value; givenParams +=
				 * "&"; });
				 */
				givenParams = givenParams.substring(0, givenParams.length - 1);
				var uri = $scope.$location.absUrl();
				var url = $scope.$location.url();

				uri = uri.substring(0, uri.indexOf(url));
				console.log(uri);

				uri = uri + "/";
				uri = uri + page + "/" + givenParams;
				console.log(" jump " + uri);
				window.location.href = uri;
				// window.location.reload();
			}

			$scope.launch = function(which, title, message, yesFun, noFun) {
				var dlg = null;
				switch (which) {

				// Error Dialog
				case 'error':
					dlg = $dialogs.error(message);
					break;

				/*
				 * case 'wait': dlg = $dialogs.wait(msgs[i++], progress);
				 * fakeProgress(); break;
				 */
				// Notify Dialog
				case 'notify':
					dlg = $dialogs.notify(title, message, yesFun);
					break;

				// Confirm Dialog
				case 'confirm':
					dlg = $dialogs.confirm(title, message);
					dlg.result.then(yesFun, noFun);
					break;
				}
				; // end switch
			}; // end launch

			$scope.formatDate = function(time) {
				var date = new Date(time);
				var hours = date.getHours();
				var minutes = date.getMinutes();
				var ampm = hours >= 12 ? 'pm' : 'am';
				hours = hours % 12;
				hours = hours ? hours : 12; // the hour '0'
				// should be '12'
				minutes = minutes < 10 ? '0' + minutes : minutes;
				var strTime = hours + ':' + minutes + ' ' + ampm;
				return date.getMonth() + 1 + "/" + date.getDate() + "/"
						+ date.getFullYear() + "  " + strTime;
			}
			// $scope.init();
		} ]);
// MainController.$inject = ['$scope', 'userService'];

// Main end
