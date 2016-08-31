<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%
    String path = request.getContextPath();
    String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<!DOCTYPE html>
<html lang="en">
    <head>
        <base href="<%=basePath%>">
        <meta charset="utf-8" />
        <title></title>
    	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
        <link rel="stylesheet" href="resources/css/common/reset.css">
        <link rel="stylesheet" href="resources/css/common/common.css">
        <link rel="stylesheet" href="resources/css/general/general-self.css">
        <link rel="stylesheet" href="resources/css/date/dateRange.css">
        <link rel="stylesheet" href="resources/css/date/monthPicker.css">
        <script src="resources/js/date/dateRange.js"></script>
        <script src="resources/js/date/monthPicker.js"></script>
        <script src="resources/js/common/jquery-1.8.3.min.js"></script>
    </head>
<body>
<div class="mark2">
    <div class="mark2-box">
        <div class="success-icon"></div>
        <p>个人信息修改成功</p>
        <div class="achieve"><p>完成</p></div>
    </div>
</div>
<div class="header fix">
    <div class="logo fl">
        <img src="resources/images/reglogo_02.jpg" alt="">
    </div>
    <div class="nav-right fl">
        <div class="top">
            <span class="yearTime">2016年8月24日</span>
            <span class="dateTime">09:18:06 </span>
            <span class="weekTime"> 星期三 </span>
            <span>&nbsp; &nbsp;&nbsp; &nbsp;&nbsp; &nbsp;&nbsp;尊敬的 <span>1234334234</span> ，您好！&nbsp;欢迎使用本系统</span>
            <span class="exit"><a href="">【退出】</a></span>
        </div>
        <div class="nav fix">
            <ul class="fl">
                <li ><a href="general-team.html">我的团队</a></li>
                <li><a href="general-account.html">我的账户</a></li>
                <li ><a class="active" href="general-myhelf.html">个人中心</a></li>
                <li><a href="general-safety.html">安全中心</a></li>
                <li><a href="http://www.r8exchange.com">R8交易所</a></li>
            </ul>
        </div>
    </div>
</div>
<div class="container">
    <div class="main fl">
        <div class="mains">
            <div class="main-top">你当前的位置：<span>个人中心</span></div>
           
            <form id="form" action="/service/user/upload" class="form" method="post">
                <div class="main-photo">
                  <div class="pic fl">
                      <p>个人头像</p>
                      <div id="newsimgid"></div>
                  </div>
                  <div class="upload fl">
                      <p>上传头像</p>
  <!--                     <input type="file" value="选择文件"> -->
                      <label class="text_center;" style="color: red">原图上传：(请上传100*100规格图片)</label><br>
                      <input type="file" id="tp" name="tp" onchange="uploadimg(this)" value="选择文件"><br><br>
                  </div>
                </div>
                <div><label >姓名</label><input type="text" placeholder="未实名认证"><span class="hint">你还未实名认证，为保障你的交顺利进行，请及时前往安全中心进行认证</span></div>
                <div><label >身份证号码</label><input type="text"  placeholder="未实名认证"></div>
                <div><label >手机号</label><input type="text" placeholder="13810381111"><span class="hint"> 前往 ”安全中心“ 进行修改。</span></div>
                <div><label >微信号</label><input type="text"  placeholder="13810381111"></div>
                <div><label >支付宝账号</label><input type="text"  placeholder="13810381111"></div>
                <div><label >通讯地址</label><input type="text"  placeholder="北京市西城区南小街国英大厦1号"></div>
                <div><label >邮编</label><input type="text" placeholder="100015"></div>
                <div><a href="" onclick=""></a></div>
                <input type="hidden" id="filename"  />
                <input type="hidden" id="filepath"  />
            </form>
        </div>
    </div>
    <div class="side-bar fl">
        <div class="fl">
            <div class="myImg">
                <div>
                    <img src="" alt="">
                </div>
                <p>普通会员</p>
            </div>

            <div class="qr-code">
                <div>
                    <img src="resources/images/ma.jpg" alt="">
                </div>
                <p>我的二维码</p>
            </div>
        </div>
    </div>
</div>
<script src="resources/js/common/dateCom.js"></script>
</body>
</html>
<script type="text/javascript" >
  function uploadimg(obj){
      var filename = $(obj).attr('name');
      var filepath =$("input[type='file'][name='tp']").val();
      if(filepath==""){
          alert("请选择文件！");
          return;
      }
      $("#filename").val(filename);
      $("#filepath").val(filepath);
      $('#form').attr("target","aaa");
      $('#form').attr("enctype","multipart/form-data");
      var form  = document.getElementById("form");
      form.submit();
  }
</script>