var SUCCESS=0;
var ERROR=1;

$(function(){
	$('#login').click(loginAction);
	$('#count').blur(checkName);
	$('#password').blur(checkPassword);

	$('#regist_button').click(registAction);
	$('#regist_username').blur(checkRegistName);
	$('#regist_password').blur(checkRegistPassword);
	$('#confirm_password').blur(checkConfirm);
});

function checkName(){
	var name=$('#count').val();
	var rule=/^\w{4,10}$/;
	if(rule.test(name)){
		$('#count').next().empty();
		return true;	
	}
	$('#count').next().html('4~10个字符');
	return false;
}

function checkPassword() {
	var password=$('#password').val();
	var rule=/^\w{4,10}$/;
	if(rule.test(password)){
		$('#password').next().empty();
		return true;	
	}
	$('#password').next().html('4~10个字符');
	return false;
}

function loginAction(){
	var name=$('#count').val();
	var password=$('#password').val();
	
	var n=checkName()+checkPassword();
	if(n!=2){
		return;
	}
	var url='user/login.do'
	var data={name:name,password:password};
	$.post(url,data,function(result){
		if(result.state==SUCCESS){
			var user=result.data;
			console.log(user);
			addCookie("userId", user.id);
			location.href='edit.html';
		}else if(result.state==2){
			$('#count').next().html(result.message);
		}else if(result.state==3){
			$('#password').next().html(result.message);
		}else{
			alert(msg);
		}
	});
}

function checkRegistPassword(){
    var pwd = $('#regist_password').val().trim();
    var rule = /^\w{4,10}$/;
    if(rule.test(pwd)){
        $('#regist_password').next().hide();
        return true;
    }
    $('#regist_password').next().show()
        .find('span').html('4~10个字符');
    return false;
}

function checkRegistName(){
    var name = $('#regist_username').val().trim();
    var rule = /^\w{4,10}$/;
    if(rule.test(name)){
        $('#regist_username').next().hide();
        return true;
    }
    $('#regist_username').next().show()
      .find('span').html('4~10字符');
    return false;
}

function checkConfirm(){
    var pwd2 = $('#confirm_password').val();
    var pwd = $('#regist_password').val();
    //pwd 如果是空值表示 false, 非空则是true
    if(pwd && pwd==pwd2){
        $('#confirm_password').next().hide();
        return true;
    }
    $('#confirm_password').next().show()
        .find('span').html('确认密码不一致');
    return false;
}

function registAction(){
    console.log('registAction');
    //检验界面参数
    var n = checkRegistName() + checkRegistPassword() + checkConfirm();
    console.log(n);
    console.log(checkRegistName());
    console.log(checkRegistPassword());
    console.log(checkConfirm());
    if(n!=3){
        return ;
    }
    //获取界面中表单数据
    var name = $('#regist_username').val().trim();
    var nick = $('#nickname').val();
    var password = $('#regist_password').val();
    var confirm = $('#confirm_password').val();
    //发起AJAX请求
    var url = 'user/regist.do';
    var data = {name:name, nick:nick, password:password,  confirm:confirm};
    //console.log(data);
    // $.post 是 $.ajax的简化版
    $.post(url, data, function(result){
        console.log(result);
        if(result.state==0){
            //退回登录界面
            $('#back').click();
            var name = result.data.name;
            $('#count').val(name);
            $('#password').focus();
            //清空表单
            $('#regist_username').val('');
            $('#nickname').val('');
            $('#regist_password').val('');
            $('#confirm_password').val('');

        }else if(result.state==4){
            $('#regist_username').next().show()
              .find('span').html(result.message);
        }else if(result.state==3){
            $('#regist_password').next().show()
              .find('span').html(result.message);
        }else{
            alert(result.message);
        }
    });
    //得到响应以后, 更新界面
}