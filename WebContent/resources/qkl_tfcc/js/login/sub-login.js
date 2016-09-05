$(function() {
	var flag = true;
	//手机失去焦点
	$('.input-input1 .phone').blur(function () {
		//手机号正则
		reg = /^1[3|4|5|7|8][0-9]\d{4,8}$/i;
		if ($(this).val() == '' || $(this).val() == '请输入你的手机号') {
			$(this).addClass('errorC');
			$(this).next().html('请输入你的手机号');
			flag = false;
			return;

		} else if ($(this).val().length < 11) {
			$(this).addClass('errorC');
			$(this).next().html('你输入的手机号的长度有误！');
			flag = false;
			return;
		} else if (!reg.test($(this).val())) {
			$(this).addClass('errorC');
			$(this).next().html('请输入正确的手机号');
			flag = false;
			return;
		} else {
			$(this).addClass('checkedC');
			$(this).removeClass('errorC');
			$(this).next().empty();
			flag = true;
			return;
		}
	});
	//密码失去焦点
	$('.input-input1 .pass').blur(function () {
		//密码正则6-16字母数字或特殊字符
		reg = /^[a-zA-Z][a-zA-Z0-9|*|&|%|.|@|!]{5,15}$/
		if ($(this).val() == '' || $(this).val() == '请输入密码') {
			$(this).addClass('errorC');
			$(this).next().html('请输入密码');
			flag = false;
			return;
		} else if (!reg.test($(this).val())) {
			$(this).addClass('errorC');
			$(this).next().html('密码是以字母开头的6-16数字字母或特殊字符');
			flag = false;
			return;
		} else {
			$(this).addClass('checkedC');
			$(this).removeClass('errorC');
			$(this).next().empty();
			flag = true;
			return;
		}
	});
	//验证码失去焦点
	$('.input-input1 .xiaoyan').blur(function () {
		//密码正则6-16字母数字或特殊字符
		reg = /^[a-zA-Z0-9]{6}$/ig;
		if ($(this).val() == '') {
			$(this).addClass('errorC');
			$(this).next().next().html('请输入验证码');
			flag = false;
			return;

		} else if (!reg.test($(this).val())) {
			$(this).addClass('errorC');
			$(this).next().next().html('请重新输入验证码');
			flag = false;
			return;
		} else if ($(this).val() != x) {
			$(this).next().next().html('验证码区分大小写');
		} else {
			$(this).addClass('checkedC');
			$(this).removeClass('errorC');
			$(this).next().next().empty();
			flag = true;
			return;
		}
	});


	// 生成验证码
	function auth_code() {
		var arr = ['1', '2', '3', '4', '5', '6', '7', '8', '9', '0', 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z', 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z'];
		return arr.sort(function () {
			return (Math.random() - .5)
		})
	}

	auth_code();
	function show_code() {
		var arr1 = '';
		var code = auth_code();
		for (var i = 0; i < 6; i++) {
			arr1 += code[i]
		}
		x = arr1;
		$("#check-code").text(arr1);
		console.log(x);
	}

	show_code();
	$("#check-code").click(function () {
		show_code();
	});


	$(".submit").click(function () {
		
		var showcnt =10; //每页页数初始值
		var  myselect=document.getElementById("showcnt");
		if(myselect==null||myselect=="null"){			
		}else{
			showcnt=myselect.options[myselect.selectedIndex].value;		
		}
		if(flag){
			reload_table(1,showcnt);					
		}

	})
	
	
});


function reload_table(currentPage,showCount) {
	 var rsStr="";
	 var phoneVal = $('.phone').val();
		console.log("phoneVal is "+phoneVal);
		var passVal = $('.pass').val();
		console.log("passVal is "+passVal); 
	$.ajax({
		type: 'post',
//		url: '../../../service/user/login/',
		url: '../../../service/test/queryuser/',
		dataType: 'json',
		data: {
			phone: $('.phone').val(),
			pass: $('.pass').val(),
			currentPage: currentPage,
			showCount:showCount
		},
		success: function (data) {
			alert('请求成功');
			var message = data.message;
			console.log("success is "+data.success);
			console.log("data is "+data.map);
			console.log("data.userList is "+data.data.userList);
			var usList =data.data.userList;
			console.log("usList.length "+usList.length);
			console.log("data.data.page.pageStr "+data.data.page.pageStr);
			var tablecols ="<tr> \n"
                              +" <th>用户名</th> \n"
                              + "<th>id</th> \n"
                            + "</tr> \n";
			rsStr=tablecols;
			for(var i=0;i<usList.length;i++){
				rsStr = rsStr+"<tr>";
				console.log("usList("+i+") name is "+usList[i].name);
				rsStr = rsStr +"<td>" + usList[i].name+"</td>";
				rsStr = rsStr +"<td>" + usList[i].test_user_id+"</td>";
				rsStr =rsStr+"</tr>";
			}
			
			console.log("rsStr "+rsStr);
//			 $(".result-tab").append(rsStr);
			 $(".result-tab").html(rsStr);
//			 $(".pages1").append(data.data.page.pageStr);
			 $(".pages1").html(data.data.page.pageStr);
			 
//			$('.phone').val(data.data.phone);
			 alert("message is "+message);
						 
			 
//			 if(data.success){
//				 window.location.href ="../index.html";
//			 }
		}, error: function (data) {
			alert('请求错误');
		}
	})
}


