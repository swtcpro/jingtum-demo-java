/**
 * 
 */

$(document).ready(function(){
	loadImgList();
	$('#btnTochain').on('click',function(){
		audioToChain();
	})
	window.ossFilename="";
	initUpload();
	//注意此方法引用了SparkMD5库 library:https://github.com/satazor/SparkMD5
	//监听文本框变化
	
	document.getElementById("file").addEventListener("change", function() {
	    //声明必要的变量
	    var fileReader = new FileReader(), box = document.getElementById('box');
	    //文件分割方法（注意兼容性）
	    blobSlice = File.prototype.mozSlice || File.prototype.webkitSlice || File.prototype.slice, 
	    file = document.getElementById("file").files[0], 
	    //文件每块分割2M，计算分割详情
	    chunkSize = 2097152,                
	    chunks = Math.ceil(file.size / chunkSize), 
	    currentChunk = 0, 
	    //创建md5对象（基于SparkMD5）
	    spark = new SparkMD5();
	    //上传
	    window.uploader.addFile(file);
	    //每块文件读取完毕之后的处理
	    fileReader.onload = function(e) {
	        console.log("读取文件", currentChunk + 1, "/", chunks);
	        //每块交由sparkMD5进行计算
	        spark.appendBinary(e.target.result);
	        currentChunk++;

	        //如果文件处理完成计算MD5，如果还有分片继续处理
	        if (currentChunk < chunks) {
	            loadNext();
	        } else {
	            console.log("finished loading");
	            $('#box').val(  spark.end());
	            console.info("计算的Hash", spark.end());
	        }
	    };

	     //处理单片文件的上传
	     function loadNext() {
	         var start = currentChunk * chunkSize, end = start + chunkSize >= file.size ? file.size : start + chunkSize;

	         fileReader.readAsBinaryString(blobSlice.call(file, start, end));
	     }

	      loadNext();
	});
	
});

function fileToHash(file){
	//if(window.hashFile== null) return;
    //声明必要的变量
    var fileReader = new FileReader(), box = document.getElementById('box');
    //文件分割方法（注意兼容性）
    blobSlice = File.prototype.mozSlice || File.prototype.webkitSlice || File.prototype.slice, 
    //file = document.getElementById("file").files[0], 

    //文件每块分割2M，计算分割详情
    chunkSize = 2097152,                
    chunks = Math.ceil(file.size / chunkSize), 
    currentChunk = 0, 

    //创建md5对象（基于SparkMD5）
    spark = new SparkMD5();

    //每块文件读取完毕之后的处理
    fileReader.onload = function(e) {
        console.log("读取文件", currentChunk + 1, "/", chunks);
        //每块交由sparkMD5进行计算
        spark.appendBinary(e.target.result);
        currentChunk++;

        //如果文件处理完成计算MD5，如果还有分片继续处理
        if (currentChunk < chunks) {
            loadNext();
        } else {
            console.log("finished loading");
            box.innerText = 'MD5 hash:' + spark.end();
            console.info("计算的Hash", spark.end());
        }
    };

     //处理单片文件的上传
     function loadNext() {
         var start = currentChunk * chunkSize, end = start + chunkSize >= file.size ? file.size : start + chunkSize;

         fileReader.readAsBinaryString(blobSlice.call(file, start, end));
     }

      loadNext();
}


//根据目的地加载线路数据
function loadImgList(){
	$('#gridTxt').datagrid({
		url :"/jingtum/getAudioTransactionList",
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
		columns : [ [
		{
			field : 'filePath1',
			title : '音频',
			align : 'center',
			width : 90,
			formatter:function(value,row,index){ 
				var a="";
//				if(row.filePath !=null && row.filePath !=''){
//					a= "<img src='"+row.filePath+"'/ style='width:90px;height:80px'>";
//				}
				if(row.filePath !=null && row.filePath !=''){
					a='<audio src="'+ row.filePath +'" controls="controls">'
						+'你的浏览器不支持音频播放'
						'</audio>';
				}
				return a;
			}
		},
		{
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
			title : '上链数据', 
			align : 'center', 
			width : 150
		},{
			field : 'fee', 
			title : '手续费', 
			align : 'center', 
			width : 40
		},{
			field : 'filePath', 
			title : '文件路径', 
			align : 'center', 
			width : 80,
			formatter:function(value,row,index){
				return row.filePath +" ";
			}
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

function audioToChain(){
	if(document.getElementById("file").files==null || document.getElementById("file").files.length ==0){
		$.messager.alert('提示','请选择文件！')
		return;
	}
	if(window.ossFilename ==""){
		$.messager.alert('提示','文件正在处理中，请稍候！')
		return;
	}
	var url="/jingtum/audioToChain";
	var params={
			"audioHash":$('#box').val(),
			"filePath":window.ossFilename
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
				$('#txtTrainHash').val(data.data.memos[0]);
				if(data.data.memos.length>1){
					$('#txtFilePath').val(data.data.memos[1]);
				}else{
					$('#txtFilePath').val('');
				}
				$('#divAllData').html(xhr.responseText);
				$('#divShowData').window('open');
	  
			}else{
				$.messager.alert('提示','获取数据失败');
			}
		},'json');
}

function initUpload(){
	g_object_name_type = 'random_name';
	var options={
		btnSelfile:'btnImgFile',
		containerId:'top1',
		ossfileId:'ossfile',
		multiSel:false,
		filenamePre:'imgTest_',
		isUsefilename:true
//		resize:{//压缩图片
//	    	width: 800,
//	    	height: 600,
//	    	crop: false,
//	    	quality: 60,
//	    	preserve_headers: true //保留图片参数
//    	}
	};
	window.uploader = new UploadOSS_voice(options,function(coverPic,file){
		//上传完后回调
		//$(".upimg-div")[0].style.backgroundImage = "url("+ossHost + coverPic+")";//线路封面作为Div背景
		window.ossFilename=ossHost + coverPic;
		if(file !=null){
			//$('#file').val(file.name);
			
		}
		//routeCover=coverPic;
		//window.routeCoverPath =ossHost;
	});
	
}
