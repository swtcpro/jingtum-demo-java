/**
 * @class 全局对象及公用方法，以及
 * @author 
 * @param
 * @description 为了防止全局变量和普通变量混淆，并且便于管理,把全局变量整理成一个全局对象
 */

/**
 * 定义全局对象，类似于命名空间或包的作用 
 */
var global = $.extend({}, global);

/**
 * 全局用户对象
 */
global.user_info = {};

/**
 * 登录用户个人信息初始化
 */
global.initUser = function(user_info){
	global.user_info = user_info;
};

/**
 * 赋值全局Title的公用方法
 */
global.setPanelTitle = function(titleString){
	$("#mainFrame").panel("setTitle",titleString);
};

/**
 * 
 * 增加命名空间功能
 * 使用方法：global.namespace('jQuery.bbb.ccc','jQuery.eee.fff');
 */
global.namespace = function(){
	var o = {}, d;
	for ( var i = 0; i < arguments.length; i++) {
		d = arguments[i].split(".");
		o = window[d[0]] = window[d[0]] || {};
		for ( var k = 0; k < d.slice(1).length; k++) {
			o = o[d[k + 1]] = o[d[k + 1]] || {};
		}
	}
	return o;
};

/**
 * 在页面加载之前，先开启一个进度条
 * 然后在页面所有easyui组件渲染完毕后，关闭进度条
 */
$.parser.auto = false;
$(function() {
	$.messager.progress({
		text : 'Loading......',
		interval : 100
	});
	$.parser.parse(window.document);
	window.setTimeout(function() {
		$.messager.progress('close');
		if (self != parent) {
			window.setTimeout(function() {
				try {
					parent.$.messager.progress('close');
				} catch (e) {
				}
			}, 500);
		}
	}, 1);
	$.parser.auto = true;
});

/**
 * 
 * @requires jQuery,EasyUI
 * 扩展datagrid，添加动态增加或删除Editor的方法
 * 例子如下，第二个参数可以是数组
 * datagrid.datagrid('removeEditor', 'cpwd');
 * datagrid.datagrid('addEditor', [ { field : 'ccreatedatetime', editor : { type : 'datetimebox', options : { editable : false } } }, { field : 'cmodifydatetime', editor : { type : 'datetimebox', options : { editable : false } } } ]);
 */
$.extend($.fn.datagrid.methods, {
	addEditor : function(jq, param) {
		if (param instanceof Array) {
			$.each(param, function(index, item) {
				var e = $(jq).datagrid('getColumnOption', item.field);
				e.editor = item.editor;
			});
		} else {
			var e = $(jq).datagrid('getColumnOption', param.field);
			e.editor = param.editor;
		}
	},
	removeEditor : function(jq, param) {
		if (param instanceof Array) {
			$.each(param, function(index, item) {
				var e = $(jq).datagrid('getColumnOption', item);
				e.editor = {};
			});
		} else {
			var e = $(jq).datagrid('getColumnOption', param);
			e.editor = {};
		}
	}
});

/**
 * 
 * @requires jQuery,EasyUI
 * 扩展datagrid，动态设置列标题的扩展方法
 * 例子如下，第二个参数可以是数组
 * $("#datagridID").datagrid("setColumnTitle",{field:"oldTitle",text:"newTitle"});
 */
$.extend($.fn.datagrid.methods, {  
	setColumnTitle: function(jq, option){  
		if(option.field){
			return jq.each(function(){  
				var $panel = $(this).datagrid("getPanel");
				var $field = $('td[field='+option.field+']',$panel);
				if($field.length){
					var $span = $("span",$field).eq(0);
					$span.html(option.text);
				}
			});
		}
		return jq;		
	}  
}); 

/**
 * 防止panel/window/dialog组件超出浏览器边界
 * @param left
 * @param top
 */
var easyuiPanelOnMove = function(left, top) {
	var l = left;
	var t = top;
	if (l < 1) {
		l = 1;
	}
	if (t < 1) {
		t = 1;
	}
	var width = parseInt($(this).parent().css('width')) + 14;
	var height = parseInt($(this).parent().css('height')) + 14;
	var right = l + width;
	var buttom = t + height;
	var browserWidth = $(document).width();
	var browserHeight = $(document).height();
	if (right > browserWidth) {
		l = browserWidth - width;
	}
	if (buttom > browserHeight) {
		t = browserHeight - height;
	}
	$(this).parent().css({/* 修正面板位置 */
		left : l,
		top : t
	});
};

$.fn.dialog.defaults.onMove = easyuiPanelOnMove;
$.fn.window.defaults.onMove = easyuiPanelOnMove;
$.fn.panel.defaults.onMove = easyuiPanelOnMove;

/**
 * 防止dialog自动弹出，销毁dialog
 * @requires jQuery,EasyUI
 * @param options
 */
global.dialog = function(options) {
	var opts = $.extend({
		modal : true,
		onClose : function() {
			$(this).dialog('destroy');
		}
	}, options);
	return $('<div/>').dialog(opts);
};

/**
 * 根据ID，关闭 dialog 对象
 * @requires jQuery,EasyUI
 * @param options
 */
global.closedDialog = function(dialogID){
	$("#"+dialogID).dialog("close");
};

/**
 * 加载进度条
 */
global.loading = function(str,title,msg,text,interval) {
	if(str === "open"){
		$.messager.progress({
			title:title,
			msg:msg,
			text:(text==undefined?"Loading .....":text),
			interval:(interval==undefined?300:interval)
		});
		$(".messager-window").css("z-index",1000001);
		$(".window-mask").css("z-index",1000000);
	}else if(str === "close"){
		$.messager.progress("close");
	}else{
		
	}
};

/**
 * 从URL中得到传过来的参数数组的公用方法
 * @returns paras
 */
global.getUrlParams = function(url){
	var paraString = url.substring(url.indexOf("?") + 1, url.length).split("&");
	var paras = {};
	for (i = 0; j = paraString[i]; i++) {
		paras[j.substring(0, j.indexOf("=")).toLowerCase()] = j.substring(j.indexOf("=") + 1, j.length);
	}
	return paras;
};

/**
 * 添加Frame Main Tabs对象的公用方法
 * @params
 * title：tabs的标题内容
 * icon：tabs的图标
 * url：tabs的URL路径
 * isRefresh：是否刷新tabs，true：刷新，false：不刷新
 * isClosed：是否允许关闭tabs，true：关闭，false：不关闭
 * @returns 
 */
global.addTabs = function(title,icon,url,isRefresh,isClosed){
	var content = '<iframe scrolling="yes" frameborder="0" src="'+url+'" style="width:100%;height:100%;"></iframe>';
	var tab = top.$('#tab_main_Id');
    if (tab.tabs('exists', title)) {
        tab.tabs('select', title);
		if(isRefresh){
			tab.tabs('update', {
				tab: tab.tabs('getTab',title),
				options: {
		            closable: isClosed,
					title: title,
		            content: content,
		            iconCls: icon
				}
			});
		}
    }else {
    	top.$('#tab_main_Id').tabs('add', {
            title: title,
            closable: true,
            content: content,
            iconCls: icon
        });
    }
};

/**
 * 刷新当前tab选项卡
 * */
global.flushCurrTab = function flushCurrentTab(){
	var parentObj = parent.$('#tab_main_Id');
	var currTab =  parentObj.tabs('getSelected'); //获得当前tab
	var url = $(currTab.panel('options').content).attr('src');
	parentObj.tabs('update', {
		tab : currTab,
		options : {
			// content : createFrame(url)
			content : '<iframe scrolling="auto" frameborder="0"  src="' + url
					+ '" style="width:100%;height:100%;"></iframe>'
		}
	});
};

/**
 * 获取 Frame Main Tabs对象指定选项卡，执行关闭操作
 * @params 
 * which：选中的tabs选项卡，值为选中tabs的title或者索引，如果不传此参数，则关闭当前选中tabs
 * @returns 
 */
global.closeTabs = function(which){
	var tab = parent.$('#tab_main_Id');
	if(which === undefined){
		var select = tab.tabs('getSelected');
		tab.tabs('close',tab.tabs('getTabIndex',select));
	}else{
		tab.tabs('close',tab.tabs('getTabIndex',tab.tabs("getTab",which)));
	}
};

/**
 * 获取 Frame Main Tabs对象指定选项卡，执行选中显示操作
 * @params 
 * which：选中的tabs选项卡，值为选中tabs的title或者索引
 * @returns 
 */
global.selectTabs = function(which){
	var tab = parent.$('#tab_main_Id');
	tab.tabs('select',which);
};

/**
 * 获取当前选中显示的 Tabs 索引
 * @returns 
 */
global.getTabsSelectIndex = function(){
	var tab = parent.$('#tab_main_Id');
	return tab.tabs("getTabIndex",tab.tabs('getSelected'));
};


/**
 * 根据数的ID，查找对应的数节点对象并返回
 * @params treeId：树节点的treeId
 * @returns 
 */
global.getTreeNode = function(treeId){
	return parent.$('#menuTree').tree('find', treeId);
};

/**
 * 根据数的ID，选中对应的数节点对象
 * @params treeId：树节点的treeId
 * @returns 
 */
global.selectTreeNode = function(treeId){
	var node = parent.$('#menuTree').tree('find', treeId);
	parent.$('#menuTree').tree('select', node.target);
};

/**
 * 取消选中对应的数节点对象
 * @params node：指定树节点 node
 * @ 如果不传参数，取消所有选中节点，否则取消指定选中节点
 * @returns 
 */
global.unCheckTreeNode = function(node){
	if(node == undefined){
		var nodes = parent.$('#menuTree').tree('getChecked');
		for(var i=0;i<nodes.length;i++){
			parent.$('#menuTree').tree('uncheck',nodes[i].target);
		}
	}else{
		parent.$('#menuTree').tree('uncheck',node.target);
	}
};

/**
 * 显示message消息提示信息
 * @params title：提示标题，msg：提示内容，timeout：提示显示时间
 * @returns 
 */
global.showMsg = function(title,msg,showType,timeout){
	$.messager.show({
        title: title,
        msg: msg,
        showType: showType,
        timeout: timeout
    });
};

/**
 * 设置Radio值的公用方法
 * @returns 
 */
global.setRadioValue = function(radioName, aValue) { // 传入一个对象
	var radio_oj = document.getElementsByName(radioName);
	for (var i = 0; i < radio_oj.length; i++) // 循环
	{
		if (radio_oj[i].value == aValue) // 比较值
		{
			radio_oj[i].checked = true; // 修改选中状态
			break; // 停止循环
		}
	}
};

/**
 * 接收一个以逗号分割的字符串，返回List，list里每一项都是一个字符串
 * @returns list
 */
global.getListByStr = function(value) {
	if (value != undefined && value != '') {
		var values = [];
		var t = value.split(',');
		for (var i = 0; i < t.length; i++) {
			values.push('' + t[i]);/* 避免他将ID当成数字 */
		}
		return values;
	} else {
		return [];
	}
};

/**
 * 将JSON对象转换成字符串
 * @param o
 * @returns string
 */
global.getJsonToString = function(o) {
	var r = [];
	if (typeof o == "string")
		return "\"" + o.replace(/([\'\"\\])/g, "\\$1").replace(/(\n)/g, "\\n").replace(/(\r)/g, "\\r").replace(/(\t)/g, "\\t") + "\"";
	if (typeof o == "object") {
		if (!o.sort) {
			for ( var i in o)
				r.push(i + ":" + obj2str(o[i]));
			if (!!document.all && !/^\n?function\s*toString\(\)\s*\{\n?\s*\[native code\]\n?\s*\}\n?\s*$/.test(o.toString)) {
				r.push("toString:" + o.toString.toString());
			}
			r = "{" + r.join() + "}";
		} else {
			for ( var i = 0; i < o.length; i++)
				r.push(obj2str(o[i]));
			r = "[" + r.join() + "]";
		}
		return r;
	}
	return o.toString();
};

/**
 * 增加formatString功能
 * 使用方法：global.getFormatString('字符串{0}字符串{1}字符串','第一个变量','第二个变量');
 * @returns 格式化后的字符串
 */
global.getFormatString = function(str) {
	for ( var i = 0; i < arguments.length - 1; i++) {
		str = str.replace("{" + i + "}", arguments[i + 1]);
	}
	return str;
};

/**
 * 当前日期时间格式化
 * @para :
 * @returns Array 
 */
global.getDateFormatString = function() {
	var array = new Array;
	var now = new Date();
	//年
    var year = now.getFullYear();    
	//月
    var month = now.getMonth() + 1;   
	//日  
    var day = now.getDate();            
	//时
	var hours = now.getHours(); 
	//分
	var minutes = now.getMinutes();
	//秒
	var seconds = now.getSeconds();
    var weekArray = new Array("星期日","星期一","星期二","星期三","星期四","星期五","星期六");
	//周
    var week = weekArray[now.getDay()];
	array[0] = year;
	array[1] = month;
	array[2] = day;
	array[3] = hours;
	array[4] = minutes;
	array[5] = seconds;
	array[6] = week;
	return array;
};

/**
 * 扩展Array的remove方法
 * @param {} obj
 */
Array.prototype.remove = function(obj) {
	for ( var i = 0; i < this.length; i++) {
		var temp = this[i];
		if (!isNaN(obj)) {
			temp = i;
		}
		if (temp == obj) {
			for ( var j = i; j < this.length; j++) {
				this[j] = this[j + 1];
			}
			this.length = this.length - 1;
		}
	}
};

/**
 * 扩展Date的format方法，用于格式化日期时间
 * @param format
 * @returns
 */
Date.prototype.format = function(format) {
	if (isNaN(this.getMonth())) {
		return '';
	}
	if (!format) {
		format = "yyyy-MM-dd hh:mm:ss";
	}
	var o = {
		/* month */
		"M+" : this.getMonth() + 1,
		/* day */
		"d+" : this.getDate(),
		/* hour */
		"h+" : this.getHours(),
		/* minute */
		"m+" : this.getMinutes(),
		/* second */
		"s+" : this.getSeconds(),
		/* quarter */
		"q+" : Math.floor((this.getMonth() + 3) / 3),
		/* millisecond */
		"S" : this.getMilliseconds()
	};
	if (/(y+)/.test(format)) {
		format = format.replace(RegExp.$1, (this.getFullYear() + "").substr(4 - RegExp.$1.length));
	}
	for ( var k in o) {
		if (new RegExp("(" + k + ")").test(format)) {
			format = format.replace(RegExp.$1, RegExp.$1.length == 1 ? o[k] : ("00" + o[k]).substr(("" + o[k]).length));
		}
	}
	return format;
};

/**
 * 扩展Date的DateDiff方法，计算两个日期之间的天数
 * @param {} sDate1和sDate2是2007-04-19格式 aid: 判断两个日期之间的天数；
 */
Date.prototype.DateDiff = function(sDate1,sDate2){ 
  var aDate, oDate1, oDate2, iDays;    
  aDate = sDate1.split("-");    
  oDate1 = new Date(aDate[1] + '-' + aDate[2] + '-' + aDate[0]);   //转换为3-1-2009格式    
  aDate = sDate2.split("-");    
  oDate2 = new Date(aDate[1] + '-' + aDate[2] + '-' + aDate[0]);    
  iDays = parseInt(Math.abs(oDate1 - oDate2) / 1000 / 60 / 60 /24);   //把相差的毫秒数转换为天数
  return iDays ;
};

/**
 * 获取当前月的第一天
 */
global.getCurrentMonthFirst = function(){
	var myDate = new Date();
    var year = myDate.getFullYear();
    var month = myDate.getMonth()+1;
    if (month < 10){
        month = "0" + month;
    }
    var firstDay = year + "-" + month + "-01";
	return firstDay;
};

/**
 * 获取当前月的最后一天
 */
global.getCurrentMonthLast = function(){
	var date = new Date();
	var currentMonth = date.getMonth();
	var nextMonth = ++currentMonth;
	var nextMonthFirstDay = new Date(date.getFullYear(), nextMonth, 1);
	var oneDay = 1000 * 60 * 60 * 24;
	return new Date(nextMonthFirstDay - oneDay);
};

/**
 * 扩展String的replaceAll方法，替换指定字符串所有内容
 * @param {} s1 需要替换的字符串，s2:替换指定的字符串；
 */
String.prototype.replaceAll = function(s1,s2){     
    return this.replace(new RegExp(s1,"gm"),s2);     
};

/**
 * @description 正则表达式验证方法，验证通过返回 true，验证不通过返回 false
 * @param {} input:需要验证的内容，type:需要验证的类型
 * @return boolean
 */
global.isValidate = function(input,type){  
    var reg;  
    if(type === "email"){
		/** 
		 * 验证是否是Email
		 */
		reg = /^([\.a-zA-Z0-9_-])+@([a-zA-Z0-9_-])+(\.[a-zA-Z0-9_-])/;
	}else if(type === "mobile"){
		/** 
		 * 验证是否是手机号码
		 */
		reg = /^\d{11}$/;
	}else if(type === "phone"){
		/** 
		 * 验证是否是固定号码 
		 */
		reg = /^(\(\d{3,4}\)|\d{3,4}-)?\d{7,8}$/;
	}else if(type === "idCardNo"){
		/**
		 * 验证身份证号码是否合法
		 * 身份证号码为15位或者18位，15位时全为数字，18位前17位为数字，最后一位是校验位，可能为数字或字符X  
		 */
		reg = /(^\d{15}$)|(^\d{18}$)|(^\d{17}(\d|X|x)$)/;
	}else if(type === "int"){
		/** 
		 * 验证是否是整数数字
		 */
		reg = /^[0-9]*$/;
	}else if(type === "float"){
		/** 
		 * 验证是否是小数数字
		 */
		reg = /^\d+(\d|(\.[1-9]{1,2}))$/;
	}
	return reg.test(input);  
};

/**
 * @description 判断浏览器是否是IE
 * @return boolean true：是IE，false：不是IE
 */
global.isIE = function(){  
	return document.all ? true : false;
};

/**
 * @description 计算arg1,arg2两个小数相乘的整数值
 * @return int
 */
global.accMul = function(arg1,arg2){
	var m=0,s1=arg1.toString(),s2=arg2.toString();
	try{m+=s1.split(".")[1].length}catch(e){}
	try{m+=s2.split(".")[1].length}catch(e){}
	return Number(s1.replace(".",""))*Number(s2.replace(".",""))/Math.pow(10,m);
};

/**
 * 格式化数字，如果后面为.00，或者点0等等，都去掉
 * 如：23.00 -->23
 * 14.50-->14.5
 * */
global.numberBoxFormatter = function (val){
	if(val!=undefined&&$.trim(val)!=""){
		var vals = val.replace(/(\d+)\.([0]+)$/,"$1");
		vals = vals.replace(/(\d+)\.([1-9]+)([0]+)/,"$1.$2");
		vals = vals.replace(/(\d+)\.([0]+)([1-9]+)/,"$1.$2$3");	
		return vals;
	}else{
		return 0;
	}
};

/**
 * @description 格式化字符串，去掉前后空格
 * @return string
 * */
String.prototype.trim = function() {
	return this.replace(/^\s+|\s+$/g, "");
};

/**
 * @description 字符串去掉重复数值
 * @return string
 * */
global.filterDuplicate = function(str) {
	var ar2 = str.split(",");
	var array = new Array();
	var j = 0;
	for ( var i = 0; i < ar2.length; i++) {
		if ((array == "" || array.toString().match(new RegExp(ar2[i], "g")) == null)
				&& ar2[i] != "") {
			array[j] = ar2[i];
			array.sort();
			j++;
		}
	}
	return array.toString();
};