/**
 * 
 */

accessid = ''
accesskey = ''
host = ''
policyBase64 = ''
signature = ''
callbackbody = ''
filename = ''
key = ''
expire = 0
g_object_name = ''
g_object_name_type = ''
now = timestamp = Date.parse(new Date()) / 1000; 
var ossHost="http://jingtumt.oss-cn-shenzhen.aliyuncs.com/";
var g_objType='0';//0-图片；1-语音；2-视频
var bucketDir='';//默认OSS的路径
var curFile=null;

function send_request()
{
	
	var xmlhttp = null;
    if (window.XMLHttpRequest)
    {
        xmlhttp=new XMLHttpRequest();
    }
    else if (window.ActiveXObject)
    {
        xmlhttp=new ActiveXObject("Microsoft.XMLHTTP");
    }
  
    if (xmlhttp!=null)
    {
        serverUrl = '/servlet/PostObjectPolicy'
        xmlhttp.open( "GET", serverUrl, false );
        xmlhttp.send( null );
        return xmlhttp.responseText
    }
    else
    {
        alert("您的浏览器不支持 XMLHTTP.");
    }
};


function get_signature()
{
    //可以判断当前expire是否超过了当前时间,如果超过了当前时间,就重新取一下.3s 做为缓冲
    now = timestamp = Date.parse(new Date()) / 1000; 
    if (expire < now + 3)
    {
        body = send_request()
        var obj = eval ("(" + body + ")");
        host = obj['host']
        policyBase64 = obj['policy']
        accessid = obj['accessid']
        signature = obj['signature']
        expire = parseInt(obj['expire'])
        callbackbody = obj['callback'] 
        key = obj['dir'];
        bucketDir=key;
        return true;
    }
    return false;
};

function add0(m){  
    return m<10?'0'+m : m;  
}  
  
function random_string2(){  
    var time = new Date();  
    var y = time.getFullYear();  
    var m = time.getMonth()+1;  
    var d = time.getDate();  
    var h = time.getHours();  
    var mm = time.getMinutes();  
    var s = time.getSeconds();  
    var ms = time.getMilliseconds();
  
    return y+add0(m)+add0(d)+add0(h)+add0(mm)+add0(s)+ms;  
} 

function random_string(len) {
	len = len || 32;
	var chars = 'ABCDEFGHJKMNPQRSTWXYZabcdefhijkmnprstwxyz2345678';   
	var maxPos = chars.length;
	var pwd = '';
	for (i = 0; i < len; i++) {
		pwd += chars.charAt(Math.floor(Math.random() * maxPos));
	}
	return pwd;
}

function get_suffix(filename) {
    pos = filename.lastIndexOf('.')
    suffix = ''
    if (pos != -1) {
        suffix = filename.substring(pos)
    }
    return suffix;
}

function calculate_object_name(filename)
{
    if (g_object_name_type == 'local_name')
    {
        g_object_name += "${filename}"
    }
    else if (g_object_name_type == 'random_name')
    {
        suffix = get_suffix(filename)
        g_object_name = key + random_string2() + suffix;
    }else{
    	g_object_name= key +filename;
    }
    return ''
}

function get_uploaded_object_name(filename)
{
    if (g_object_name_type == 'local_name')
    {
        tmp_name = g_object_name
        tmp_name = tmp_name.replace("${filename}", filename);
        return tmp_name
    }
    else if(g_object_name_type == 'random_name')
    {
        return g_object_name
    }else{
    	 return g_object_name
    }
}

function set_upload_param(up, filename, ret)
{
    if (ret == false)
    {
        ret = get_signature()
    }
    if(bucketDir !=''){
    	key=bucketDir;
    }
    if(g_objType =='1'){
    	key +="voice/";
    }else if(g_objType =='2'){
    	key +="vedio/";
    }
    g_object_name = key;
    if (filename != '') { 
        calculate_object_name(filename);
    }
    new_multipart_params = {
        'key' : g_object_name,
        'policy': policyBase64,
        'OSSAccessKeyId': accessid, 
        'success_action_status' : '200', //让服务端返回200,不然，默认会返回204
        'callback' : callbackbody,
        'signature': signature,
    };    
    up.setOption({
        'url': host,
        'multipart_params': new_multipart_params
    });

//    up.start();
}

function isDom(obj) {
	var isDOM = ( typeof HTMLElement === 'object' ) ?
	        function(obj){
	          return obj instanceof HTMLElement;
	        } :function(obj){
	          return obj && typeof obj === 'object' && obj.nodeType === 1 && typeof obj.nodeName === 'string';
	        }
	 return isDOM(obj);
}

/*
 * 上传图片
 */
function UploadOSS(options,callback){
	if(options.uploadObjType!=null && options.uploadObjType!=''){
		g_objType= options.uploadObjType
	}else{
		g_objType='0';
	}
	var container = null;
	if(isDom(options.containerId)) {
		container = options.containerId;
	} else {
		container = document.getElementById(options.containerId);
	}
	var uploader = new plupload.Uploader({
		runtimes : 'html5,flash,silverlight,html4',
		browse_button : options.btnSelfile, 
	    multi_selection: options.multiSel,
		container: container,
		flash_swf_url : '../../js/plupload-2.1.2/js/Moxie.swf',
		silverlight_xap_url : '../../js/plupload-2.1.2/js/Moxie.xap',
	    url : 'http://jingtumt.oss-cn-shenzhen.aliyuncs.com',
	    //resize:options.resize,
	    filters: {
	        mime_types : [ //只允许上传图片
	            { title : "Image files", extensions : "jpg,gif,png,bmp,jpeg" }
	        ],
	        max_file_size : '100mb', //最大只能上传10mb的文件
	        prevent_duplicates : true //不允许选取重复文件
	    },

		init: {
			PostInit: function() {
				if(isDom(options.ossfileId))
					options.ossfileId.innerHTML = '';
				else
					document.getElementById(options.ossfileId).innerHTML = '';
			},

			FilesAdded: function(up, files) {
				var ossFileDom = null;
				if(isDom(options.ossfileId)) {
					ossFileDom = options.ossfileId;
				} else {
					ossFileDom = $('#'+options.ossfileId)[0];
				}
				$(ossFileDom).empty();
				plupload.each(files, function(file) {
					curFile=file;
					ossFileDom.innerHTML += '<div id="' + file.id + '" style="text-align:center;">' + file.name + ' (' + plupload.formatSize(file.size) + ')<b></b>'
					+'<div class="progress"><div class="progress-bar" style="width: 0%"></div></div>'
					+'</div>';
				});			
				$(ossFileDom).show();
//				set_upload_param(up, '', false);
				get_signature();//获取OSS访问的签名
				up.start();
			},

			BeforeUpload: function(up, file) {
				var filename=options.filenamePre + file.name
				if(!options.isUsefilename){
					var suffix= get_suffix(file.name);
					filename=options.filenamePre +suffix;
				}
	            set_upload_param(up, filename, true);//可以自定义文件名称
	        },

			UploadProgress: function(up, file) {
				var d = document.getElementById(file.id);
				d.getElementsByTagName('b')[0].innerHTML = '<span>' + file.percent + "%</span>";
	            var prog = d.getElementsByTagName('div')[0];
				var progBar = prog.getElementsByTagName('div')[0]
				progBar.style.width= 2*file.percent+'px';
				progBar.setAttribute('aria-valuenow', file.percent);
			},

			FileUploaded: function(up, file, info) {
	            if (info.status == 200)
	            {
//	                document.getElementById(file.id).getElementsByTagName('b')[0].innerHTML = 'upload to oss success, object name:' + get_uploaded_object_name(file.name) + ' 回调服务器返回的内容是:' + info.response;
	            	if(isDom(options.ossfileId)) {
	            		$(options.ossfileId).hide();
	            	} else {
	            		$('#'+options.ossfileId).hide();
	            	}
	            
	            	if($.isFunction(callback)){	
	            		callback( get_uploaded_object_name(file.name),curFile);
	            	}
//	            	laomaoTip.alert("上传成功！"+get_uploaded_object_name(file.name));
	            }
	            else if (info.status == 203)
	            {
//	                document.getElementById(file.id).getElementsByTagName('b')[0].innerHTML = '上传到OSS成功，但是oss访问用户设置的上传回调服务器失败，失败原因是:' + info.response;
	            	laomaoTip.alert("上传到OSS成功，但是oss访问用户设置的上传回调服务器失败!");
	            }
	            else
	            {
//	                document.getElementById(file.id).getElementsByTagName('b')[0].innerHTML = info.response;
	                laomaoTip.alert(info.response);
	            } 
			},

			Error: function(up, err) {
	            if (err.code == -600) {
//	                document.getElementById('console').appendChild(document.createTextNode("\n选择的文件太大了,可以根据应用情况，在upload.js 设置一下上传的最大大小"));
	            	laomaoTip.alert("选择的图片文件太大了！");
	            }
	            else if (err.code == -601) {
//	                document.getElementById('console').appendChild(document.createTextNode("\n选择的文件后缀不对,可以根据应用情况，在upload.js进行设置可允许的上传文件类型"));
	            	laomaoTip.alert("图片上传失败！");
	            }
	            else if (err.code == -602) {
//	                document.getElementById('console').appendChild(document.createTextNode("\n这个文件已经上传过一遍了"));
	            	laomaoTip.alert("图片已经上传过一遍了！");
	            }
	            else 
	            {
//	                document.getElementById('console').appendChild(document.createTextNode("\nError xml:" + err.response));
	            	$.message.alert("图片上传失败！");
	            }
	            
			}
		}
	}
	
	);
	uploader.init();
	
	addFile= function(files){
		uploader.addFile(files);
		uploader.start();
	};
	return uploader;
}

/*
 * 上传语音文件
 */
function UploadOSS_voice(options,callback){
	if(options.uploadObjType!=null && options.uploadObjType!=''){
		g_objType= options.uploadObjType
	}
	var container = null;
	if(isDom(options.containerId)) {
		container = options.containerId;
	} else {
		container = document.getElementById(options.containerId);
	}
	var uploader = new plupload.Uploader({
		runtimes : 'html5,flash,silverlight,html4',
		browse_button : options.btnSelfile, 
	    multi_selection: options.multiSel,
		container: container,
		flash_swf_url : '../../js/plupload-2.1.2/js/Moxie.swf',
		silverlight_xap_url : '../../js/plupload-2.1.2/js/Moxie.xap',
	    url : 'http://jingtumt.oss-cn-shenzhen.aliyuncs.com',
	    filters: {
	        mime_types : [ //只允许上传图片
	            { title : "音频文件", extensions : "mp3,ogg" }
	        ],
	        max_file_size : '100mb', //最大只能上传100mb的文件
	        prevent_duplicates : true //不允许选取重复文件
	    },

		init: {
			PostInit: function() {
				if(isDom(options.ossfileId))
					options.ossfileId.innerHTML = '';
				else
					document.getElementById(options.ossfileId).innerHTML = '';
			},

			FilesAdded: function(up, files) {
				var ossFileDom = null;
				if(isDom(options.ossfileId)) {
					ossFileDom = options.ossfileId;
				} else {
					ossFileDom = $('#'+options.ossfileId)[0];
				}
				$(ossFileDom).empty();
				plupload.each(files, function(file) {
					ossFileDom.innerHTML += '<div id="' + file.id + '" style="text-align:center;">' + file.name + ' (' + plupload.formatSize(file.size) + ')<b></b>'
					+'<div class="progress"><div class="progress-bar" style="width: 0%"></div></div>'
					+'</div>';
				});			
				$(ossFileDom).show();
				get_signature();//获取OSS访问的签名
				up.start();
			},

			BeforeUpload: function(up, file) {
				var filename=options.filenamePre + file.name
				if(!options.isUsefilename){
					var suffix= get_suffix(file.name);
					filename=options.filenamePre +suffix;
				}
	            set_upload_param(up, filename, true);//可以自定义文件名称
	        },

			UploadProgress: function(up, file) {
				var d = document.getElementById(file.id);
				d.getElementsByTagName('b')[0].innerHTML = '<span>' + file.percent + "%</span>";
	            var prog = d.getElementsByTagName('div')[0];
				var progBar = prog.getElementsByTagName('div')[0]
				progBar.style.width= 2*file.percent+'px';
				progBar.setAttribute('aria-valuenow', file.percent);
			},

			FileUploaded: function(up, file, info) {
	            if (info.status == 200)
	            {
//	                document.getElementById(file.id).getElementsByTagName('b')[0].innerHTML = 'upload to oss success, object name:' + get_uploaded_object_name(file.name) + ' 回调服务器返回的内容是:' + info.response;
	            	if(isDom(options.ossfileId)) {
	            		$(options.ossfileId).hide();
	            	} else {
	            		$('#'+options.ossfileId).hide();
	            	}
	            
	            	if($.isFunction(callback)){	
	            		callback( get_uploaded_object_name(file.name),curFile);
	            	}
//	            	laomaoTip.alert("上传成功！"+get_uploaded_object_name(file.name));
	            }
	            else if (info.status == 203)
	            {
//	                document.getElementById(file.id).getElementsByTagName('b')[0].innerHTML = '上传到OSS成功，但是oss访问用户设置的上传回调服务器失败，失败原因是:' + info.response;
	            	laomaoTip.alert("上传到OSS成功，但是oss访问用户设置的上传回调服务器失败!");
	            }
	            else
	            {
//	                document.getElementById(file.id).getElementsByTagName('b')[0].innerHTML = info.response;
	                laomaoTip.alert(info.response);
	            } 
			},

			Error: function(up, err) {
	            if (err.code == -600) {
//	                document.getElementById('console').appendChild(document.createTextNode("\n选择的文件太大了,可以根据应用情况，在upload.js 设置一下上传的最大大小"));
	            	laomaoTip.alert("选择的文件太大了！");
	            }
	            else if (err.code == -601) {
//	                document.getElementById('console').appendChild(document.createTextNode("\n选择的文件后缀不对,可以根据应用情况，在upload.js进行设置可允许的上传文件类型"));
	            	laomaoTip.alert("文件上传失败！");
	            }
	            else if (err.code == -602) {
//	                document.getElementById('console').appendChild(document.createTextNode("\n这个文件已经上传过一遍了"));
	            	laomaoTip.alert("文件已经上传过一遍了！");
	            }
	            else 
	            {
//	                document.getElementById('console').appendChild(document.createTextNode("\nError xml:" + err.response));
	            	laomaoTip.alert("文件上传失败！");
	            }
	            
			}
		}
	});
	uploader.init();
	return uploader;
}
