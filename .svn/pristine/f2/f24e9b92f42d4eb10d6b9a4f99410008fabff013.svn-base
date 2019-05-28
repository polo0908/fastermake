

//工艺中文
var mainProcessList = [
                       {
                           name: "注塑"
                       },
                       {
                           name: "吸塑"
                       },
                       {
                           name: "滚塑"
                       },
                       {
                           name: "吹塑"
                       },
                       {
                           name: "挤塑"
                       },
                       {
                           name: "其他塑料工艺"
                       },
                       {
                           name: "不锈钢铸造(硅溶胶、水玻璃)"
                       },
                       {
                           name: "压铸(铝、锌、镁、铜等)"
                       },
                       {
                           name: "熔模铸(钢、铁、铜、铝等)"
                       },
                       {
                           name: "低压重力铸造(铝、铜等)"
                       },
                        {
                            name: "粉末冶金"
                        },
                       {
                           name: "其他铸造"
                       },
                       {
                           name: "热锻",                        
                       },
                       {
                           name: "冷锻",                        
                       },
                       {
                           name: "原料采购粗加工"
                       },
                        {
                            name: "其他钣金加工"
                        },
                        {
                            name: "激光等离子火焰切割"
                        },
                        {
                            name: "折弯"
                        },
                        {
                            name: "弯管"
                        },
                        {
                            name: "焊接"
                        },
                        {
                            name: "装配"
                        },
                        {
                            name: "冲压"
                        },
                        {
                            name: "深拉伸"
                        },
                        {
                            name: "数冲"
                        },
                        {
                            name: "旋压"
                        },
                        {
                            name: "铝挤压"
                        },
                       {
                           name: "3轴以上精密加工中心"
                       },
                       {
                           name: "高速车床"
                       },
                       {
                           name: "高速铣床"
                       },
                       {
                           name: "普通车铣钻磨"
                       },
                       {
                           name: "其他机加工"
                       },
                        {
                            name: "阳极氧化"
                        },
                        {
                            name: "喷塑"
                        },
                        {
                            name: "电镀"
                        },
                        {
                            name: "热浸锌"
                        },
                        {
                            name: "抛光或拉丝"
                        },
                        {
                            name: "其他表面处理"
                        },
                        {
                            name: "电子"
                        },
                        {
                            name: "零售包装"
                        },
                       {
                           name: "现成商品采购"
                       },
                       {
                           name: "其他采购"
                       }
                   ];


//添加工艺事件
function getProcessList(){	
	var tl = mainProcessList.length;
	var ary = [];
	for(var i=0;i<tl;i++){
		ary.push(mainProcessList[i].name)
	}
	return ary;
}

//获取工艺，添加到select
function addProcess(){
	var tl = mainProcessList.length;
	var options = "<option></option>";
	for(var i=0;i<tl;i++){
		options += '<option value="'+mainProcessList[i].name+'">'+mainProcessList[i].name+'</option>';	
	}
	return options;
}

function getProcessByIndex(index){
	return mainProcessList[index].name;
}

//获取工艺，添加到select
function addProcessIndex(){
    var tl = mainProcessList.length;
    var options = "<option value=\"-1\" class=\"first_option\">工艺</option>";
    for(var i=0;i<tl;i++){
        options += '<option value="'+mainProcessList[i].name+'">'+mainProcessList[i].name+'</option>';
    }
    return options;
}


//获取工艺，添加到select（用于创建产品）
function addProcessCreate(){
    var tl = mainProcessList.length;
    var options = "<option value=''>请选择</option>";
    for(var i=0;i<tl;i++){
        options += '<option value="'+mainProcessList[i].name+'">'+mainProcessList[i].name+'</option>';
    }
    return options;
}