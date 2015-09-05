zmxk.controller('TeamCtrl', [
		'$scope',
		'userService',
		'kidService',
		'eventService',
		'eventTypeService',
		'serieService',
		'teamService',
		'$interval',
		'$timeout',
		'$routeParams',
		'$q',
		function($scope, userService, kidService, eventService,
				eventTypeService, serieService, teamService, $interval,
				$timeout, $routeParams, $q) {
			$scope.teams = [];
			var init = function() {
				teamService.listAll().then(function(data) {
					$scope.teams = data;
				});
			};
			init();
			$scope.createNewTeam = function() {
				var newTeam = {
					size : 10,
					showInput : true
				};
				$scope.teams.push(newTeam);
			}

			$scope.updateTeam = function(teamIndex, team) {
				var updateTeam;
				$scope.newTeams = [];
				angular.forEach($scope.teams, function(team, index) {
					if (index == teamIndex) {
						updateTeam = angular.copy(team);
						updateTeam.showInput = true;
						$scope.newTeams.push(updateTeam);
					} else {
						$scope.newTeams.push(angular.copy(team));
					}
				});

				$scope.teams = $scope.newTeams;
			}

			$scope.deleteTeam = function(teamIndex, team) {
				if (team.id) {
					if (confirm("确定要删除吗？")) {
						teamService.remove(team.id).then(
								function(data) {
									var updateTeam;
									$scope.newTeams = [];
									angular.forEach($scope.teams, function(
											team, index) {
										if (index != teamIndex) {
											updateTeam = angular.copy(team);
											$scope.newTeams.push(updateTeam);
										}
									});
									$scope.teams = $scope.newTeams;
								}, function(data) {
									alert(data.data.message);
									return;
								});
					}
				} else {
					var updateTeam;
					$scope.newTeams = [];
					angular.forEach($scope.teams, function(team, index) {
						if (index != teamIndex) {
							updateTeam = angular.copy(team);
							$scope.newTeams.push(updateTeam);
						}
					});
					$scope.teams = $scope.newTeams;
				}
			}

			$scope.saveTeam = function(teamIndex, team) {
				if (team.id) {
					teamService.update(team).then(function(data) {
						var updateTeam;
						$scope.newTeams = [];
						angular.forEach($scope.teams, function(team, index) {
							if (index == teamIndex) {
								updateTeam = angular.copy(data);
								$scope.newTeams.push(updateTeam);
							} else {
								$scope.newTeams.push(angular.copy(team));
							}
						});

						updateTeam.showInput = false;
						$scope.teams = $scope.newTeams;
					}, function(data) {
						alert(data.data.message);
						return;
					});
				} else {
					teamService.create(team).then(function(data) {
						var updateTeam;
						$scope.newTeams = [];
						angular.forEach($scope.teams, function(team, index) {
							if (index == teamIndex) {
								updateTeam = angular.copy(data);
								$scope.newTeams.push(updateTeam);
							} else {
								$scope.newTeams.push(angular.copy(team));
							}
						});

						updateTeam.showInput = false;
						$scope.teams = $scope.newTeams;
					}, function(data) {
						alert(data.data.message);
						return;
					});
				}
			};
		} ]);
zmxk.controller('TeamMemberCtrl', [
		'$scope',
		'userService',
		'kidService',
		'eventService',
		'eventTypeService',
		'serieService',
		'teamService',
		'$interval',
		'$timeout',
		'$routeParams',
		'$dialogs',
		'$q',
		function($scope, userService, kidService, eventService,
				eventTypeService, serieService, teamService, $interval,
				$timeout, $routeParams, $dialogs, $q) {
			$scope.teamId = $routeParams.teamId;
			$scope.team = {};
			$scope.teamMembers = [];
			var init = function() {
				teamService.get($scope.teamId).then(function(data) {
					$scope.team = data;
					teamService.listMembers($scope.teamId).then(function(data) {
						$scope.teamMembers = data;
					}, function(error) {
						alert(error.data.message);
					});
				});
			};
			init();
			$scope.removeTeamMember = function(teamMemberIndex, teamMember) {
				if(confirm("确定要删除该成员吗？")){
					kidService.leaveTeam(teamMember.id, teamMember.teamId).then(function(data) {
						var newKids = [];
						angular.forEach($scope.teamMembers, function(kid,index) {
							if (index == teamMemberIndex) {
							} else {
								newKids.push(angular.copy(kid));
							}
						});
						$scope.teamMembers = newKids;
					}, function(error) {
						alert(error.data.message);
					});
				}
			};
			$scope.addNewMember = function() {
				dlg = $dialogs.create('/admin/select_kid.html', 'FindKidCtrl',
						{
							teamId : $scope.teamId,
							userService : userService,
							kidService : kidService,
							teamMembers : $scope.teamMembers
						}, 'lg');
				dlg.result.then(function(kids) {
					angular.forEach(kids, function(kid, index) {
						if (kid.selected) {
							kid.teamId = $scope.teamId;
							$scope.teamMembers.push(kid);
						}
					});
				}, function() {
				});
			};
		} ]);
zmxk.controller('FindKidCtrl', function($scope, $modalInstance, data) {
	$scope.found_user = [];
	$scope.enroll_form_data = {};
	var teamId = data.teamId;
	var userService = data.userService;
	var kidService = data.kidService;
	var teamMembers = data.teamMembers;

	$scope.init = function() {
	}

	$scope.searchUser = function() {
		userService.search($scope.enroll_form_data.userQueryString).then(
				function(users) {
					if (users.length > 1 || users.length == 0) {
						alert("搜索到" + users.length + "个用户，请到用户页面检查用户");
					} else {
						$scope.found_user = users[0];
					}
				}, function(error) {
					alert(error.data.message);
				});
	}

	// -- Methods --//

	$scope.cancel = function() {
		$modalInstance.dismiss('Canceled');
	}; // end cancel

	$scope.save = function() {
		angular.forEach($scope.found_user.kids, function(kid, index) {
			if (kid.selected) {
				kidService.joinTeam(kid.id, teamId).then(function(data) {
				}, function(error) {
					alert(error.data.message);
				});
			}
		});
		$modalInstance.close($scope.found_user.kids);
	};
});