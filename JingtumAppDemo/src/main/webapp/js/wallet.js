/**
 * 
 */
$(document).ready(function(){
	loadWalletList();
	$('#btnCreateWallet').on('click',function(){
		createWallet();
	})
});

//根据目的地加载线路数据
function loadWalletList(){
	$('#gridWallet').datagrid({
		url :"/jingtum/getWalletList",
		iconCls : 'icon-search',
		nowrap : false,
		fit : true,
		fitColumns : true,
		nowarp : false,
		border : false,
		idField : 'address',
		rownumbers : true,
		pagination : true,
		pageSize : 20,
		pageList : [ 10, 20, 30, 40, 50 ],
		singleSelect : true,
		sortName : "createTime",
		sortOrder : "DESC",
		loadMsg : '数据加载中 请稍后……',
		columns : [ [{
			field : 'address',
			title : '钱包地址',
			align : 'center',
			width : 100
		},{
			field : 'account',
			title : '手机号',
			align : 'center',
			width : 40
		},{
			field : 'createTime', 
			title : '创建时间', 
			align : 'center', 
			width : 100
	    },{
			field : 'action',
			title : '操作',
			align : 'center',
			width : 80,
			formatter:function(value,row,index){ 
				var a='';
				if(row.isActivity =="0"){
					a='<a class="easyui-linkbutton" data-options="iconCls:\'icon-edit\'" style="margin:5px;" onclick="activityWallet(\'' +row.address +'\')">激活</a>';
				}
				var b='<a class="easyui-linkbutton"  style="margin:5px;" onclick="getBalance(\'' +row.address +'\')" >余额</a>';
				return a+b;
			}
		}]],
		toolbar : "#toolbar",
		onLoadSuccess:function(data){
			$.parser.parse('td[field="action"]');
			$(this).datagrid("resize");
		}
	});
}

function createWallet(){
	if($('#txtPhone').val()==""){
		$.messager.alert('提示','请先输入手机号！');
		return;
	}
	var url="/jingtum/createWallet";
	var params={
			"account":$('#txtPhone').val()	
	};
	$.post(url,params,
		function(data,status){
		if(status =="success" && data.success=="true"){
			$.messager.alert('提示','创建成功！');
			$("#gridWallet").datagrid("reload",{ });  
		}else{
			$.messager.alert('提示','创建失败或手机号重复！');
		}
	},'json');
}

function activityWallet(address){
	var url="/jingtum/activityWallet";
	var params={
			"address":address
	};
	$.post(url,params,
			function(data,status){
			if(status =="success" && data.success=="true"){
				$.messager.alert('提示','激活成功！');
				$("#gridWallet").datagrid("reload",{ });  
			}else{
				$.messager.alert('提示','激活失败');
			}
		},'json');
}

function getBalance(address){
	var url="/jingtum/getBalance";
	var params={
			"address":address
	};
	$.post(url,params,
			function(data,status){
			if(status =="success" && data.data !=null){
				for(var i=0;i<data.data.balanceEntities.length;i++){
					if(data.data.balanceEntities[i].currency=="SWT"){
						$.messager.alert('提示','SWT余额：'+data.data.balanceEntities[i].value
								+"\n冻结："+data.data.balanceEntities[i].freezed );
						break;
					}
				} 
			}else{
				$.messager.alert('提示','查询失败');
			}
		},'json');
}