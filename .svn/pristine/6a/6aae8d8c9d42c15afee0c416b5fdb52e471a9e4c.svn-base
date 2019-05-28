var pageNum = $('#page').val();
var category = $('#category').val();
$(function() {


	// if(pageNum){
	// 	ajaxSelect(pageNum)
	// }else{
	// 	ajaxSelect(1)
	// }
    $('.panel-body').each(function(i){
        var factoryEchart = $('#rating_matrix'+ i).attr('jp-data');
        echartPic(i,factoryEchart)
    })


	$('#confirm').click(function() {
		
		$('#factoryAcreageGroup').find('i').removeClass('iactive')
		.addClass('i0').css('display', 'none').parent().css(
				'color', '')
		$('#staffNumberGroup').find('i').removeClass('iactive')
		.addClass('i0').css('display', 'none').parent().css(
				'color', '')
		$('#qualificationGroup').find('i').removeClass('iactive')
		.addClass('i0').css('display', 'none').parent().css(
				'color', '')
				
				
		// var url = location.href;
		
		//不刷新页面修改url
		// if(url.indexOf('#!fenye')>0){
		// 	newUrl = url.substring(0,url.indexOf('#!fenye'))
		// 	window.history.pushState({}, 0, newUrl);
		// }
		
		
		ajaxSelect(1)
	})

	$('.confirm').click(function() {
		$(this).parents('.sonOption').hide()
		
		// var url = location.href;
		
		//不刷新页面修改url
		// if(url.indexOf('#!fenye')>0){
		// newUrl = url.substring(0,url.indexOf('#!fenye'))
		// window.history.pushState({}, 0, newUrl);
		// }
		
		ajaxSelect(1)
	})



	$('.cleanDate').click(
			function() {
				$(this).parents('.sonOption').find('i').removeClass('iactive')
						.addClass('i0').css('display', 'none').parent().css(
								'color', '')
			})

})

function ajaxSelect(currpage) {

	var processGroup = [];
	var materialsGroup = [];
	var regionGroup = [];
	var qualificationGroup = [];
	var staffNumberGroup = [];
	var factoryAcreageGroup = [];

	// $('#productList').empty();
	// $('#processGroup span i').each(function() {
	// 	if ($(this).hasClass('iactive')) {
	// 		processGroup.push($(this).parent().text());
    //
	// 	}
	// })
	var processList = $('#processList').val();
	if(processList){
        var split = processList.split(",");
        for(var i=0;i<split.length;i++){
            processGroup.push(split[i]);
		}
	}



	$('#materialsGroup span i').each(function() {
		if ($(this).hasClass('iactive')) {
			materialsGroup.push($(this).parent().text());

		}
	})

	$('#regionGroup span i').each(function() {
		if ($(this).hasClass('iactive')) {
			regionGroup.push($(this).parent().text());
		}
	})

	$('#factoryAcreageGroup span i').each(function() {
		if ($(this).hasClass('iactive')) {
			factoryAcreageGroup.push($(this).parent().text());
		}
	})

	$('#staffNumberGroup span i').each(function() {
		if ($(this).hasClass('iactive')) {
			staffNumberGroup.push($(this).parent().text());
		}
	})
	$('#qualificationGroup span i').each(function() {
		if ($(this).hasClass('iactive')) {
			qualificationGroup.push($(this).parent().text());
		}
	})

	$.ajax({
		url : "/viewFactoryInfo/queryList.do",
		type : "post",
		traditional : true,
		data : {
			'processGroup' : processGroup,
			'materialsGroup' : materialsGroup,
			"regionGroup" : regionGroup,
			"qualificationGroup" : qualificationGroup,
			"staffNumberGroup" : staffNumberGroup,
			"factoryAcreageGroup" : factoryAcreageGroup,
			"page" : currpage
		},
		datatype : "json",
		success : function(result) {

			if (result.state == 0) {
				$('#factoryList').html('')
				var factoryList = result.data.factoryList;
				if (factoryList && factoryList.length > 0) {
					for (var i = 0; i < factoryList.length; i++) {

						var factoryInfo = factoryList[i]
						var factoryName = factoryInfo.factoryName
				        var factoryStatus = factoryInfo.factoryStatus
				        var qualificationList = factoryInfo.qualificationList
				        var factoryLogo = factoryInfo.factoryLogo
				        var specialLabel = factoryInfo.specialLabel
				        var prList = factoryInfo.prList
				        
				        var prListSize = prList.length
				        var factoryEchart = factoryInfo.factoryEchart
				        
						

						var addHtml = $('#tempDiv .panel-body').clone()

						 addHtml.find('dd').each(function() {
							var temp = factoryInfo[$(this).attr('class')]
							if(temp){
								$(this).text(temp)
							}
							
						
						})
						if(factoryLogo){    
							addHtml.find('.factoryLogo').attr('src', '/static_img/factory_logo/'+factoryInfo.factoryId+'/'+factoryLogo)
						}
						
						
						addHtml.find('.factoryName').text(factoryName?factoryName:"").attr('href','/manufacturer-category/'+factoryInfo.factoryId+'/info');
						
						
						 if(factoryStatus==1){
					        	addHtml.find('.factoryStatus').show()
					        }else{
					        	addHtml.find('.factoryStatus').hide()
					        }
						
						if(qualificationList&&qualificationList.length>0){
							var qualifications = ''
							for(var j=0;j<qualificationList.length;j++){
							if(qualificationList[j].qualificationName){
								if(j==0){
									qualifications+=qualificationList[j].qualificationName
								}else{
									qualifications+=','+qualificationList[j].qualificationName
								}
								
							}
							}
							
							addHtml.find('.qualifications').text(qualifications)	
							
						}
						
						
						addHtml.find('.specialLabel').text(specialLabel?specialLabel:"")
						
						addHtml.find('.rating-matrix').attr('id','rating_matrix_'+i)	
						// addHtml.find('.invitation').attr('href','/zh/quickly_release.html?factoryId='+factoryInfo.factoryId);

                        addHtml.find('.invitation').attr('onclick','showInquiry(\''+factoryInfo.factoryId+'\',\''+factoryInfo.factoryName+'\')')

                        addHtml.find('.equipment').attr('href','http://www.kuaizhizao.cn/manufacturer-category/'+factoryInfo.factoryId+'/equipments')

						addHtml.find('.product').attr('href','http://www.kuaizhizao.cn/manufacturer-category/'+factoryInfo.factoryId+'/products')

						addHtml.find('.environment').attr('href','http://www.kuaizhizao.cn/manufacturer-category/'+factoryInfo.factoryId+'/photos')

						addHtml.find('.allproducts').attr('href','http://www.kuaizhizao.cn/manufacturer-category/'+factoryInfo.factoryId+'/products').children().text('查看全部产品'+(prListSize?prListSize:""))

						
						addHtml.find('.comments').attr('href','http://www.kuaizhizao.cn/manufacturer-category/'+factoryInfo.factoryId+'/comments')
						


						

						$('#factoryList').append(addHtml.show())


						
						// echartPic(i,factoryEchart)
						
												
					}

				}
                var totalOrder = result.data.totalOrder;   //总数
                $("#PageCount").val(totalOrder);
                loadpage();
				imgchange();
			}
			
		}

	})
	

}


/**
 * 分页
 * @param num
 * @param type
 */
function exeData(num, type) {
    loadpage();
}
function loadpage() {

    var currentPage = $('#page').val();
    var myPageCount = parseInt($("#PageCount").val());
    var myPageSize = parseInt($("#PageSize").val());
    var countindex = myPageCount % myPageSize > 0 ? (myPageCount / myPageSize) + 1 : (myPageCount / myPageSize);
    $("#countindex").val(countindex);


    var isFirstPage = (pageNum == 1);//是否是第一页（true  false）
    var isLastPage = (pageNum == parseInt(countindex)); //是否是最后一页（true  false）
    var prevLink = '';
    var nextLink = '';
    if(isFirstPage){
        prevLink="javascript:;"
    }else{
        prevLink = '/manufacturer-category/'+category+'-'+(pageNum-1)+'';
    }
    if(isLastPage){
        nextLink="javascript:;"
    }else{
        nextLink = '/manufacturer-category/'+category+'-'+(pageNum+1)+'';
    }


    $.jqPaginator('#pagination', {
        totalPages: parseInt($("#countindex").val()),
        visiblePages: parseInt($("#visiblePages").val()),
        currentPage: Number(currentPage),
        first: '<li class="first"><a href="/manufacturer-category/'+category+'-1">首页</a></li>',
        prev: '<li class="prev"><a href="'+prevLink+'"><i class="arrow arrow2"></i>上一页</a></li>',
        next: '<li class="next"><a href="'+nextLink+'">下一页<i class="arrow arrow3"></i></a></li>',
        last: '<li class="last"><a href="/manufacturer-category/'+category+'-'+parseInt(countindex)+'">末页</a></li>',
        page: '<li class="page"><a href="/manufacturer-category/'+category+'-{{page}}">{{page}}</a></li>',
        onPageChange: function (num, type) {
            if (type == "change") {
                exeData(num, type);
            }
        }
    });

}









var option = {
    title : {
        /*text: '基础雷达图'*/
    },
    tooltip : {
        /*trigger: 'item',
        formatter: "{b}: {c} ({d}%)"*/
    },
    legend : {
        /*data: ['预算分配（Allocated Budget）']*/
    },
    symbolSize : '2',
    radar : {
        splitNumber : 4,
        radius : "50%",
        shape : 'circle',
        name : {
            textStyle : {
                color : '#333',
                /*backgroundColor: '#999',
                borderRadius: 3,
                padding: [3, 5]*/
            }
        },
        indicator : [ {
            name : '质量',
            max : 100
        }, {
            name : '价格',
            max : 100
        }, {
            name : '沟通',
            max : 100
        }, {
            name : '交期',
            max : 100
        } ],
        splitArea : {
            show : true,
            areaStyle : {
                type : "default",
                color : "rgb(255, 255, 255)"
            }
        }
    },
    series : [ {
        /*name: '预算 vs 开销（Budget vs spending）',*/
        type : 'radar',
        areaStyle : {
            normal : {
                color : "rgba(48,159,255,0.4)"
            }
        },
        data : [ {
            value : [ 100, 60, 80, 90 ],
            name : '预算分配'
        } ],
        itemStyle : {
            normal : {
                lineStyle : {
                    color : 'rgba(48,159,255,0.4)'
                }
            }
        },
        symbolSize : 0
    } ]
};
// 使用刚指定的配置项和数据显示图表。



function echartPic(i,obj){

    var option = {
        title : {
            /*text: '基础雷达图'*/
        },
        tooltip : {
            /*trigger: 'item',
            formatter: "{b}: {c} ({d}%)"*/
        },
        legend : {
            /*data: ['预算分配（Allocated Budget）']*/
        },
        symbolSize : '2',
        radar : {
            splitNumber : 4,
            radius : "50%",
            shape : 'circle',
            name : {
                textStyle : {
                    color : '#333',
                    /*backgroundColor: '#999',
                    borderRadius: 3,
                    padding: [3, 5]*/
                }
            },
            indicator : [ {
                name : '质量',
                max : 100
            }, {
                name : '价格',
                max : 100
            }, {
                name : '沟通',
                max : 100
            }, {
                name : '交期',
                max : 100
            } ],
            splitArea : {
                show : true,
                areaStyle : {
                    type : "default",
                    color : "rgb(255, 255, 255)"
                }
            }
        },
        series : [ {
            /*name: '预算 vs 开销（Budget vs spending）',*/
            type : 'radar',
            areaStyle : {
                normal : {
                    color : "rgba(48,159,255,0.4)"
                }
            },
            data : [ {
                value : [ 80, 80, 80, 90 ],
                name : '预算分配'
            } ],
            itemStyle : {
                normal : {
                    lineStyle : {
                        color : 'rgba(48,159,255,0.4)'
                    }
                }
            },
            symbolSize : 0
        } ]
    };
    // 使用刚指定的配置项和数据显示图表。



    if(obj){
        var factoryEchart = obj
        var  quality = factoryEchart.quality
        var price = factoryEchart.price
        var connect = factoryEchart.connect
        var delivery = factoryEchart.delivery
        option.series[0].data[0].value = []
        option.series[0].data[0].value.push(quality?quality:80)
        option.series[0].data[0].value.push(price?price:80)
        option.series[0].data[0].value.push(connect?connect:80)
        option.series[0].data[0].value.push(delivery?delivery:90)

    }




    var myChart = echarts.init(document.getElementById('rating_matrix_'+i));
    myChart.setOption(option);
	
	
	
}

function addcss() {
	/* body 高度控制底部位置 */
	var h1 = $(document.body).height()+80;
	var h2 = window.screen.availHeight;
	if (h1 < h2) {
		$('#footer').addClass('footer1')
	} else {
		$('#footer').removeClass('footer1')
	}
	

	/* 根据邮件状态和颜色，控制本行字体颜色 */
	$('tbody .wd').find('span,em').css({
		'color' : '#000'
	});
	/* 隔行换色效果 */
	$('table tbody tr:even').css({
		'background-color' : '#f9f9f9'
	})
	
}
function imgchange(){
		
	$('#factoryList').find('.factoryLogo').each(function(){		
		var realWidth;//真实的宽度
		var realHeight;//真实的高度

			realWidth = $(this).width();
			realHeight = $(this).height();
		var $m_h = (60 - realHeight)/2;
		$(this).parent().css({'padding-top':$m_h+'px'});
	   })
}	



//选择邀请报价 1、添加到工厂收藏
//2、 根据工厂收藏进行邀请报价
function showInquiry(factoryId,factoryName){

    if(!factoryId){
        return false;
    }

    $.ajax({
        url : "/viewFactoryInfo/addCollect.do",
        type : "post",
        traditional : true,
        data : {
            'factoryId' : factoryId,
            'factoryName' :factoryName,
			'url' : location.href
        },
        datatype : "json",

        success : function(result) {

            if (result.state == 0) {
                window.location = "/zh/quickly_release.html";
            }
            if (result.state == 2) {
                window.location = result.data;
            }
		}
	})

}





















