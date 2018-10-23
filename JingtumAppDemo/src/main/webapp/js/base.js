/**
 * 通用js，各页面包含改js
 */
function parseArgumentsCostom(url, data, success, dataType) {
  var hasData = !$.isFunction(data)
  return {
    url:      url,
    data:     hasData  ? data : undefined,
    success:  !hasData ? data : $.isFunction(success) ? success : undefined,
    dataType: hasData  ? dataType || success : success
  }
}
/*
 * ajax发送get
 * url, data：发送的数据如：{k:v,k2:v2}, success：成功回调函数, error：失败回调函数,dataType：传输的数据格式（json或xml)
 */
$.getCustom = function(url, data, success, error,dataType){
  return $.ajaxCustom(parseArgumentsCostom.apply(null, arguments));
}
$.getNoHead = function(url, data, success, error,dataType){
	  return $.ajaxNoHead(parseArgumentsCostom.apply(null, arguments));
	}
$.postCustom = function(url, data, success,error, dataType){
  var options = parseArgumentsCostom.apply(null, arguments);
  options.type = 'POST';
  return $.ajaxCustom(options);
}

$.getJSONCustom = function(url, data, success,error,complete){
  var options = parseArgumentsCostom.apply(null, arguments);
  options.dataType = 'json';
  options.type = 'POST'	  ;
  return $.ajaxCustom(options);
}

$.ajaxCustom = function(options){
	
//		var token = loadToken();
//		if(token != null){					
//			//options.headers = {'Authorization':token};
//			options.beforeSend= function(xhr){
//	             xhr.setRequestHeader("Authorization",localStorage.getItem("Authorization"));
//	         }
//		}

	$.ajax(options);
}

$.ajaxNoHead = function(options){
	$.ajax(options);
}

function loadToken(){
	return localStorage.getItem("Authorization");
}

function setLocalStorage(key,value){
	localStorage.setItem(key,value);
}
