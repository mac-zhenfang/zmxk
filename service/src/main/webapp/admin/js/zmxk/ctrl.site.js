// Event Typs Start
zmxk
		.controller(
				'SiteManageCtrl',
				[
						'$scope',
						'userService',
						'eventService',
						'siteService',
						'siteService',
						'serieService',
						'$interval',
						'$timeout',
						'$routeParams',
						function($scope, userService, eventService,
								siteService, siteService, serieService,
								$interval, $timeout, $routeParams) {

							$scope.sites = [ {
								id : null,
								name : "公共"
							} ];
							$scope.giveUpdateSite = {};
							$scope.toDeleteSite = {};
							$scope.init = function() {
								siteService
										.list()
										.then(
												function(data) {
													$scope.listSites = data;
													angular
															.forEach(
																	$scope.listSites,
																	function(
																			site,
																			index) {
																		site["existed"] = true;
																		site["changed"] = false;
																		site["showInput"] = false;
																	});
												}, function(data) {
												});

								siteService.list().then(
										function(data) {
											angular.forEach(data, function(
													site, index) {
												$scope.sites.push(site);
											})

										}, function(data) {
										})

							}

							

							$scope.updateSite = function(
									giveUpdateSite, idx) {
								var assinGiveUpdateSite = function(data) {
									giveUpdateSite['id'] = data.id;
									// console.log("returned");
									// giveUpdateSite["id"] = data.id;
									// giveUpdateSite["idx"] = idx;

									if (giveUpdateSite.existed) {
										giveUpdateSite.changed = !giveUpdateSite.changed;
									} else {
										giveUpdateSite.existed = !giveUpdateSite.existed;
									}

									giveUpdateSite.showInput = !giveUpdateSite.showInput;

									// console.log(giveUpdateSite);

									// $scope.kids = kid; $scope.listEvents
									var sites = [];
									// console.log($scope.listSites);
									angular
											.forEach(
													$scope.listSites,
													function(site, i) {
														// console.log(site);
														if (i == idx) {
															console.log(i + "~"
																	+ idx);
															sites
																	.push(giveUpdateSite);
														} else {
															sites
																	.push(angular
																			.copy(site));
														}
													});
									$scope.listSites = angular
											.copy(sites);
									// console.log($scope.listSites);

								}
								// console.log(giveUpdateSite);
								// $scope.giveUpdateSite =
								// giveUpdateSite;
								// save event
								if (!giveUpdateSite.existed) {
									siteService.create(
											giveUpdateSite).then(
											function(data) {
												assinGiveUpdateSite(data);
											}, function(data) {

											})
								} else if (giveUpdateSite.changed) {
									siteService.update(
											giveUpdateSite.id,
											giveUpdateSite).then(
											function(data) {
												assinGiveUpdateSite(data);
											},
											function(data) {
												$scope.launch("error", "",
														error.data.message,
														function() {

														}, function() {
														});
											});
								} else {
									// console.log("~~~~~");
									assinGiveUpdateSite(giveUpdateSite);
								}

							}

							$scope.deleteSite = function(
									toDeleteSite, idx) {
								var handleReturn = function(data) {
									var sites = [];
									angular.forEach($scope.listSites,
											function(site, i) {
												if (idx != i) {
													sites.push(angular
															.copy(site));
												}
											});
									$scope.listSites = sites;
									console.log($scope.listSites)
								}
								$scope.toDeleteSite = toDeleteSite;
								console.log(toDeleteSite);

								if (!toDeleteSite.existed) {
									handleReturn(null);
									// $scope.$digest();
								} else {
									siteService.remove(
											toDeleteSite.id).then(
											function(data) {
												handleReturn(null);
											},
											function(error) {
												$scope.launch("error", "",
														error.data.message,
														function() {

														}, function() {
														});
											})
								}

							}

							$scope.createSite = function() {

								var toCreateSite = {

								}
								toCreateSite["existed"] = false;
								toCreateSite["changed"] = true;
								toCreateSite["showInput"] = true;
								$scope.listSites.push(toCreateSite);
								/*
								 * angular.forEach($scope.kids, function(kid,
								 * index){ })
								 */
								// create all existed == false
								// update all changed == true
							}

						} ]);