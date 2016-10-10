$(document).ready(function(){
	$(".uesr_phone").html(userPhone);
	var orderStatus="ALL";
	vip();
	function vip(){
	$.ajax({
	type:"post",
	url:webURL+"/service/team/findVipNum",
	data:{"userCode":usercode},
	success:function(msg){
		console.log(msg);
		if(msg.data==null){
			$("#rNum").html(0);
			$("#aNum").html(0);
			$("#bNum").html(0);
			$("#cNum").html(0);
		}else{
			var rNum=msg.data.rNum;
			var aNum=msg.data.aNum;
			var bNum=msg.data.bNum;
			var cNum=msg.data.cNum;
			$("#rNum").html(rNum);
			$("#aNum").html(aNum);
			$("#bNum").html(bNum);
			$("#cNum").html(cNum);
		}
	
	}
});
}
	//订单页码插件调用
	$.jqPaginator('#pagination1', {
		        totalPages: 100,
		        visiblePages:5,
		        currentPage: 1, 
		         prev: '<li class="prev"><a href="javascript:;">上一页</a></li>',
		         next: '<li class="next"><a href="javascript:;">下一页</a></li>',
		         page: '<li class="page"><a href="javascript:;">{{page}}</a></li>',
		         onPageChange: function (num, type){
		         serverTitle=$("#server_title").val();
		         orderInquiry(orderStatus,num)
		    }
		   });
		   
		  $("#rNum").click(function(){
		  orderStatus=$(this).text();
		   orderInquiry(orderStatus,num)
		  });
		$("#aNum").click(function(){
		  orderStatus=$(this).text();
		   orderInquiry(orderStatus,num)
		  });
		$("#bNum").click(function(){
		  orderStatus=$(this).text();
		   orderInquiry(orderStatus,num)
		  });
		$("#cNum").click(function(){
		  orderStatus=$(this).text();
		   orderInquiry(orderStatus,num)
		  });
		   
		   
		   
		   
		   
		   
		   
		   
		   
		   
		   
		   
		   
		   
	function orderInquiry(orderStatus,num){
	console.log(num);
	$.ajax({
		type:"post",
		url:webURL+"/service/team/findVipPage",
		async:true,
		data:{"str":"all","currentPage":num,"showCount":10,"userCode":usercode},
		success:function(msg){
			console.log(msg);
			 $("tr").remove(".table_tr");
			var msgdata=msg.data.tviplist;
			$.each(msgdata,function(i,n) {
			var relaLevel=msgdata[i].rela_level;
			var msgphone=msgdata[i].phone;
		    var msgbuyNum=msgdata[i].buyNum;
		    var msgrealName=msgdata[i].real_name;
		    var mstxdate=msgdata[i].rg_time.substr(0,10);
		    var msgtime = mstxdate.replace(/年/, "-").replace(/月/, "-").replace(/日/,"")
//		    console.log(msgtime);
//			console.log(msgStatus)
			var ajaxwrap="<tr class='table_tr'><td class='team_level'>"+relaLevel+"</td><td>"+msgtime+"</td><td class='zhifub'>"+msgphone+"</td><td class='zonge'>"+msgrealName+"</td><td class='goumai'>"+msgbuyNum+"</td></tr>"
		$(".table_order1").find("tbody").append(ajaxwrap);
		});
		}
	});
   }
	
})