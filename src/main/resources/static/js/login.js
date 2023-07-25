$(document).ready(async function () {
    AuthLogin.redirectUri = $("#redirectUri").val()
    $("#actionLogin").unbind();
    $("#actionLogin").click(function () {
        AuthLogin.methods.login();
    });

    $("#password").unbind();
    $("#password").keyup(function (e) {
        if (e.keyCode == 13) {   //enter key
            AuthLogin.methods.login();
        }
    });
})

const AuthLogin = {
    redirectUri: "",
    methods: {
        login: () => {
            let username = $("#username").val()
            let password = $("#password").val()

            if (username.trim() == "") {
                $("#err-username-sign-in").text("Tài khoản không được bỏ trống")
                return;
            }
            $("#err-username-sign-in").text("")

            if (password.trim() == "") {
                $("#err-password-sign-in").text("Mật khẩu không được bỏ trống")
                return;
            }
            $("#err-password-sign-in").text("")

            let loginReq = {
                username: username,
                password: password
            }

            $.ajax({
                url: "/api/auth/login",
                type: "POST",
                dataType: "json",
                contentType: 'application/json',
                data: JSON.stringify(loginReq)
            }).done(function (resp) {
                if (resp.status == RESULT_OK) {
                    AuthLogin.methods.handleRedirect(resp.responseData);
                } else if (resp.status == RESULT_NG) {
                    $("#toast-mess").text("Tài khoản hoặc mật khẩu không chính xác")
                    $('#toast-container').show(0).delay(5000).hide(0);
                } else {
					$("#toast-mess").text("Tài khoản chưa được xác minh. Kiểm tra mail để xác minh tài khoản")
                    $('#toast-container').show(0).delay(5000).hide(0);
				}
            }).fail(function (err) {
                $("#toast-mess").text("Tài khoản hoặc mật khẩu không chính xác")
                $('#toast-container').show(0).delay(5000).hide(0);
            });
        },

        handleRedirect: (accessToken) => {
            window.location.href = AuthLogin.redirectUri + '?access_token=' + accessToken;
        }
    }
}