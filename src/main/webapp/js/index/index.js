$(function() {

    // ajaxQuoteList();

    //获取成交订单走马灯、和最近一周询盘总金额
    ajaxGetTotalEstimatedPrice();


    // ajaxBigBuyer();

    // 判断供应商是否登录，登录后显示完善资料和匹配询盘
    var userInfo = getCookie("factoryInfo");
    if (userInfo == null || userInfo == '') {
        $('#s_div')
            .append(
                '<a href="/zh/login.html" class="active btn btn-primary btn-lg">登录</a><a href="/zh/register_zh.html" class="btn-default">注册</a>');
    } else {
        $('#s_div')
            .append(
                '<a href="/zh/quote_list.html" class="active">匹配询盘</a><a href="/zh/edit_enterprise_archives.html">完善资料</a>');

    }
    /*视频播放控制*/
    $('.home .close_button').click(function(){
        $(this).parent().hide();
    });
})

function ajaxQuoteList() {


    $.post("/inquiry/queryInquiryListToIndex.do",
        {
            "page" : 1,
            "pageSize" : 24
        },
        function(result){
            if (result.state == 0) {

                if (result.data.inquiryOrders) {
                    for (var i = 0; i < result.data.inquiryOrders.length; i++) {
                        var quoteProduct = result.data.inquiryOrders[i];
                        var selfFlag = true;
                        //判断是自营还是第三方询盘
                        if(quoteProduct.csgOrderId == null || quoteProduct.csgOrderId == ''){
                            selfFlag = false;
                        }else{
                            selfFlag = true;
                        }


                        //获取国家国旗
                        // var country = quoteProduct.country;
                        // var flagSrc=getFlag(country);
                        // var newQuote = "";
                        //获取允许报价的工厂数
                        // var maxNumber = quoteProduct.maxNumber;
                        // if(!maxNumber){
                        //     maxNumber = 3;
                        // }

                        // if(i>7){
                        //     newQuote = '<li>'
                        //         + '<div class="top">'
                        //         + '<img src="'+flagSrc+'" alt="RFQ came from '+country+'"/>'
                        //         + '<span>'
                        //         + (quoteProduct.mainProcess == null ? ''
                        //             : quoteProduct.mainProcess)
                        //         + '</span>'
                        //         + '<em class="xpdate">'+(quoteProduct.publishDate == null ? '' : (new Date(quoteProduct.publishDate.replace(/-/g,"/").split(".")[0])).Format("yyyy-MM-dd"))+'</em>'
                        //         + '</div>'
                        //         + '<div class="center">'
                        //         + '<div class="div_video">'
                        //         + '	<video src="'+(quoteProduct.videoPath == null ? '' : quoteProduct.videoPath)+'" controls="" loop=""></video>'
                        //         + '	<div class="close_button" onclick="closeVideo(this)"><span class="iconfont">&#xe643;</span></div>'
                        //         + '</div>'
                        //         + '<img src="'
                        //         + ((quoteProduct.drawingPathCompress == null || quoteProduct.drawingPathCompress == '') ? '/images/pic2.png'
                        //             : quoteProduct.drawingPathCompress)
                        //         + '" onclick="queryDetail('+quoteProduct.orderId+')" alt="'+ quoteProduct.quoteTitle +'"/>'
                        //         + (quoteProduct.videoPath == null || quoteProduct.videoPath == '' ? '' : '<div class="bf_botton" onclick="playVideo(this)"><span class="iconfont">&#xe626;</span><a title="点击观看3D"></a></div>')
                        //         + ' </div>'
                        //
                        //
                        //         + '<div class="bottom clearfix">'
                        //         + '<div class="bot-left pull-left">'
                        //         + '<h2><a class="clearfix" href="http://www.kuaizhizao.cn/rfq/'+quoteProduct.orderId+'">'+(quoteProduct.quoteTitle == null ? "" : quoteProduct.quoteTitle)+ '</a></h2>'
                        //         + '<span>'+(quoteProduct.materials == null ? "" : quoteProduct.materials) + '</span>';
                        // }else{
                        //     newQuote = '<li>'
                        //         + '<div class="top">'
                        //         + '<img src="'+flagSrc+'" alt="RFQ came from '+country+'"/>'
                        //         + '<span>'
                        //         + (quoteProduct.mainProcess == null ? ''
                        //             : quoteProduct.mainProcess)
                        //         + '</span>'
                        //         + '<em class="xpdate">'+(quoteProduct.publishDate == null ? '' : (new Date(quoteProduct.publishDate.replace(/-/g,"/").split(".")[0])).Format("yyyy-MM-dd"))+'</em>'
                        //         + '</div>'
                        //         + '<div class="center">'
                        //         + '<div class="div_video">'
                        //         + '	<video src="'+(quoteProduct.videoPath == null ? '' : quoteProduct.videoPath)+'" controls="" loop=""></video>'
                        //         + '	<div class="close_button" onclick="closeVideo(this)"><span class="iconfont">&#xe643;</span></div>'
                        //         + '</div>'
                        //         + '<img src="'
                        //         + ((quoteProduct.drawingPathCompress == null || quoteProduct.drawingPathCompress == '') ? '/images/pic2.png'
                        //             : quoteProduct.drawingPathCompress)
                        //         + '" onclick="queryDetail('+quoteProduct.orderId+')" alt="'+ quoteProduct.quoteTitle +'"/>'
                        //         + (quoteProduct.videoPath == null || quoteProduct.videoPath == '' ? '' : '<div class="bf_botton" onclick="playVideo(this)"><span class="iconfont">&#xe626;</span><a title="点击观看3D"></a></div>')
                        //         + ' </div>'
                        //         + '<div class="bottom clearfix">'
                        //         + '<div class="bot-left pull-left">'
                        //         + '<h2><a href="http://www.kuaizhizao.cn/rfq/'+quoteProduct.orderId+'">'+(quoteProduct.quoteTitle == null ? "" : quoteProduct.quoteTitle)+ '</a></h2>'
                        //         + '<span>'+(quoteProduct.materials == null ? "" : quoteProduct.materials) + '</span>';
                        // }



                        // if (quoteProduct.quantityList) {
                        //     var quantityList = quoteProduct.quantityList
                        //         .split(',');
                        //     var quantityUnit = quoteProduct.quantityUnit
                        //         .split(',');
                        //
                        //     newQuote += '<em class="em_l">' + quantityList[0] + ' '
                        //         + quantityUnit[0] + '</em><br/>';
                        //
                        // }

                        // newQuote += '</div>'
                        //     + '<div class="bot-right pull-right">'
                        //     + '<a target="_blank" href="http://www.kuaizhizao.cn/rfq/'+quoteProduct.orderId+'">报价</a><br/>'
                        //     + '<div class="text-right">'
                        //     + '<i>报价厂数:</i>'
                        //     + '<em><b>'+quoteProduct.currentNumber+'</b>/'+maxNumber+'</em>'
                        //     + '</div>'
                        //     + '</div>'
                        //     + '</li>';
                        //
                        // $('#newQuote').append(newQuote);


                    }

                    // $(".lazy").lazyload({
                    //     effect:'fadeIn'
                    // });
                }

            }
        }
    )

}






function ajaxBigBuyer() {

    $.ajax({
        url : "/bigBuyerComment/queryList.do",
        type : "post",
        traditional : true,
        data : {
            'currpage' : 1,
            'pageSize' : 8
        },
        datatype : "json",
        success : function(result) {
            if (result.state == 0) {

                var bigBuyerList = result.data.bigBuyerList;

                var userInfo = result.data.userInfo

                if (userInfo != null ) {

                    if(userInfo.factoryLogo != null){
                        $('#factoryLogo').attr(
                            'src',
                            (userInfo.factoryLogo ?('/static_img/factory_logo/'+ userInfo.factoryId + '/' +
                                userInfo.factoryLogo) : ""));
                    }
                    if(userInfo.factoryName != null){
                        $('#factoryLogo').attr('alt',userInfo.factoryName);
                    }

                }

                if (bigBuyerList) {
                    for (var i = 0; i < bigBuyerList.length; i++) {

                        var bigBuyerComment = bigBuyerList[i];

                        var newBigBuyer = '<li>'

                            + '<div class="top">'

                            + '<img src="/images/bj1.png" alt=""/>'
                            + '<span style="cursor:pointer"  src="/zh/buy_detail.html?id='
                            + bigBuyerComment.id
                            + '" onclick="jumpUrl(this)")">接受报名</span>'
                            + '</div>' + '<div class="center">'

                        if (bigBuyerComment.factoryInfo
                            && bigBuyerComment.factoryInfo.factoryLogo) {
                            newBigBuyer += '<img src="/static_img/factory_logo/'
                                + bigBuyerComment.buyerId
                                + '/'
                                + bigBuyerComment.factoryInfo.factoryLogo
                                + '" alt=""/>'
                        } else {
                            newBigBuyer += '<img src="/images/buypurchase1.png" alt=""/>'
                        }

                        newBigBuyer += '</div>'
                            + '<div class="bottom">' + '<h4>'
                            + bigBuyerComment.title + '</h4>'

                        if (bigBuyerComment.products) {
                            newBigBuyer += '<p>采购产品:'
                            var products = bigBuyerComment.products
                                .split("，");
                            for (var j = 0; j < products.length; j++) {
                                newBigBuyer += ' <em> ' + products[j]
                                    + '</em>'
                            }
                        }

                        newBigBuyer += '</p></div>'

                            + '<div class="li-hover">'
                        if (bigBuyerComment.requirement) {
                            newBigBuyer += '<div class="words">'
                                + '<p>采购商对于供应商的要求</p>'
                            var requirements = bigBuyerComment.requirement
                                .split(",");
                            for (var k = 0; k < requirements.length; k++) {
                                var indexK = k + 1;
                                if (k == requirements.length - 1) {
                                    newBigBuyer += '<span>' + indexK
                                        + '、' + requirements[k]
                                        + '。 </span><br/>';
                                } else {
                                    newBigBuyer += '<span>' + indexK
                                        + '、' + requirements[k]
                                        + '； </span><br/>';

                                }
                            }

                        }

                        newBigBuyer += ' </div>'
                            + '<a href="/zh/buy_detail.html?id='
                            + bigBuyerComment.id + '" target="_blank">查看详情</a>'
                            + '</div>' + '</li>';

                        $('#newBigBuyer').append(newBigBuyer)

                        $('.buyer-purchase li').hover(
                            function() {
                                $(this).children('.li-hover')
                                    .addClass('active')
                                    .siblings().children(
                                    '.li-hover')
                                    .removeClass('active');
                            },
                            function() {
                                $(this).children('.li-hover')
                                    .removeClass('active');
                            })

                    }

                }

            }
        }
    })
}

//获取询盘金额和走马灯
function ajaxGetTotalEstimatedPrice(){

    $.post("/inquiry/getTotalEstimatedPrice.do",
        function(result){
            if(result.state == 0){
                //获取一周询盘总价和日期
                var backWeekDate=getBackWeekDate();
                $('#estimatedDate').text("("+backWeekDate+")");
                $('#estimatedPrice').text("人民币 "+ Number((result.data.totalEstimatedPrice == 0 ? 630000 : result.data.totalEstimatedPrice)).toFixed(0));


                //工厂获取实单轮播
                var finishQuotes = result.data.finishQuotes;
                for(var j=0;j<finishQuotes.length;j++){
                    if(finishQuotes[j] != null){
                        var orderFactoryList = finishQuotes[j].orderFactoryList;
                        for(var n=0;n<orderFactoryList.length;n++){
                            var totalAmount = 0;
                            var factoryName = "";
                            var hideName = "";
                            var quoteEndDate = "";
                            if(!isNaN(Number(orderFactoryList[n].orderAmount))){
                                totalAmount =  Number(orderFactoryList[n].orderAmount/10000).toFixed(2);
                                if(totalAmount<2){
                                    continue;
                                }
                            }else{
                                continue;
                            }
                            if(orderFactoryList[n].orderFactoryName != null){
                                factoryName = orderFactoryList[n].orderFactoryName;
                                if(factoryName.length > 3){
                                    hideName = new Array(factoryName.length-3).join("*");
                                }

                                factoryName = factoryName.substr(0, 1) + hideName + factoryName.substr(factoryName.length-2);
                            }

                            if(orderFactoryList[n].orderTime){
                                quoteEndDate = orderFactoryList[n].orderTime;
                            }


                            var li = '<li>恭喜'+factoryName + quoteEndDate+'拿到<b class="red">'+totalAmount+'万</b>人民币订单&nbsp;/&nbsp;</li>';
                            $('#order_factory').append(li);
                        }
                    }

                }
                var len = $('#order_factory').find('li').text().length;
                var fontSize = $('#order_factory').find('li').find('b').css('fontSize');
                if(fontSize != '' && fontSize != null && fontSize != undefined){
                    fontSize = fontSize.replace("px",'');
                }
                var totalPx = 0;
                //字体按照12px来计算
                fontSize = 12;
                if(!isNaN(Number(fontSize))){
                    totalPx = Number(fontSize)*Number(len);
                    totalPx = Number(totalPx).toFixed(0);
                    $('#order_factory').css({"width":totalPx});
                }else{
                    return false;
                }

                //添加循环
                var style = document.styleSheets[0];
                style.insertRule("@keyframes loop{from{left:0px;}to{ left:-"+totalPx+"px;}",5);//写入样式
                style.insertRule("@-webkit-keyframes loop{from{left:0px;}to{ left:-"+totalPx+"px;}",5);//写入样式

                //添加游动时间
                var time = Number(totalPx/80);
                $('.factory_in').css({"-webkit-animation":"loop "+time+"s linear infinite","animation":"loop "+time+"s linear infinite"});


                if(finishQuotes.length > 0){
                    $('#new_quote').show();
                }
            }
        })
}





//查询详情
function queryDetail(orderId){

        window.open("/rfq/"+orderId);

}


function jumpUrl(obj) {

    if ($(obj).attr('src')) {

        window.location = $(obj).attr('src');

    }
}


/*视频播放控制*/
function playVideo(obj){

    $(obj).siblings('.div_video').show();
    var div_h = $('.home .div_video').height();
    var video_h =  $(obj).parent().find('video').height();
    var m_t = (div_h - video_h)/2;
    $(obj).parent().find('video').css({'margin-top':m_t +'px'});
    $('#newQuote').find('.div_video').hide().removeAttr('autoplay');
    $(obj).prev().prev().show().find('video').attr('autoplay',true);

    var $video = $(obj).prev().prev().show().find('video');
    $video.get(0).play();
}
function closeVideo(obj){
    $(obj).parent().hide().find('video').removeAttr('autoplay');
    var $video = $(obj).parent().find('video');
    $video.get(0).pause();
}

Date.prototype.Format = function (fmt) { //author: meizz
    var o = {
        "M+": this.getMonth() + 1, //月份
        "d+": this.getDate(), //日
        "h+": this.getHours(), //小时
        "m+": this.getMinutes(), //分
        "s+": this.getSeconds(), //秒
        "q+": Math.floor((this.getMonth() + 3) / 3), //季度
        "S": this.getMilliseconds() //毫秒
    };
    if (/(y+)/.test(fmt)) fmt = fmt.replace(RegExp.$1, (this.getFullYear() + "").substr(4 - RegExp.$1.length));
    for (var k in o)
        if (new RegExp("(" + k + ")").test(fmt)) fmt = fmt.replace(RegExp.$1, (RegExp.$1.length == 1) ? (o[k]) : (("00" + o[k]).substr(("" + o[k]).length)));
    return fmt;
}


//获取系统前一周的时间
function getBackWeekDate(){
    var nowdate = new Date();
    var oneweekdate = new Date(nowdate-7*24*3600*1000);
    var y = oneweekdate.getFullYear();
    var m = oneweekdate.getMonth()+1;
    var d = oneweekdate.getDate();
    if(m.toString().length == 1){
        m="0"+m;
    }
    if(d.toString().length == 1){
        d="0"+d;
    }
    var formatwdate = y+'-'+m+'-'+d;
    return formatwdate;
}




