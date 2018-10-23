// 初始化main
$(document).ready(function(){
	// 初始化全局tab选项卡对象
	$('#tab_main_Id').tabs({
		onSelect:function(title,index){
			global.setPanelTitle("当前位置：" + title);
			if(index === 0){
				// 设置首页tabs显示的URL
				var URL = "first.html";
				// 设置首页tabs显示的TITLE
				var TITLE = "首页";
				// 设置首页tabs显示ICON
				var ICON = "icon-index";
				// 设置首页tabs显示CONTENT
				var CONTENT = '<iframe scrolling="yes" frameborder="0" src="'+URL+'" style="width:100%;height:100%;"></iframe>';
				// 设置默认首页Tabs内容
				var tab = parent.$('#tab_main_Id');
		        tab.tabs('update', {
					tab: tab.tabs('getTab',title),
					options: {
						title: TITLE,
			            content: CONTENT,
			            iconCls: ICON,
			            tools:[{
							iconCls:'icon-mini-refresh',
							handler:function(){
								global.addTabs(TITLE,ICON,URL,true,false);
								global.showMsg('操作提示','首页刷新成功','slide',5000);
							}
						}]
					}
				});
			}
		},
		onContextMenu:function(e,title,index){
			e.preventDefault();
            $('#tab_menu_Id').menu('show', {
                left : e.pageX,
                top : e.pageY,
				onClick : function(item){
					// 关闭当前选中的tabs选项卡
			        var tabs = $("#tab_main_Id");
			        if (item.name === "close") {
			            tabs.tabs("close", title);
			            return;
			        }
			        // 关闭所有tabs选项卡，或当前选中外的其他tabs选项卡
			        var allTabs = tabs.tabs("tabs");
			        var closeTabsTitle = [];
			        $.each(allTabs, function () {
			            var opt = $(this).panel("options");
			            if (opt.closable && opt.title != title && item.name === "other") {
			                closeTabsTitle.push(opt.title);
			            } else if (opt.closable && item.name === "all") {
			                closeTabsTitle.push(opt.title);
			            }
			        });
			        for (var i = 0; i < closeTabsTitle.length; i++) {
			            tabs.tabs("close", closeTabsTitle[i]);
			        }
				},
				onShow : function(){
					// 首页tabs不能关闭
					if(index === 0){
						$('#tab_menu_Id').menu("disableItem",$("#close"));
					}
				}
            });
		}
    });
});