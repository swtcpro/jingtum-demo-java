/**
 * 
 */
$(document).ready(function(){
	loadTxtList();
	$('#btnTotrain').on('click',function(){
		txtToTrain();
	})
});

//根据目的地加载线路数据
function loadTxtList(){
	$('#gridTxt').datagrid({
		url :"/jingtum/getTxtTransactionList",
		iconCls : 'icon-search',
		nowrap : false,
		fit : true,
		fitColumns : true,
		nowarp : false,
		border : false,
		idField : 'clientId',
		rownumbers : true,
		pagination : true,
		pageSize : 20,
		pageList : [ 10, 20, 30, 40, 50 ],
		singleSelect : true,
		sortName : "payTime",
		sortOrder : "DESC",
		loadMsg : '数据加载中 请稍后……',
		columns : [ [{
			field : 'clientId',
			title : '交易ID',
			align : 'center',
			width : 50
		},{
			field : 'payAddress',
			title : '发送方地址',
			align : 'center',
			width : 100
		},{
			field : 'memos', 
			title : '文本数据', 
			align : 'center', 
			width : 150
		},{
			field : 'fee', 
			title : '手续费', 
			align : 'center', 
			width : 40
	    },{
			field : 'action',
			title : '操作',
			align : 'center',
			width : 80,
			formatter:function(value,row,index){ 
				var a='';
				a='<a class="easyui-linkbutton" data-options="iconCls:\'icon-edit\'" style="margin:5px;" onclick="viewTrainData(\'' +row.clientId +'\')">查看区块链中的数据</a>';
				
				return a;
			}
		}]],
		toolbar : "#toolbar",
		onLoadSuccess:function(data){
			$.parser.parse('td[field="action"]');
			$(this).datagrid("resize");
		}
	});
}

function txtToTrain(){
	if($("#txtTotrain").val() ==""){
		$.messager.alert('提示','请先输入文本信息')
		return;
	}
	var url="/jingtum/txtToTrain";
	var params={
			"txt":$("#txtTotrain").val()
	};
	$.post(url,params,
			function(data,status){
			if(status =="success" && data.success=="true"){
				$.messager.alert('提示','上链成功！');
				$("#gridTxt").datagrid("reload",{ });  
			}else{
				$.messager.alert('提示','上链失败');
			}
		},'json');
}

function viewTrainData(clientId){
	var url="/jingtum/getTransactionByClentid";
	var params={
			"clientId":clientId
	};
	$.post(url,params,
			function(data,status,xhr){
			if(status =="success" && data.data!=null){
				$('#divBackTxt').html(data.data.memos.join(","));
				$('#divAllData').html(xhr.responseText);
				$('#divShowData').window('open');
	  
			}else{
				$.messager.alert('提示','获取数据失败');
			}
		},'json');
}

