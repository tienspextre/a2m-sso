$(document).ready(async function () {
    AuthSignUp.redirectUri = $("#redirectUri").val()
    $("#btn-sign-up").unbind();
    $("#btn-sign-up").click(function () {
        AuthSignUp.methods.signup();
    });

    $("#password-sign-up").unbind();
    $("#password-sign-up").keyup(function (e) {
        if (e.keyCode == 13) {   //enter key
            AuthSignUp.methods.signup();
        }
    });
    $("#repeat-password-sign-up").unbind();
    $("#repeat-password-sign-up").keyup(function (e) {
        if (e.keyCode == 13) {   //enter key
            AuthSignUp.methods.signup();
        }
    });
})

const AuthSignUp = {
    redirectUri: "",
    methods: {
        signup: () => {
    		const emailRegex = /^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,}$/;
			let name = $("#fullname-sign-up").val();
			let email = $("#email-sign-up").val();
            let username = $("#username-sign-up").val()
            let password = $("#password-sign-up").val()
            let repPassword = $("#repeat-password-sign-up").val()
            
            if (name.trim() == "") {
                $("#err-fullname-sign-up").text("Họ và tên không được bỏ trống")
                return;
            }
            $("#err-fullname-sign-up").text("")
            
            if (email.trim() == "") {
                $("#err-email-sign-up").text("Email không được bỏ trống")
                return;
            }
            $("#err-email-sign-up").text("")
			
            if (username.trim() == "") {
                $("#err-username-sign-up").text("Tên tài khoản không được bỏ trống")
                return;
            }
            $("#err-username-sign-up").text("")

            if (password.trim() == "") {
                $("#err-password-sign-up").text("Mật khẩu không được bỏ trống")
                return;
            }
            $("#err-password-sign-up").text("")

            if (repPassword.trim() == "") {
                $("#err-repPassword-sign-up").text("Vui lòng nhập lại mật khẩu")
                return;
            }
            $("#err-repPassword-sign-up").text("")
            
            if (!emailRegex.test(email.trim())){
				$("#err-email-sign-up").text("Định dạng email không đúng")
                return;
			}
            
            if (password != repPassword) {
				$("#err-repPassword-sign-up").text("Mật khẩu không trùng khớp")
                return;
			}
			$("#err-repPassword-sign-up").text("")

            let SignupReq = {
                username: username,
                password: password,
                email: email,
                name: name,
                redirectUri: AuthSignUp.redirectUri
            }

            $.ajax({
                url: "/api/auth/signup",
                type: "POST",
                dataType: "json",
                contentType: 'application/json',
                data: JSON.stringify(SignupReq)
            }).done(function (resp) {
				console.log(resp.status);
                if (resp.status == RESULT_OK) {
					$("#toast-mess").text("Đăng ký thành công. Hệ thống sẽ tự động chuyển trang đăng nhập sau 5 giây")
                    $('#toast-container').show(0).delay(5000).hide(0);
                    setTimeout(function() {
					  AuthSignUp.methods.handleRedirect();
					}, 5100);
                } else if (resp.status == USER_EXISTED) {
                    $("#toast-mess").text("Tài khoản đã tồn tại")
                    $('#toast-container').show(0).delay(3000).hide(0);
                } else if (resp.status == EMAIL_EXISTED) {
                    $("#toast-mess").text("Email đã tồn tại")
                    $('#toast-container').show(0).delay(3000).hide(0);
                }
            }).fail(function (err) {
                $("#toast-mess").text("An error occured")
                $('#toast-container').show(0).delay(3000).hide(0);
            });
        },

        handleRedirect: () => {
            window.location.href = "/auth/login?redirectUri=" + AuthSignUp.redirectUri
        }
    }
}