angular.module('navController', [])
	.controller('nav', function($scope, $state, RoleService) {
		$scope.title = 'Phone Book';

		// returns true if the current router url matches the passed in url
		// so views can set 'active' on links easily
		$scope.isUrl = function(url) {
			if (url === '#') return false;
			return ('#' + $state.$current.url.source + '/').indexOf(url + '/') === 0;
		};


		function init(callback){
			var roles = {};
			RoleService.getRole().then(function (response) {
				roles = response;


				if(roles.indexOf("ROLE_ADMIN") !== -1){
					$scope.pages = [
						{
							name: 'Users',
							url: '#/users'
						},
						{
							name: 'Profile',
							url: '#/profile'
						},
						{
							name: 'Phone Book',
							url: '#/phonebook'
						},
						{
							name: 'Logout',
							url: '/logout'
						}];
				} else if(roles.indexOf("ROLE_USER") !== -1){
					$scope.pages = [
					    {
                            name: 'Phone Book',
                            url: '#/phonebook'
                        },
						{
							name: 'Profile',
							url: '#/profile'
						},{
							name: 'Logout',
							url: '/logout'
						}];
				}else{
					$scope.pages = [

						{
							name: 'Register',
							url: '#/register'
						},
						{
							name: 'Login',
							url: '/login'
						}];
				}

				callback && callback(response);
			});
		}

		init();

	});
