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
			$scope.siteId = null;
			$scope.eventSeriesCategories = [ {
				id : true,
				name : "团队赛"
			}, {
				id : false,
				name : "个人赛"
			} ]
			$scope.init = function() {
				$scope.siteId = $routeParams.siteId;
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
				toCreateSerie["eventTypeId"] = $scope.eventTypeId;
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

							$scope.sites = [];
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
										giveUpdateEventType.changed = !giveUpdateEventType.changed;
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
zmxk
		.controller(
				'EventManageCtrl',
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
							$scope.eventCategories = [ {
								id : true,
								name : "团队赛"
							}, {
								id : false,
								name : "个人赛"
							} ]
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
							$scope.seriesId = null;
							$scope.eventTypeId = null;
							$scope.siteId = null;
							var dateNames = {};
							$scope.$on('setDate1', function(e, date, idx) {
								// console.log(idx);
								if (!angular.isUndefined(idx)) {
									$scope.formEventName(idx);
								}
							});
							$scope.sites = [];

							$scope.eventTypes = [ {
								id : "",
								name : "N/A"
							} ]
							$scope.init = function() {
								var loginUserSiteId = $scope.loginUser.siteId;
								$scope.siteId = $routeParams.siteId;
								$scope.seriesId = $routeParams.seriesId;
								$scope.eventTypeId = $routeParams.eventTypeId;
								var criteria = {};
								if ($scope.seriesId) {
									criteria.seriesId = $scope.seriesId;
								}

								eventService
										.search(criteria)
										.then(
												function(data) {
													$scope.listEvents = data;
													var listEvents = data;
													angular
															.forEach(
																	listEvents,
																	function(
																			event,
																			index) {

																		var enrolledCount = 0;
																		var applyScoreCount = 0;
																		var leftCount = 0;
																		// $scope.formEventName(index);
																		angular
																				.forEach(
																						event.attendees,
																						function(
																								attendee,
																								index2) {
																							var attStatus = attendee.status;
																							// console.log(attStatus);
																							if (attStatus == 1) {
																								enrolledCount += 1;
																							} else if (attStatus == 2) {
																								applyScoreCount += 1;
																							}
																						});
																		event.leftCount = event.quota
																				- enrolledCount
																				- applyScoreCount;
																		event.applyScoreCount = applyScoreCount;
																		event.enrolledCount = enrolledCount;
																		event["existed"] = true;
																		event["changed"] = false;
																		event["showInput"] = false;

																		event.eventSeries = [ {
																			id : "",
																			name : "N/A"
																		} ];
																		serieService
																				.list(
																						event.eventTypeId)
																				.then(
																						function(
																								data) {
																							angular
																									.forEach(
																											data,
																											function(
																													serie,
																													index) {
																												event.eventSeries
																														.push(serie);
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
								if ($scope.isAdmin()) {
									eventTypeService.list().then(
											function(data) {
												angular.forEach(data, function(
														eachData) {
													$scope.eventTypes
															.push(eachData);
												})
											}, function(data) {

											})

								} else {
									eventTypeService
											.list(loginUserSiteId)
											.then(
													function(data) {
														angular
																.forEach(
																		data,
																		function(
																				eachData) {
																			$scope.eventTypes
																					.push(eachData);
																		})

													}, function(data) {

													})
								}

								if ($scope.isAdmin()) {
									siteService.list().then(function(data) {
										$scope.sites = data;
									}, function(data) {
									})
								} else if ((!angular
										.isUndefined(loginUserSiteId) && loginUserSiteId != null)) {
									siteService.get(loginUserSiteId).then(
											function(data) {
												$scope.sites.push(data);
											}, function(data) {
											})
								} else {
									// FIXME error
									$scope.launch("error", "",
											"无法取得site信息，请重新登录或联系管理员",
											function() {

											}, function() {
											});
								}
							}

							$scope.changeName = function(idx) {
								var selectEvent = undefined;
								angular.forEach($scope.listEvents, function(
										event, index2) {
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
								angular.forEach($scope.listEvents, function(
										event, index2) {
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
								if (angular.isUndefined(namePrefix)) {
									namePrefix = "";
								}
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
									name = mon + "-" + day + "-"
											+ date.getFullYear();
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
								 * if(angular.isUndefined(dateNames[name])) {
								 * dateNames[name] = 1; } else {
								 * dateNames[name]+=1; } var num =
								 * dateNames[name]; if(num < 10) { num =
								 * "0"+num; } name = name + " " + num;
								 */

							}
							$scope.initSeries = function(event) {
								event.eventSeries = [ {
									id : "",
									name : "N/A"
								} ];
								serieService.list(event.eventTypeId).then(
										function(data) {
											angular.forEach(data, function(
													serie, index) {
												event.eventSeries.push(serie);
											})

										})
								// event.eventSeries.push({id : null, name :
								// "单次比赛"})
								console.log(event.eventSeries);

							}

							$scope.isSingleEvent = function(updateEvent) {
								console.log(updateEvent);
								if ((angular.isUndefined(updateEvent.seriesId) || !updateEvent.seriesId)) {
									return true;
								} else {
									return false
								}
							}

							$scope.updateEvent = function(updateEvent, idx) {
								var handleReturn = function(data) {

									if (updateEvent.existed) {
										updateEvent.changed = !updateEvent.changed;
									}

									updateEvent.showInput = !updateEvent.showInput;
									// $scope.kids = kid; $scope.listEvents
									var events = [];
									angular.forEach($scope.listEvents,
											function(event, i) {
												if (i == idx) {
													events.push(updateEvent);
												} else {
													events.push(event);
												}
											})
									$scope.listEvents = angular.copy(events);
								}
								//console.log($scope.serieId);
								//console.log($scope.eventTypeId);
								/*if ($scope.serieId == null
										&& $scope.eventTypeId == null) {
									if (!$scope.isSingleEvent(updateEvent)) {
										$scope.launch("error", "", "请选择系列",
												function() {

												}, function() {
												});
										return;
									}
								}*/
								console.log(updateEvent);
								// save event
								if (!updateEvent.existed) {
									eventService.createEvent(updateEvent).then(
											function(data) {
												// updateEvent = data;
												// data.existed = true;
												updateEvent["id"] = data.id;
												updateEvent.existed = true;
												handleReturn(updateEvent);
											}, function(data) {
												console.log(data);
											})
								} else if (updateEvent.changed) {
									eventService.updateEvent(updateEvent.id,
											updateEvent).then(
											function(data) {
												handleReturn(data);

											},
											function(data) {
												$scope.launch("error", "",
														error.data.message,
														function() {

														}, function() {
														});
											});
								} else {
									handleReturn(updateEvent);
								}

							}
							$scope.freezeEvent = function(freezeEvent, idx) {
								if (confirm("是否确定要冻结本赛事")) {
									var handleReturn = function(data) {
										if (freezeEvent.existed) {
											freezeEvent.changed = !freezeEvent.changed;
										}
										var events = [];
										angular
												.forEach(
														$scope.listEvents,
														function(event, i) {
															if (i == idx) {
																events
																		.push(freezeEvent);
															} else {
																events
																		.push(event);
															}
														})
										$scope.listEvents = angular
												.copy(events);
									}
									freezeEvent.status = 2;
									eventService.updateEvent(freezeEvent.id,
											freezeEvent).then(
											function(data) {
												handleReturn(data);
											},
											function(data) {
												$scope.launch("error", "",
														error.data.message,
														function() {

														}, function() {
														});
											});
								}
							}

							$scope.deleteEvent = function(deleteEvent, idx) {
								console.log(deleteEvent);
								var handleReturn = function() {
									var events = [];
									angular.forEach($scope.listEvents,
											function(event, i) {
												if (idx != i) {
													events.push(angular
															.copy(event));
												}
											});
									$scope.listEvents = angular.copy(events);
									console.log($scope.listEvents)
								}
								if (!deleteEvent.existed) {
									deleteEvent = null;
									handleReturn();
								} else {
									eventService
											.deleteEvent(deleteEvent.id)
											.then(
													function(data) {
														deleteEvent = null;
														handleReturn();
													},
													function(data) {
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
								}

							}

							$scope.createEvent = function() {

								var toCreateEvent = {

								}

								toCreateEvent["existed"] = false;
								toCreateEvent["changed"] = true;
								toCreateEvent["showInput"] = true;
								toCreateEvent["status"] = 0;
								toCreateEvent["team"] = false;
								toCreateEvent["eventTypeId"] = $scope.eventTypeId == null ? ""
										: $scope.eventTypeId;
								toCreateEvent["seriesId"] = $scope.seriesId == null ? ""
										: $scope.seriesId;
								toCreateEvent["siteId"] = $scope.siteId;
								toCreateEvent["stage"] = $scope.seriesId == null ? 0
										: 1;
								toCreateEvent.eventSeries = [ {
									id : "",
									name : "N/A"
								} ];
								if ($scope.eventTypeId != null) {

									serieService
											.list($scope.eventTypeId)
											.then(
													function(data) {
														angular
																.forEach(
																		data,
																		function(
																				serie,
																				index) {
																			toCreateEvent.eventSeries
																					.push(serie);
																			/*
																			 * console.log("<<<<<<<<<<<<<<<")
																			 * console.log(serie.id)
																			 * console.log(serie.name)
																			 * console.log(event.seriesId)
																			 * console.log(">>>>>>>>>>>>>>>")
																			 */
																			// $scope.listEvents.push(event);
																		})
														$scope.listEvents
																.unshift(toCreateEvent);
													})
								} else {
									console.log("~~~~~");
									$scope.listEvents.unshift(toCreateEvent);
								}
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
						'ruleService',
						'eventRuleService',
						function($scope, userService, eventService, $interval,
								$timeout, $routeParams, $dialogs, tagService,
								ruleService, eventRuleService) {
							$scope.eventId = $routeParams.eventId;
							// console.log("~~~~~" + $scope.eventId);
							$scope.eventInit = {};
							$scope.isGeneratedGrades = false;
							$scope.generateGradesLabel = "生成表单";
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
																			attendee.min = Math
																					.floor(attendee.score / 60);
																			attendee.sec = attendee.score % 60;
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
																								attendee["editing"] = false;
																								attendee["editingTag"] = false;
																								newAttendees
																										.push(attendee);
																							});
																		});

														$scope.eventInit.attendees = angular
																.copy(newAttendees);
														$scope.eventInit.leftCount = $scope.eventInit.quota
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
							
							$scope.clickEditScore = function(attendee) {
								attendee.editing = true;
								console.log(attendee);
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
									ruleService
											.listAll()
											.then(
													function(data) {
														dlg = $dialogs
																.create(
																		'/admin/give_event_credit.html',
																		'GiveEventCreditCtrl',
																		{
																			eventTypeId : $scope.eventInit.eventTypeId,
																			serieId : $scope.eventInit.serieId,
																			attendee : selectedAttendee,
																			generalRules : data,
																			ruleService : ruleService,
																			eventRuleService : eventRuleService
																		}, 'lg');
														dlg.result
																.then(
																		function(
																				selectedRule) {
																			$scope.selectedRule = selectedRule;
																			$scope.applyCreditRuleAttendeeList
																					.push({
																						rule : selectedRule,
																						attendee : selectedAttendee
																					});
																		},
																		function() {

																		});
													}, function(error) {
													})

								} else {
									$scope.launch("error", "",
											"选手没有完成比赛，无法授予积分", function() {

											}, function() {
											});
								}
							}
							$scope.clickToEdit = function() {
								//if (canStartEditScore()) {
									$scope.editing = true;
								//}

							}
							var canStartEditScore = function() {
								var d = new Date();
								var t = d.getTime();
								if (!angular
										.isUndefined($scope.eventInit.eventTime)
										&& t < $scope.eventInit.eventTime) {
									$scope
											.launch(
													"error",
													"",
													"比赛还未开始！ 当前时间: "
															+ d
															+ ", 比赛时间: "
															+ new Date(
																	$scope.eventInit.eventTime),
													function() {

													}, function() {

													});
									return false;
								} else {
									return true;
								}
							}
							$scope.editScore = function() {
								//if (canStartEditScore()) {
									var m = {};
									angular
											.forEach(
													$scope.eventInit.attendees,
													function(attendee, index) {
														attendee.score = parseInt(attendee.min)
																* 60
																+ parseInt(attendee.sec);
														console
																.log(attendee.score);
														if (angular
																.isUndefined(m[attendee.tagId])) {
															m[attendee.tagId] = [];
														}
														if (attendee.score) {
															m[attendee.tagId]
																	.push(attendee);
														}
													});
									angular.forEach(m, function(attendees,
											tagId) {
										attendees.sort(function(a, b) {
											var key = "score";
											var x = a[key];
											var y = b[key];
											return ((x < y) ? -1 : ((x > y) ? 1
													: 0));
										});
										attendees.forEach(function(attendee,
												index) {
											attendee.rank = index + 1;
										})
									})
									/*
									 * m.forEach(function(attendees, tagId) {
									 * 
									 * });
									 */
								//}
							}

							$scope.generateGrades = function() {
								$scope.isGeneratedGrades = !$scope.isGeneratedGrades;
								if (!$scope.isGeneratedGrades) {
									$scope.generateGradesLabel = "生成表单";
								} else {
									$scope.generateGradesLabel = "返回选手管理";
								}
							}
							$scope.deleteAttendee = function(attendee, index) {
								
							}
							$scope.applyAttendeeChanges = function() {
								$scope.editScore();
								var error_msg = "";
								var error = false;
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
																							"修改成功",
																							"修改成功",
																							function() {

																							},
																							function() {
																							});
																			$scope
																					.init();
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
							}

						} ]);

zmxk.controller('GiveEventCreditCtrl', function($scope, $modalInstance, data) {

	// -- Variables --//
	$scope.data = data;
	$scope.selectRule = {};
	$scope.eventRules = $scope.data.generalRules

	// ruleService: ruleService,
	// eventRuleService: eventRuleService

	$scope.init = function() {
		var newRules = [];
		angular.forEach($scope.eventRules, function(rule, index) {
			if (angular.isUndefined(rule.rank)) {
				newRules.push(angular.copy(rule));
			}
		});
		$scope.eventRules = newRules;
	}

	// -- Methods --//

	$scope.cancel = function() {
		$modalInstance.dismiss('Canceled');
	}; // end cancel

	$scope.save = function(selectRule) {
		$scope.data.ruleService.apply({
			id : selectRule.id,
			attendeeId : $scope.data.attendee.id
		}).then(function(data) {
			console.log("A");
		});
		$modalInstance.close(selectRule);
	};
});