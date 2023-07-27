$(document).ready(async function () {
	const urlString = window.location.search;
    const url = new URL("https://localhost:8097/auth/reset" + urlString);
    const searchParams = url.searchParams;
    AuthResetPass.redirectUri = searchParams.get("redirectUri");
    AuthResetPass.verifyKey = searchParams.get("verifyKey");
    document.getElementById("verifyKey").value = AuthResetPass.verifyKey;
    hide()
    $("#btn-reset").unbind();
    $("#btn-reset").click(function () {
        AuthResetPass.methods.signup();
    });

    $("#pass-reset").unbind();
    $("#pass-reset").keyup(function (e) {
        if (e.keyCode == 13) {   //enter key
            AuthResetPass.methods.signup();
        }
    });
    $("#repeat-pass-reset").unbind();
    $("#repeat-pass-reset").keyup(function (e) {
        if (e.keyCode == 13) {   //enter key
            AuthResetPass.methods.signup();
        }
    });
})

function hide(){
	const newUrl = "http://localhost:8097/auth/reset";
	history.pushState({}, document.title, newUrl);
}

const AuthResetPass = {
    redirectUri: "",
    verifyKey: "",
    methods: {
        signup: () => {
            let password = $("#pass-reset").val()
            let repPassword = $("#repeat-pass-reset").val()
			
            if (password.trim() == "") {
                $("#err-pass-reset").text("Mật khẩu không được bỏ trống")
                return;
            }
            $("#err-pass-reset").text("")

            if (repPassword.trim() == "") {
                $("#err-repeat-pass-reset").text("Vui lòng nhập lại mật khẩu")
                return;
            }
            $("#err-repeat-pass-reset").text("")
            
            if (password != repPassword) {
				$("#err-repeat-pass-reset").text("Mật khẩu không trùng khớp")
                return;
			}
			$("#err-repeat-pass-reset").text("")

            let SignupReq = {
                password: password,
                verifyKey: AuthResetPass.verifyKey
            }

            $.ajax({
                url: "/api/auth/reset",
                type: "POST",
                dataType: "json",
                contentType: 'application/json',
                data: JSON.stringify(SignupReq)
            }).done(function (resp) {
				console.log(resp.status);
                if (resp.status == RESULT_OK) {
					$("#toast-mess").text("Đặt lại mật khẩu thành công")
                    $('#toast-container').show(0).delay(3000).hide(0);
                    AuthResetPass.methods.handleRedirect();
                } else {
                    $("#toast-mess").text("Không hợp lệ")
                    $('#toast-container').show(0).delay(3000).hide(0);
                }
            }).fail(function (err) {
                $("#toast-mess").text("An error occured")
                $('#toast-container').show(0).delay(3000).hide(0);
            });
        },

        handleRedirect: () => {
            window.location.href = "/auth/login?redirectUri=" + AuthResetPass.redirectUri
        }
    }
}