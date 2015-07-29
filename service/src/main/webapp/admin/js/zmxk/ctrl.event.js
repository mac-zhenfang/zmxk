//Serie Management

zmxk.controller('SerieManageCtrl', [
		'$scope',
		'userService',
		'eventService',
		'eventTypeService',
		'siteService',
		'serieService',
		'$interval',
		'$timeout',
		'$routeParams',
		function($scope, userService, eventService, eventTypeService,
				siteService, serieService, $interval, $timeout, $routeParams) {
			$scope.eventTypeId = $routeParams.eventTypeId;
			$scope.eventSeries = [];
			$scope.eventTypes = [];
			$scope.init = function() {
				serieService.list($scope.eventTypeId).then(function(data) {
					angular.forEach(data, function(serie, index) {
						$scope.eventSeries.push(serie);
						serie["existed"] = true;
						serie["changed"] = false;
						serie["showInput"] = false;
					})
				});

				eventTypeService.list().then(function(data) {
					$scope.eventTypes = data;
				}, function(data) {

				});
			}

			$scope.updateSerie = function(giveUpdateSerie, idx) {
				var assinGiveUpdateSerie = function(data) {
					giveUpdateSerie['id'] = data.id;
					// console.log("returned");
					// giveUpdateEventType["id"] = data.id;
					// giveUpdateEventType["idx"] = idx;

					if (giveUpdateSerie.existed) {
						giveUpdateSerie.changed = !giveUpdateSerie.changed;
					} else {
						giveUpdateSerie.existed = !giveUpdateSerie.existed;
					}

					giveUpdateSerie.showInput = !giveUpdateSerie.showInput;

					// console.log(giveUpdateEventType);

					// $scope.kids = kid; $scope.listEvents
					var eventSeries = [];
					// console.log($scope.listEventTypes);
					angular.forEach($scope.eventSeries, function(serie, i) {
						// console.log(eventType);
						if (i == idx) {
							console.log(i + "~" + idx);
							eventSeries.push(giveUpdateSerie);
						} else {
							eventSeries.push(angular.copy(serie));
						}
					});
					$scope.eventSeries = angular.copy(eventSeries);
					// console.log($scope.listEventTypes);

				}
				// console.log(giveUpdateEventType);
				// $scope.giveUpdateEventType =
				// giveUpdateEventType;
				// save event
				if (!giveUpdateSerie.existed) {
					serieService.create(giveUpdateSerie.eventTypeId,
							giveUpdateSerie).then(function(data) {
						assinGiveUpdateSerie(data);
					}, function(data) {

					})
				} else if (giveUpdateSerie.changed) {
					serieService.update(giveUpdateSerie.eventTypeId,
							giveUpdateSerie.id, giveUpdateSerie).then(
							function(data) {
								assinGiveUpdateSerie(data);
							},
							function(data) {
								$scope.launch("error", "", error.data.message,
										function() {

										}, function() {
										});
							});
				} else {
					// console.log("~~~~~");
					assinGiveUpdateSerie(giveUpdateSerie);
				}

			}

			$scope.deleteSerie = function(toDeleteSerie, idx) {

				var handleReturn = function(data) {
					var eventSeries = [];
					angular.forEach($scope.eventSeries, function(serie, i) {
						if (idx != i) {
							eventSeries.push(angular.copy(serie));
						}
					});
					$scope.eventSeries = eventSeries;
					console.log($scope.eventSeries)
				}

				$scope.toDeleteSerie = toDeleteSerie;
				console.log(toDeleteSerie);

				if (!toDeleteSerie.existed) {
					handleReturn(null);
					// $scope.$digest();
				} else {
					serieService.remove(toDeleteSerie.eventTypeId,
							toDeleteSerie.id).then(
							function(data) {
								handleReturn(null);
							},
							function(error) {
								$scope.launch("error", "", error.data.message,
										function() {

										}, function() {
										});
							})
				}

			}

			$scope.createSerie = function() {

				var toCreateSerie = {

				}
				toCreateSerie["existed"] = false;
				toCreateSerie["changed"] = true;
				toCreateSerie["showInput"] = true;
				$scope.eventSeries.push(toCreateSerie);
				/*
				 * angular.forEach($scope.kids, function(kid, index){ })
				 */
				// create all existed == false
				// update all changed == true
			}
		} ]);
// Event Typs Start
zmxk
		.controller(
				'EventTypeManageCtrl',
				[
						'$scope',
						'userService',
						'eventService',
						'eventTypeService',
						'siteService',
						'serieService',
						'$interval',
						'$timeout',
						'$routeParams',
						function($scope, userService, eventService,
								eventTypeService, siteService, serieService,
								$interval, $timeout, $routeParams) {

							$scope.sites = [ {
								id : null,
								name : "公共"
							} ];
							$scope.giveUpdateEventType = {};
							$scope.toDeleteEventType = {};
							$scope.init = function() {
								eventTypeService
										.list()
										.then(
												function(data) {
													$scope.listEventTypes = data;
													angular
															.forEach(
																	$scope.listEventTypes,
																	function(
																			eventType,
																			index) {
																		eventType["existed"] = true;
																		eventType["changed"] = false;
																		eventType["showInput"] = false;
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

							

							$scope.updateEventType = function(
									giveUpdateEventType, idx) {
								var assinGiveUpdateEventType = function(data) {
									giveUpdateEventType['id'] = data.id;
									// console.log("returned");
									// giveUpdateEventType["id"] = data.id;
									// giveUpdateEventType["idx"] = idx;

									if (giveUpdateEventType.existed) {
										giveUpdateEventType.changed = !giveUpdateEventType.changed;
									} else {
										giveUpdateEventType.existed = !giveUpdateEventType.existed;
									}

									giveUpdateEventType.showInput = !giveUpdateEventType.showInput;

									// console.log(giveUpdateEventType);

									// $scope.kids = kid; $scope.listEvents
									var eventTypes = [];
									// console.log($scope.listEventTypes);
									angular
											.forEach(
													$scope.listEventTypes,
													function(eventType, i) {
														// console.log(eventType);
														if (i == idx) {
															console.log(i + "~"
																	+ idx);
															eventTypes
																	.push(giveUpdateEventType);
														} else {
															eventTypes
																	.push(angular
																			.copy(eventType));
														}
													});
									$scope.listEventTypes = angular
											.copy(eventTypes);
									// console.log($scope.listEventTypes);

								}
								// console.log(giveUpdateEventType);
								// $scope.giveUpdateEventType =
								// giveUpdateEventType;
								// save event
								if (!giveUpdateEventType.existed) {
									eventTypeService.createEventType(
											giveUpdateEventType).then(
											function(data) {
												assinGiveUpdateEventType(data);
											}, function(data) {

											})
								} else if (giveUpdateEventType.changed) {
									eventTypeService.updateEventType(
											giveUpdateEventType.id,
											giveUpdateEventType).then(
											function(data) {
												assinGiveUpdateEventType(data);
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
									assinGiveUpdateEventType(giveUpdateEventType);
								}

							}

							$scope.deleteEventType = function(
									toDeleteEventType, idx) {
								var handleReturn = function(data) {
									var eventTypes = [];
									angular.forEach($scope.listEventTypes,
											function(eventType, i) {
												if (idx != i) {
													eventTypes.push(angular
															.copy(eventType));
												}
											});
									$scope.listEventTypes = eventTypes;
									console.log($scope.listEventTypes)
								}
								$scope.toDeleteEventType = toDeleteEventType;
								console.log(toDeleteEventType);

								if (!toDeleteEventType.existed) {
									handleReturn(null);
									// $scope.$digest();
								} else {
									eventTypeService.deleteEventType(
											toDeleteEventType.id).then(
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

							$scope.createEventType = function() {

								var toCreateEventType = {

								}
								toCreateEventType["existed"] = false;
								toCreateEventType["changed"] = true;
								toCreateEventType["showInput"] = true;
								$scope.listEventTypes.push(toCreateEventType);
								/*
								 * angular.forEach($scope.kids, function(kid,
								 * index){ })
								 */
								// create all existed == false
								// update all changed == true
							}

						} ]);

// Event Start
zmxk.controller('EventManageCtrl', [
		'$scope',
		'userService',
		'eventService',
		'eventTypeService',
		'siteService',
		'serieService',
		'$interval',
		'$timeout',
		'$routeParams',
		function($scope, userService, eventService, eventTypeService,
				siteService, serieService, $interval, $timeout, $routeParams) {

			$scope.viewEventManagePage = function() {
				if (angular.isUndefined($scope.eventId)) {
					return "event_manage_show_list.html";
				} else {
					return "event_manage_show_detail.html";
				}
			}

			$scope.callToEvent = function(eventId) {
				$scope.hoopPage("events.html", {
					"eventId" : eventId
				})
			}

			$scope.listEvents = [];
			var dateNames = {};
			$scope.$on('setDate1', function(e, date, idx) {
				// console.log(idx);
				if (!angular.isUndefined(idx)) {
					$scope.formEventName(idx);
				}
			});
			$scope.sites = [];
			$scope.init = function() {
				//console.log($scope.loginUser);
				if(!$scope.isAdmin()) {
					if(angular.isUndefined($scope.loginUser.siteId)) {
						window.location.href="err403.html";
					}
					var loginUserSiteId = $scope.loginUser.siteId;
				}
				
				eventService.list(loginUserSiteId).then(
						function(data) {
							$scope.listEvents = data;
							var listEvents = data;
							angular.forEach(listEvents, function(event, index) {

								var enrolledCount = 0;
								var applyScoreCount = 0;
								var leftCount = 0;
								// $scope.formEventName(index);
								angular.forEach(event.attendees, function(
										attendee, index2) {
									var attStatus = attendee.status;
									// console.log(attStatus);
									if (attStatus == 1) {
										enrolledCount += 1;
									} else if (attStatus == 2) {
										applyScoreCount += 1;
									}
								});
								event.leftCount = event.quota - enrolledCount
										- applyScoreCount;
								event.applyScoreCount = applyScoreCount;
								event.enrolledCount = enrolledCount;
								event["existed"] = true;
								event["changed"] = false;
								event["showInput"] = false;

								event.eventSeries = [ {
									id : null,
									name : "单次比赛"
								} ];
								serieService.list(event.eventTypeId).then(
										function(data) {
											angular.forEach(data, function(
													serie, index) {
												event.eventSeries.push(serie);
												/*
												 * console.log("<<<<<<<<<<<<<<<")
												 * console.log(serie.id)
												 * console.log(serie.name)
												 * console.log(event.seriesId)
												 * console.log(">>>>>>>>>>>>>>>")
												 */
												// $scope.listEvents.push(event);
											})
										})
							});
						}, function(data) {
						});

				// get EventTypes
				// FIXME eventTypes
				if($scope.isAdmin()) {
					eventTypeService.list().then(function(data) {
						$scope.eventTypes = data;
					}, function(data) {

					})
					
				} else {
					console.log(loginUserSiteId);
					eventTypeService.list(loginUserSiteId).then(function(data) {
						$scope.eventTypes = data;
					}, function(data) {
						
					})
				}
				
				
				
				if($scope.isAdmin()) {
					siteService.list().then(function(data) {
						$scope.sites = data;
					}, function(data) {
					})
				} else if ((!angular.isUndefined(loginUserSiteId) && loginUserSiteId!=null)){
					siteService.get(loginUserSiteId).then(function(data) {
						$scope.sites.push(data);
						console.log($scope.sites);
					}, function(data) {
					})
				} else {
					//FIXME error
					$scope.launch("error", "", "无法取得site信息，请重新登录或联系管理员",
							function() {

							}, function() {
							});
				}
			}

			$scope.changeName = function(idx) {
				var selectEvent = undefined;
				angular.forEach($scope.listEvents, function(event, index2) {
					if (index2 == idx) {
						selectEvent = event;
						console.log(selectEvent);
						return;
					}
				});

				if (angular.isUndefined(selectEvent)) {
					return;
				}

				var name = selectEvent.name;
				var nameArr = name.split(" ");
				var prefix = nameArr[0];
				var num = $scope.nameStageMap[prefix];
				selectEvent.stage = num;
			}

			$scope.formEventName = function(idx) {
				var selectEvent = undefined;
				angular.forEach($scope.listEvents, function(event, index2) {
					if (index2 == idx) {
						selectEvent = event;
						console.log(selectEvent);
						return;
					}
				});

				if (angular.isUndefined(selectEvent)) {
					return;
				}

				var namePrefix = $scope.stageNameMap[selectEvent.stage];
				console.log(selectEvent);
				var name;
				if (angular.isUndefined(selectEvent.eventTime)) {
					name = namePrefix;
				} else {
					var date = new Date(selectEvent.eventTime);
					var mon = date.getMonth() + 1;
					mon = mon < 10 ? "0" + mon : mon;
					var day = date.getDate();
					day = day < 10 ? "0" + day : day;
					name = mon + "-" + day + "-" + date.getFullYear();
					name = namePrefix + " " + name;
				}
				if (!angular.isUndefined(selectEvent.name)) {
					if (selectEvent.name.search(name) < 0) {
						console.log(name);
						selectEvent.name = name;
					}
				} else {
					selectEvent.name = name;
				}
				/*
				 * if(angular.isUndefined(dateNames[name])) { dateNames[name] =
				 * 1; } else { dateNames[name]+=1; } var num = dateNames[name];
				 * if(num < 10) { num = "0"+num; } name = name + " " + num;
				 */

			}
			$scope.initSeries = function(event) {
				event.eventSeries = [ {
					id : null,
					name : "单次比赛"
				} ];
				serieService.list(event.eventTypeId).then(function(data) {
					angular.forEach(data, function(serie, index) {
						event.eventSeries.push(serie);
					})

				})
				// event.eventSeries.push({id : null, name : "单次比赛"})
				console.log(event.eventSeries);

			}

			$scope.updateEvent = function(updateEvent, idx) {
				var handleReturn = function(data) {

					if (updateEvent.existed) {
						updateEvent.changed = !updateEvent.changed;
					}

					updateEvent.showInput = !updateEvent.showInput;
					// $scope.kids = kid; $scope.listEvents
					var events = [];
					angular.forEach($scope.listEvents, function(event, i) {

						if (i == idx) {
							console.log(i + "~~~" + idx);
							events.push(updateEvent);
						} else {
							events.push(event);
						}
					})
					$scope.listEvents = angular.copy(events);
				}
				// save event
				if (!updateEvent.existed) {
					eventService.createEvent(updateEvent).then(function(data) {
						// updateEvent = data;
						// data.existed = true;
						updateEvent["id"] = data.id;
						updateEvent.existed = true;
						handleReturn(updateEvent);
					}, function(data) {

					})
				} else if (updateEvent.changed) {
					eventService.updateEvent(updateEvent.id, updateEvent).then(
							function(data) {
								handleReturn(data);

							},
							function(data) {
								$scope.launch("error", "", error.data.message,
										function() {

										}, function() {
										});
							});
				} else {
					handleReturn(updateEvent);
				}

			}

			$scope.deleteEvent = function(deleteEvent, idx) {
				console.log(deleteEvent);
				var handleReturn = function() {
					var events = [];
					angular.forEach($scope.listEvents, function(event, i) {
						if (idx != i) {
							events.push(angular.copy(event));
						}
					});
					$scope.listEvents = angular.copy(events);
					console.log($scope.listEvents)
				}
				if (!deleteEvent.existed) {
					deleteEvent = null;
					handleReturn();
				} else {
					eventService.deleteEvent(deleteEvent.id).then(
							function(data) {
								deleteEvent = null;
								handleReturn();
							},
							function(data) {
								$scope.launch("error", "", error.data.message,
										function() {

										}, function() {
										});
							})
				}

			}

			$scope.createEvent = function() {

				var toCreateEvent = {

				}
				toCreateEvent["existed"] = false;
				toCreateEvent["changed"] = true;
				toCreateEvent["showInput"] = true;
				$scope.listEvents.push(toCreateEvent);
				/*
				 * angular.forEach($scope.kids, function(kid, index){ })
				 */
				// create all existed == false
				// update all changed == true
			}

		} ]);

zmxk.controller('EventCreateCtrl', [
		'$scope',
		'userService',
		'eventService',
		'$interval',
		'$timeout',
		'$routeParams',
		'$dialogs',
		function($scope, userService, eventService, $interval, $timeout,
				$routeParams, $dialogs) {
			$scope.totalSteps = 3;
			$scope.selectStep = function(step) {

				if ($scope.currentStep > 0
						&& $scope.currentStep + step <= $scope.totalSteps) {
					$scope.currentStep = $scope.currentStep + step;
				}
				// change label
				if ($scope.currentStep == $scope.totalSteps) {
					$scope.next_label = "提交";
				} else {
					$scope.next_label = "下一步";
				}
			}
		} ]);
zmxk
		.controller(
				'EventDetailCtrl',
				[
						'$scope',
						'userService',
						'eventService',
						'$interval',
						'$timeout',
						'$routeParams',
						'$dialogs',
						'tagService',
						function($scope, userService, eventService, $interval,
								$timeout, $routeParams, $dialogs, tagService) {
							$scope.eventId = $routeParams.eventId;
							// console.log("~~~~~" + $scope.eventId);
							$scope.eventInit = {};
							$scope.selectedRule
							$scope.applyCreditRuleAttendeeList = [];
							$scope.eventInit.attendees = [];
							$scope.eventTags = [];
							var tagTypeClasss = [ "btn btn-info",
									"btn btn-success", "btn btn-danger",
									"btn btn-active" ]
							$scope.init = function() {

								if (!angular.isUndefined($scope.eventId)) {
									eventService
											.getEvent($scope.eventId)
											.then(
													function(data) {
														$scope.eventInit = data;

														var enrolledCount = 0;
														var applyScoreCount = 0;
														var leftCount = 0;
														var tagsMap = {};
														var newAttendees = [];
														angular
																.forEach(
																		$scope.eventInit.attendees,
																		function(
																				attendee,
																				index2) {
																			if (angular
																					.isUndefined(attendee.tagId)
																					|| !attendee.tagId) {
																				if (angular
																						.isUndefined(tagsMap["fake"])) {
																					tagsMap["fake"] = [];
																				}
																				tagsMap["fake"]
																						.push(attendee);
																			} else {
																				if (angular
																						.isUndefined(tagsMap[attendee.tagId])) {
																					tagsMap[attendee.tagId] = [];
																				}
																				tagsMap[attendee.tagId]
																						.push(attendee);
																			}
																		});
														var d = 0;
														angular
																.forEach(
																		tagsMap,
																		function(
																				attendeeList,
																				index) {
																			d++;
																			angular
																					.forEach(
																							attendeeList,
																							function(
																									attendee,
																									index2) {
																								attendee["tagType"] = d;
																								newAttendees
																										.push(attendee);
																							});
																		});

														$scope.eventInit.attendees = angular
																.copy(newAttendees);

														console
																.log($scope.eventInit.attendees);

														$scope.eventInit.leftCount = event.quota
																- enrolledCount
																- applyScoreCount;
														$scope.eventInit.applyScoreCount = applyScoreCount;
														$scope.eventInit.enrolledCount = enrolledCount;

													}, function(data) {
														console.log(data);
													});
								}

								// get tags
								tagService.search("event").then(function(data) {
									$scope.eventTags = data;
								}, function(data) {

								});
							}

							$scope.showTagClass = function(tagType) {
								return tagTypeClasss[tagType
										% tagTypeClasss.length];
							}
							$scope.canGiveCredit = function(attendee) {
								return attendee.existed == false
										|| attendee.status < 2;
							}
							$scope.giveCredit = function(attendee) {
								var selectedAttendee = attendee;
								/*
								 * angular.forEach($scope.eventInit.attendees,
								 * function(attendee, index) { if (attendee.id ==
								 * attendeeId) { selectedAttendee = attendee;
								 * return; } });
								 */
								// console.log(selectedAttendee);
								if (selectedAttendee.status == 2) {
									dlg = $dialogs
											.create(
													'/admin/give_event_credit.html',
													'GiveEventCreditCtrl',
													{
														eventTypeId : $scope.eventInit.eventTypeId,
														serieId : $scope.eventInit.serieId,
														attendee : selectedAttendee
													}, 'lg');
									dlg.result.then(function(selectedRule) {
										$scope.selectedRule = selectedRule;
										$scope.applyCreditRuleAttendeeList
												.push({
													rule : selectedRule,
													attendee : selectedAttendee
												});
										console.log(selectedRule)
									}, function() {
										// $scope.name = 'You decided not to
										// enter in your name, that makes me
										// sad.';
									});
								} else {
									$scope.launch("error", "",
											"选手没有完成比赛，无法授予积分", function() {

											}, function() {
											});
								}
							}
							$scope.editScore = function() {
								var m = new Map();
								angular.forEach($scope.eventInit.attendees,
										function(attendee, index) {
											if (!m.get(attendee.tagId)) {
												m.set(attendee.tagId, []);
											}
											m.get(attendee.tagId)
													.push(attendee);
										});
								m.forEach(function(attendees, tagId) {
									attendees.sort(function(a, b) {
										var key = "score";
										var x = a[key];
										var y = b[key];
										return ((x < y) ? -1
												: ((x > y) ? 1 : 0));
									});
									attendees
											.forEach(function(attendee, index) {
												attendee.rank = index + 1;
											})
								});
								console.log($scope.eventInit.attendees);
							}

							$scope.applyAttendeeChanges = function() {
								var error_msg = "";
								var error = false;
								console.log($scope.applyCreditRuleAttendeeList);
								/*
								 * angular .forEach( $scope.eventInit.attendees,
								 * function(attendee, index) { if
								 * ((attendee.rank == 0 && attendee.score >= 0) ||
								 * (attendee.score == 0 && attendee.rank >= 0)) {
								 * error_msg = "请核对选手的成绩与排名"; error = true;
								 * return; } })
								 */
								if (error) {
									$scope.launch("error", "", error_msg,
											function() {

											}, function() {
											});
								} else {
									// going to save data
									$scope
											.launch(
													"confirm",
													"确定保存",
													"确保您已经检查选手成绩，点击确定保存",
													function() {
														eventService
																.saveAttendee(
																		$scope.eventInit.id,
																		$scope.eventInit.attendees)
																.then(
																		function(
																				data) {
																			$scope.editingTag = false;
																			$scope
																					.launch(
																							"notify",
																							"成绩修改成功",
																							"成绩修改成功",
																							function() {

																							},
																							function() {
																							});
																		},
																		function(
																				error) {
																			$scope
																					.launch(
																							"error",
																							"",
																							error.data.message,
																							function() {

																							},
																							function() {
																							});
																		})
													}, function() {
													});
								}
								console.log($scope.eventInit.attendees);
							}

						} ]);

zmxk.controller('GiveEventCreditCtrl', function($scope, $modalInstance, data) {

	// -- Variables --//
	$scope.data = data;
	$scope.selectRule = {};
	// FAKE DATA
	$scope.eventRules = [ {
		id : "mac-test-event-rule-1",
		name : "脚踏拉力赛规则",
		eventType : "脚踏拉力赛",
		serieName : "预赛",
		rank : 1,
		rankName : "1",
		credit : 100,
		existed : true,
		changed : false,
		showInput : false
	}, {
		id : "mac-test-event-rule-2",
		name : "脚踏拉力赛规则",
		eventType : "脚踏拉力赛",
		serieName : "预赛",
		rank : 2,
		rankName : "2",
		credit : 50,
		existed : true,
		changed : false,
		showInput : false
	}, {
		id : "mac-test-event-rule-3",
		name : "脚踏拉力赛规则",
		eventType : "脚踏拉力赛",
		serieName : "预赛",
		rank : 0,
		rankName : ">3",
		credit : 10,
		existed : true,
		changed : false,
		showInput : false
	}, {
		id : "mac-test-event-rule-4",
		name : "脚踏拉力赛规则",
		eventType : "脚踏拉力赛",
		serieName : "季度复赛",
		rank : 1,
		rankName : "1",
		credit : 400,
		existed : true,
		changed : false,
		showInput : false
	}, {
		id : "mac-test-event-rule-5",
		name : "脚踏拉力赛规则",
		eventType : "脚踏拉力赛",
		serieName : "季度复赛",
		rank : 2,
		rankName : "2",
		credit : 200,
		existed : true,
		changed : false,
		showInput : false
	}, {
		id : "mac-test-event-rule-6",
		name : "脚踏拉力赛规则",
		eventType : "脚踏拉力赛",
		serieName : "季度复赛",
		rank : 0,
		rankName : ">3",
		credit : 200,
		existed : true,
		changed : false,
		showInput : false
	}, {
		id : "mac-test-event-rule-7",
		name : "最有活力小选手",
		eventType : "脚踏拉力赛",
		serieName : "季度复赛",
		rankName : "单次事件",
		credit : 300,
		existed : true,
		changed : false,
		showInput : false
	}, {
		id : "mac-test-event-rule-8",
		name : "单圈最快",
		eventType : "脚踏拉力赛",
		serieName : "季度复赛",
		rankName : "单次事件",
		credit : 200,
		existed : true,
		changed : false,
		showInput : false
	} ]

	$scope.init = function() {
		var newRules = [];
		angular.forEach($scope.eventRules, function(rule, index) {
			if (angular.isUndefined(rule.rank)) {
				newRules.push(angular.copy(rule));
			}
		});
		$scope.eventRules = newRules;
		console.log($scope.eventRules);
	}

	// -- Methods --//

	$scope.cancel = function() {
		$modalInstance.dismiss('Canceled');
	}; // end cancel

	$scope.save = function(selectRule) {
		// console.log(selectRule);
		$modalInstance.close(selectRule);
	}; // end save

	/*
	 * $scope.hitEnter = function(evt) { if (angular.equals(evt.keyCode, 13) &&
	 * !(angular.equals($scope.user.name, null) || angular.equals(
	 * $scope.user.name, ''))) $scope.save(); };
	 */

})