function ajaxJsonp(options){
	//var _index = layer.load(0,{type:3,shade:[0.8,'#393D49'],scrollbar:false});
	var _options = jQuery.extend({dataType : 'jsonp',timeout : 60000}, options);
	var _seccess = _options.success;
	var _error = _options.error;
	var _beforeSend = _options.beforeSend;
	var _complete = _options.complete;
	_options.beforeSend = function(){
		jQuery.isFunction(_beforeSend) && _beforeSend();
	}
	_options.error = function(XMLHttpRequest, textStatus, errorThrown){
		if(textStatus == 'timeout'){
			alert("请求超时，请重试");
		}
		jQuery.isFunction(_error) && _error();
	}
	_options.success = function(data){
		jQuery.isFunction(_seccess) && _seccess(data);
	}
	_options.complete = function(){
		//layer.close(_index);
		jQuery.isFunction(_complete) && _complete();
	}
	jQuery.ajax(_options);
}

var isEmpty=function(ss){
	if(ss){
		return false;
	}
	return true;
}

var isEmpty=function(ss){
	if(ss){
		return false;
	}
	return true;
}

/**
 * 共用提示框
 */
var tiper = (function(){
	var wrapEle = $('<div class="widget-tiper"><div class="widget-tiper-inner"><div class="tiper-head"></div><div class="tiper-body"></div><div class="tiper-foot"></div></div></div>');
	var maskEle = $('<div class="widget-tiper-mask"></div>');
	var init = false;
	return function(title, content, button, callback){
		init || wrapEle.appendTo($('body')) && maskEle.appendTo($('body'));
		wrapEle.addClass('widget-tiper-active');
		maskEle.addClass('widget-tiper-active');
		wrapEle.find('.tiper-head').html(title);
		wrapEle.find('.tiper-body').html(content);
		wrapEle.find('.tiper-foot').empty();
		if($.isArray(button)){
			for(var i = 0; i < button.length; i++){
				$('<span class="tiper-button '+button[i].style+'">'+button[i].text+'</span>').click(function(){
					wrapEle.removeClass('widget-tiper-active');
					maskEle.removeClass('widget-tiper-active');
					$.isFunction(callback) && callback($(this).index());
				}).appendTo(wrapEle.find('.tiper-foot'));
			}
		}
		wrapEle.css({
			'left' : ($(window).width() - wrapEle.width())/2,
			'top' : ($(window).height() - wrapEle.height())/2
		});
		init = true;
	}
})();
var loadingElement = $('<div class="scroll-loading" style="display: none;"><span id="loadingElementContent"></span></div>');
loadingElement.appendTo($('body'));
window.hideloading = function(){
	loadingElement.hide();
}
window.showloading = function(time,message){
	if(message){
		$("#loadingElementContent").html(message);
	}else{
		$("#loadingElementContent").html("数据加载中...");
	}
	loadingElement.show();
	if(time>0){
		setTimeout("hideloading()",time);
	}
}
window.alert = function(message, callback){
	if(typeof(message) == 'string'){
		tiper('提示', message, [{style: 'alert', text: '确定'}], callback);
	}else{
		tiper('提示', message.title, [{style: 'alert', text: '确定'}], message.callback);
	}
}
window.confirm = function(message, callback){
	tiper('确定', message, [{style: 'cancal', text: '取消'},{style: 'define', text: '确定'}], callback);
}
/**
 * 带回调的提示框
 */
var alert_back=function(message,callback){
	alert({
		title:message,
		callback:callback
	});
}
/***全部替换11***/
String.prototype.replaceAll = function(reallyDo, replaceWith, ignoreCase) {  
    if (!RegExp.prototype.isPrototypeOf(reallyDo)) {  
        return this.replace(new RegExp(reallyDo, (ignoreCase ? "gi": "g")), replaceWith);  
    } else {  
        return this.replace(reallyDo, replaceWith);  
    }  
}

var onlyNum=function(obj){
	value=obj.value;
	obj.value=value.replace(/[^\d]/g,'');
} 