(function() {
	var app = angular.module('app', ['ui.router', 'navController', 'ngAnimate',
	    'ui.bootstrap', 'ngResource', 'app.controllers', 'app.services', 'app.directives', 'ui-notification', 'ui.grid','ui.grid.pagination'])

	// define for requirejs loaded modules
	define('app', [], function() { return app; });

	function req(deps) {
		if (typeof deps === 'string') deps = [deps];
		return {
			deps: function ($q, $rootScope) {
				var deferred = $q.defer();
				require(deps, function() {
					$rootScope.$apply(function () {
						deferred.resolve();
					});
					deferred.resolve();
				});
				return deferred.promise;
			}
		}
	}

	app.config(function(NotificationProvider) {
        NotificationProvider.setOptions({
            delay: 10000,
            startTop: 20,
            startRight: 10,
            verticalSpacing: 20,
            horizontalSpacing: 20,
            positionX: 'right',
            positionY: 'bottom'
        });
    });

	app.config(function($stateProvider, $urlRouterProvider, $controllerProvider){
		var origController = app.controller
		app.controller = function (name, constructor){
			$controllerProvider.register(name, constructor);
			return origController.apply(this, arguments);
		}

		var viewsPrefix = 'views/';

		// For any unmatched url, send to /
		$urlRouterProvider.otherwise("/")

		$stateProvider
			// you can set this to no template if you just want to use the html in the page
			.state('home', {
				url: "/",
				templateUrl: viewsPrefix + "welcome.html",
				controller:'WelcomeController'
			})
		.state('users',{
	        url:'/users',
	        templateUrl: viewsPrefix + 'users.html',
	        controller:'UserController'
	    }).state('register',{
	        url:'/register',
	        templateUrl: viewsPrefix + 'user-add.html',
	        controller:'UserController'
	    }).state('forgot',{
	        url:'/forgotpassword',
	        templateUrl: viewsPrefix + 'forgot-password.html',
	        controller:'ForgotPasswordController'
	    }).state('reset',{
	        url:'/resetpassword/:token',
	        params: {token : null},
	        templateUrl: viewsPrefix + 'reset-password.html',
	        controller:'ResetPasswordController',
            resolve:{
                test: ['$stateParams', function($stateParams){
                    return $stateParams;
                }]
            }
	    }).state('editUser',{
	        url:'/profile/edit',
	        templateUrl: viewsPrefix + 'user-edit.html',
	        controller:'ProfileController'
	    }).state('profile',{
	        url:'/profile',
	        templateUrl: viewsPrefix + 'user-view.html',
	        controller:'ProfileController'
	    }).state('phonebook',{
	        url:'/phonebook',
	        templateUrl: viewsPrefix + 'phone-book.html',
	        controller:'ContactController',
	    }).state('phonebook-edit',{
	        url:'/phonebook-edit',
	        params: {contact: null, editContact: false},
	        templateUrl: viewsPrefix + 'phonebook-edit.html',
	        controller:'ContactUpdateController',
	        resolve:{
	            test: ['$stateParams', function($stateParams){
                    return $stateParams;
                }]
	        }
	    }).state('phonebook-add',{
	        url:'/phonebook-add',
	        templateUrl: viewsPrefix + 'phonebook-add.html',
	        controller:'ContactUpdateController',
	    })
	}).filter('yesNo', function() {
		return function(input) {
			return input ? 'Yes' : 'No';
		}
	}).directive('updateTitle', ['$rootScope', '$timeout',
		function($rootScope, $timeout) {
			return {
				link: function(scope, element) {
					var listener = function(event, toState) {
						var title = 'Project Name';
						if (toState.data && toState.data.pageTitle) title = toState.data.pageTitle + ' - ' + title;
						$timeout(function() {
							element.text(title);
						}, 0, false);
					};

					$rootScope.$on('$stateChangeSuccess', listener);
				}
			};
		}
	]);
}());
