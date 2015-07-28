zmxk.controller('SideBarController', [ '$scope', function($scope) {
	/*
	 * var mainCtrlScope = $scope.$new();
	 * 
	 * $controller('MainController',{$scope : mainCtrlScope });
	 */
	$scope.processPages = function(pageType) {
		currentModule = pageType;
		$scope.hoopPage({
			"page" : pageType
		})
	}
	// console.log($scope.userId);

} ]);