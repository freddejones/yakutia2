define(['backbone',
        'jquery',
        'underscore',
        'text!login/LoginView.html'],
    function(Backbone, $,  _, LoginViewTemplate) {

        // mimic: curl -i -X POST -d j_username=user -d j_password=user -c cookies.txt http://localhost:9090/yakutia-core/j_spring_security_check -v

        var LoginView = Backbone.View.extend({

            el: "#modalPlaceHolder",

            events: {
                "click #login" : "handleLogin"
            },

            initialize: function() {
                // TODO check if authentication is done and change button name to create account..
            },

            render: function() {
                var self = this;
                this.template = _.template(LoginViewTemplate);
                this.$el.html(this.template(this));
                $("#loginModal", this.$el) .modal();
                return this;
            },

            handleLogin: function () {
                var self = this;
                $.ajax({
//                    url: "http://localhost:8080/yakutia" + "/j_spring_security_check",
                    url: "http://localhost:8080/yakutia" + "/j_spring_openid_security_check",
                    type: "POST",
                    data: {j_username: "user", j_password: "user"},
                    success: function(data, status) {
                        if (status === "success") {
                            localStorage.setItem("username", "freddejones");
                            $("#loginModal", self.$el).modal('hide');
                            alert("delayed-:");
                            App.router.navigate("listgames", {trigger: true});
                        } else {
                            alert("not logged in");
                        }
                    },
                    error: function () {
                        alert("error logging in");
                    }

                });
            },

            close: function() {
                this.remove();
                this.unbind();
            }

        });
        return LoginView;
    });
