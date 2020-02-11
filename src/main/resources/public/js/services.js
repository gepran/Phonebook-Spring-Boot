angular.module('app.services', []).service('UserService', function($http) {

    this.getUsers = function() {
        return $http({
            url : 'users/listOfUsers',
            method : 'GET'
        }).then(function(response) {
            return response.data;
        });
    };

    this.getProfileUser = function() {
        return $http({
            url : 'users/getProfileUser',
            method : 'GET'
        }).then(function(response) {
            return response.data;
        });
    };

    this.saveUser = function(userToSave) {
        if(userToSave.id){
            return $http({
                url : 'users/editUser',
                method : 'POST',
                data 	: userToSave
            }).then(function(response) {
                return response.status;
            },function errorCallback(errorMessage) {
                return errorMessage.data;
            });
        }else{
            return $http({
                url : 'users/addUser',
                method : 'POST',
                data 	: userToSave
            }).then(function(response) {
                return response.status;
            },function errorCallback(errorMessage) {
              return errorMessage.data;
            });
        }
    };
}).service('popupService',function($window){
    this.showPopup=function(message){
        return $window.confirm(message);
    }
}).service('RoleService',function($http){
    this.getRole = function() {
        return $http({
            url : 'users/getRole',
            method : 'GET'
        }).then(function(response) {
            return response.data;
        });
    };
}).service('PasswordForgotService',function($http){
    this.forgotPassword = function(email) {
        return $http({
            url : 'users/forgotPassword',
            method : 'POST',
            data : email
        }).then(function(response) {
            return response.status;
        }, function errorCallback(errorMessage) {
            return errorMessage.data;
        });
    };
}).service('ResetPasswordService',function($http){
    this.resetPassword = function(token) {
        return $http({
            url : 'users/resetPassword',
            method : 'POST',
            data : token
        }).then(function(response) {
            return response.status;
        }, function errorCallback(errorMessage) {
            return errorMessage.data;
        });
    };

    this.setNewPassword = function(newPassword) {
        return $http({
            url : 'users/setNewPassword',
            method : 'POST',
            data : newPassword
        }).then(function(response) {
            return response.status;
        }, function errorCallback(errorMessage) {
            return errorMessage.data;
        });
    };
}).service('ContactService',function($http){
    this.getContacts = function(paginationOptions) {
        pageNumber = paginationOptions.pageNumber > 0? paginationOptions.pageNumber - 1:0;
        return $http({
            url : 'contacts/list?pageNumber='+pageNumber
                +'&pageSize='+paginationOptions.pageSize
                +'&isAscending='+paginationOptions.isAscending
                +'&sortField='+paginationOptions.sort
                +'&searchExpression='+paginationOptions.searchString,
            method : 'GET',
        }).then(function(response) {
            return response.data;
        }, function errorCallback(errorMessage) {
            return errorMessage.data;
        });
    };

    this.deleteContact = function(contact){
        return $http({
            url : 'contacts/delete',
            method : 'POST',
            data : contact
        }).then(function(response) {
            return response.status;
        }, function errorCallback(errorMessage) {
            return errorMessage.data;
        });
    }

    this.saveContact = function(contact) {
        if(contact.id){
            return $http({
                url : 'contacts/edit',
                method : 'POST',
                data 	: contact
            }).then(function(response) {
                return response.status;
            },function errorCallback(errorMessage) {
                return errorMessage.data;
            });
        }else{
            return $http({
                url : 'contacts/add',
                method : 'POST',
                data 	: contact
            }).then(function(response) {
                return response.status;
            },function errorCallback(errorMessage) {
                return errorMessage.data;
            });
        }
    };
});
