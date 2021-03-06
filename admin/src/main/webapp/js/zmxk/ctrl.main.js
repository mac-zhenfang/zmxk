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

			$scope.userId = $scope.$location.search().userId;
			$scope.eventId = $scope.$location.search().eventId;
			$scope.stages = [ {
				value : 1,
				label : "预赛"
			}, {
				value : 2,
				label : "季度赛"
			}, {
				value : 3,
				label : "年度赛"
			}, {
				value : null,
				label : "------"
			} ];
			
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
			
			$scope.stageNameMap = {
					"1" : "A",
					"2" : "B",
					"3" : "C"
			}
			
			$scope.nameStageMap = {
					"A" : 1,
					"B" : 2,
					"C" : 3
			}
			
			

			// console.log($cookies.get("loginUser"));
			$scope.loginUser = angular.fromJson($cookies.get("loginUser"));
			// console.log($scope.loginUser);
			$scope.init = function() {
				
				if(angular.isUndefined($scope.loginUser)){
					window.location.href = "err403.html";
				}
				
				if (!angular.isUndefined($scope.$location.search().page)) {
					// console.log($scope.$location);
					currentModule = $scope.$location.search().page;
				}
			}
			
			$scope.isAdmin = function() {
				return !angular.isUndefined($scope.loginUser) && !angular.isUndefined($scope.loginUser.roleId) && $scope.loginUser.roleId==2;
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
