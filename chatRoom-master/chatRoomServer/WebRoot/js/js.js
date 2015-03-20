
//修改用户黑名单信息
function updateUserBL(userid,username){
	location.href="manager!updateUser.action?user.userid="+userid+"&user.username="+username;
}

//删除聊天室
function updateChatRoom(chatroomid){
	location.href="manager!updateChatRoom.action?chatRoom.chatroomid="+chatroomid;//?chatRoom="+chatRoom
}

//删除聊天室聊天记录
function updateChatRoomLog(logid){
	location.href="manager!updateChatRoomLog.action?chatRoomLog.logid="+logid;
}

//删除聊天室聊天记录
function updateChatPerLog(logid){
	location.href="manager!updateChatPerLog.action?chatPerLog.logid="+logid;
}

//删除购物车
function deleteCar(userId,bookId){
//function deleteCar(ShoppingCart car){
	if(confirm("您确定要删除该图书吗？")){
		//location.href = "shoppingCart!delete.action?shoppingCart.amount="+id;
		location.href = "shoppingCart!delete.action?shoppingCart.id.userId="+userId+"&shoppingCart.id.bookId="+bookId;
		//location.href = "shoppingCart!delete.action?shoppingCart="+car;
	}
}
//加
function fu(userId,bookId){
	//function deleteCar(ShoppingCart car){
		if(confirm("您确定要增加该图书吗？")){
			//location.href = "shoppingCart!delete.action?shoppingCart.amount="+id;
			location.href = "shoppingCart!inAmount.action?shoppingCart.id.userId="+userId+"&shoppingCart.id.bookId="+bookId;
			//location.href = "shoppingCart!delete.action?shoppingCart="+car;
		}
	}
//减
function su(userId,bookId){ 
	location.href = "book!decreaseAmount.action?shoppingCart.id.userId="+userId+"&shoppingCart.id.bookId="+bookId;
} 
function deleteOrder(orderId){ 
	if(confirm("您确定要删除该訂單吗？")){
		location.href = "order!deleteOrder.action?order.orderId="+orderId;
	}	
}

function selectODByOrder(orderId){ 
		location.href = "order!selectODByOrder.action?order.orderId="+orderId;
	
}
	function FloatAdd(arg1, arg2) {
	    var r1, r2, m;
	    try {
	        r1 = arg1.toString().split(".")[1].length;
	    } catch(e) {
	        r1 = 0;
	    }
	    try {
	        r2 = arg2.toString().split(".")[1].length;
	    } catch(e) {
	        r2 = 0;
	    }
	    m = Math.pow(10, Math.max(r1, r2)) ;
	    return (arg1 * m + arg2 * m) / m;
	}
	 
	//浮点数减法运算
	function FloatSub(arg1, arg2) {
	    var r1, r2, m, n;
	    try {
	        r1 = arg1.toString().split(".")[1].length;
	    } catch(e) {
	        r1 = 0;
	    }
	    try {
	        r2 = arg2.toString().split(".")[1].length;
	    } catch(e) {
	        r2 = 0;
	    }
	    m = Math.pow(10, Math.max(r1, r2));
	  //动态控制精度长度
	    n = (r1 >= r2) ? r1: r2;
	    return ((arg1 * m - arg2 * m) / m).toFixed(n);
	}