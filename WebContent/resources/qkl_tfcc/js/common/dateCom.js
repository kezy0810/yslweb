//普通会员我的推荐现象
//$(document).ready(function(){
//    var $tab1_li = $('.main-middle .huiyuan li');
//    // console.log($tab_li)
//    $tab1_li.click(function(){
//        $(this).addClass('act').siblings().removeClass('act');
//        var index = $tab1_li.index(this);
//        $(' .mains .btm').eq(index).show().siblings().hide();
//        $(' .main-top .top-tab').eq(index).show().siblings().hide();
//    })
//})

$(function(){
    function time(){
        var now = new Date();
        var years =now.getFullYear();
        var month = now.getMonth()+1;
        var date = now.getDate();
        var times = now.getHours()
        var minutes = now.getMinutes();
        var seconds=now.getSeconds();
        var week = now.getDay()-1;
        if(seconds<10){
          var  sec = '0'+seconds
        }else{
            var sec =seconds;
        }
        if(minutes<10){
            var min='0'+minutes
        }else{
            var min=minutes;
        }
        if(times<10){
            var time = '0'+times
        }else{
            var time = times;
        }

        $('.yearTime').html(years+'年'+month+'月'+date+'日');
        $('.dateTime').html(times+':'+ min+':'+sec);
        var weekArr = ['一','二','三','四','五','六','日']
        $('.weekTime').html('星期'+weekArr[week]);
    }
    setInterval(time,1000);
})
/**
 * Created by qw on 2016/8/26.
 */
