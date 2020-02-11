angular.module('app.controllers', []).controller('UserController', function($scope, $state, UserService, Notification) {
      function init(){
        UserService.getUsers().then(function (response) {
          $scope.users = response;
        });
      }

      init();

  $scope.user = {};

    var today = new Date();
    var minAge = 21;
    $scope.minAge = new Date(today.getFullYear() - minAge, today.getMonth(), today.getDate());

  $scope.addOrEditUser = function(){
    UserService.saveUser($scope.user).then(function (response) {
      if(response == 201){
        Notification.success('Saved Successfully');
        $state.go('users');
      } else if(response.message){
        Notification.error({message: response.message, delay: 1000});
      } else {
        Notification.error({message: 'Error', delay: 1000});
      }
    });
  }

}).controller('ProfileController', function($scope, $state, UserService, Notification) {
      function init(){
        UserService.getProfileUser().then(function (response) {
          $scope.user = response;


          if($scope.user != null && $scope.user.roles != null) {
            var roles = $scope.user.roles;
            var testRole = roles.filter(e => e.name === "FACEBOOK_USER");

            var role = roles.find(function(r){
                return r.name === "FACEBOOK_USER";
            });

            $scope.isFbConnected = testRole != null && testRole.size > 0;
          }
        });
      }

      init();

  $scope.addOrEditUser = function(){
    UserService.saveUser($scope.user).then(function (response) {
        if(response == 200){
            Notification.success('Saved Successfully');
            $state.go('profile');
        } else if(response.message){
            Notification.error({message: response.message, delay: 1000});
        } else {
            Notification.error({message: 'Error', delay: 1000});
        }
    });
  }

}).controller('ForgotPasswordController', function($scope, $state, PasswordForgotService, Notification) {
   $scope.forgotPassDetails = {};

  $scope.forgotPassword = function(){
    PasswordForgotService.forgotPassword($scope.forgotPassDetails.email).then(function (response) {
        if(response == 200){
            $state.go('reset');
        } else if(response.message){
           Notification.error({message: response.message, delay: 1000});
        } else {
           Notification.error({message: 'Error', delay: 1000});
        }
    });
  }

}).controller('ResetPasswordController', function($scope, $state, ResetPasswordService, Notification, $stateParams) {
    $scope.resetPassDetails = {};
        if($stateParams.token){
        $scope.resetPassDetails.resetToken = $stateParams.token;
    }

    $scope.resetPassword = function(){
        ResetPasswordService.resetPassword($scope.resetPassDetails).then(function (response) {
            if(response == 200){
                $scope.tokenIsReset = true;
            } else if(response.message){
                Notification.error({message: response.message, delay: 1000});
            } else {
                Notification.error({message: 'Error', delay: 1000});
            }
        });
    }

  $scope.setNewPassword = function(){
    ResetPasswordService.setNewPassword($scope.resetPassDetails).then(function (response) {
        if(response == 200){
            Notification.success('Password Updated');
            $state.go("home");
        } else if(response.message){
          Notification.error({message: response.message, delay: 1000});
        } else {
          Notification.error({message: 'Error', delay: 1000});
        }
    });
  }
}).controller('ContactController', function($scope, $state, ContactService, Notification) {
    var paginationOptions = {
        pageNumber: 1,
        pageSize: 25,
        sort: '',
        searchString: '',
        isAscending: true
    };
    $scope.gridOptions = {
        paginationPageSizes: [25, 50, 75],
        paginationPageSize: 25,
        useExternalPagination: true,
        useExternalSorting: true,
        columnDefs: [
            { name: 'id' },
            { name: 'firstName' },
            { name: 'secondName' },
            { name: 'phoneNumber'},
            {name: 'Edit',
            cellTemplate: '<div><button ng-click="grid.appScope.editContact(row.entity)">Edit</button></div>',
            enableSorting: false},
            {name: 'Delete',
            cellTemplate: '<div><button ng-click="grid.appScope.deleteContact(row.entity)">Delete</button></div>',
            enableSorting: false}
        ],
        onRegisterApi: function(gridApi) {
          $scope.gridApi = gridApi;
          $scope.gridApi.core.on.sortChanged($scope, function(grid, sortColumns) {
            if (sortColumns.length == 0) {
              paginationOptions.isAscending = !paginationOptions.isAscending;
            } else {
              paginationOptions.isAscending = sortColumns[0].sort.direction=='asc';
              paginationOptions.sort = sortColumns[0].colDef.name;
            }
            getPage();
          });
          gridApi.pagination.on.paginationChanged($scope, function (newPage, pageSize) {
            paginationOptions.pageNumber = newPage;
            paginationOptions.pageSize = pageSize;
            getPage();
          });
        }
    };


    var getPage = function() {
        ContactService.getContacts(paginationOptions).then(function(data){
            if(data.status == "FORBIDDEN"){
                $state.go("home");
            }
            $scope.gridOptions.data = data.content;
            $scope.gridOptions.totalItems = data.totalElements;
        });
    };

    $scope.search = function() {
        paginationOptions.searchString = $scope.searchterm;
        getPage();
    };

    getPage();

    $scope.editContact = function(contact) {
        var params = {contact: contact, editContact:true};
        $state.go('phonebook-edit', params);
    }

    $scope.deleteContact = function(contact) {
        ContactService.deleteContact(contact).then(function(response){
            if(response == 200) {
                Notification.success('Contact Deleted');
                getPage();
            } else if(response.message){
                Notification.error({message: response.message, delay: 1000});
            } else {
                Notification.error({message: 'Error', delay: 1000});
            }
        });
    }

    $scope.addContact = function(contact) {
        $state.go("phonebook-add");
    }

}).controller('WelcomeController', function($scope) {
    $scope.title = 'Welcome';
}).controller('ContactUpdateController', function($scope, $stateParams, ContactService, Notification, $state) {
     if($stateParams.editContact) {
        $scope.contactToSave = $stateParams.contact;
     } else {
        $scope.contactToSave = {};
     }

     $scope.addOrEditContact = function(){
         ContactService.saveContact($scope.contactToSave).then(function (response) {
             if(response == 200){
                 Notification.success('Contact Updated Successfully');
                 $state.go('phonebook');
             } else if(response == 201){
                 Notification.success('Contact Created Successfully');
                 $state.go('phonebook');
             }else if(response.message){
                 Notification.error({message: response.message, delay: 5000});
             } else {
                 Notification.error({message: 'Error', delay: 5000});
             }
         });
     }

     $scope.cancel = function() {
         $state.go('phonebook');
     }
});

