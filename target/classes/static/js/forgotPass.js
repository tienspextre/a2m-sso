$(document).ready(async function () {
	AuthForgot.redirectUri = $("#redirectUri").val()
    $("#btn-forgot").unbind();
    $("#btn-forgot").click(function () {
        AuthForgot.methods.forgot();
    });

    $("#email-forgot").unbind();
    $("#email-forgot").keyup(function (e) {
        if (e.keyCode == 13) {   //enter key
            AuthForgot.methods.forgot();
        }
    });
    
    $("#username-forgot").unbind();
    $("#username-forgot").keyup(function (e) {
        if (e.keyCode == 13) {   //enter key
            AuthForgot.methods.forgot();
        }
    });
})

const AuthForgot = {
	redirectUri: "",
    methods: {
        forgot: () => {
    		const emailRegex = /^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,}$/;
    		const urlString = window.location.search;
		    const url = new URL("https://localhost:8097" + urlString);
		    const searchParams = url.searchParams;
			let email = $("#email-forgot").val();
            let username = $("#username-forgot").val()
            let redirectUri = searchParams.get("redirectUri")
            
            
            if (email.trim() == "") {
                $("#err-email-forgot").text("Email không được bỏ trống")
                return;
            }
            $("#err-email-forgot").text("")
			
            if (username.trim() == "") {
                $("#err-username-forgot").text("Tên tài khoản không được bỏ trống")
                return;
            }
            $("#err-username-forgot").text("")
            
            if (!emailRegex.test(email.trim())){
				$("#err-email-forgot").text("Định dạng email không đúng")
                return;
			}
			$("#err-email-forgot").text("")

            let SignupReq = {
                username: username,
                email: email,
                redirectUri: redirectUri
            }

            $.ajax({
                url: "/api/auth/forgot",
                type: "POST",
                dataType: "json",
                contentType: 'application/json',
                data: JSON.stringify(SignupReq)
            }).done(function (resp) {
				console.log(resp.status);
                if (resp.status == RESULT_OK) {
					$("#toast-mess").text("Link reset mật khẩu đã được gửi về mail của bạn")
                    $('#toast-container').show(0).delay(10000).hide(0);
                } else if (resp.status == RESULT_NG) {
                    $("#toast-mess").text("Tài khoản không tồn tại")
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