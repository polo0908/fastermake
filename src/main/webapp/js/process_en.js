var mainProcessListEn = [
                       {
                           name: "Injection molding"
                       },
                       {
                           name: "Vacuum forming"
                       },
                       {
                           name: "Rotational molding"
                       },
                       {
                           name: "Blow molding"
                       },
                       {
                           name: "Plastic extrusion"
                       },
                       {
                           name: "Others(Plastic)"
                       },
                       {
                           name: "Stainless steel casting (silicon glue, water glass)"
                       },
                       {
                           name: "Die casting (aluminum, zinc, magnesium, copper, etc.)"
                       },
                       {
                           name: "Sand casting (steel, iron, copper, aluminum, etc.)"
                       },
                       {
                           name: "Gravity casting (aluminum, copper, etc.)"
                       },
                       {
                           name: "Powder metallurgy"
                       },
                       {
                           name: "Others(casting)"
                       },
                       {
                           name: "Forging"
                       },
                       {
                    	   name: "Hot forging"
                       },
                       {
                    	   name: "Cold forging"
                       },
                       {
                           name: "Raw material procurement"
                       },
                       {
                           name: "Other sheet metal processing"
                       },
                       {
                           name: "Laser,plasma,flame cutting"
                       },
                       {
                           name: "Sheet metal bending"
                       },
                       {
                           name: "Tube bending"
                       },
                       {
                           name: "Welding"
                       },
                       {
                           name: "Assembly"
                       },
                       {
                           name: "Stamping"
                       },
                       {
                           name: "Deep drawing"
                       },
                       {
                           name: "Turret punching"
                       },
                       {
                           name: "Spinning"
                       },
                       {
                           name: "Aluminum extrusion"
                       },
                       {
                           name: "CNC machining center (3-axes or more)"
                       },
                       {
                    	   name: "High-speed lathe machine"
                       },
                       {
                    	   name: "High-speed milling machine"
                       },
                       {
                    	   name: "Ordinary lathe & millilng machine"
                       },
                       {
                    	   name: "Others"
                       },
                       {
                    	   name: "Anodizing"
                       },
                       {
                    	   name: "Spray paint"
                       },
                       {
                    	   name: "Electroplating"
                       },
                       {
                    	   name: "Hot-dip galvanizing"
                       },
                       {
                    	   name: "Polishing or wiredrawing"
                       },
                       {
                    	   name: "Other surface treatment"
                       },
                       {
                    	   name: "Electronics"
                       },
                       {
                    	   name: "Retail packaging"
                       },
                       {
                           name: "Prompt Goods"
                       },
                       {
                           name: "Other purchases"
                       }
                   ];


//添加工艺事件
function addProcessEn(obj){
	
	var tl = mainProcessListEn.length;
	var option ='';
//	$(obj).empty();
	for(var i=0;i<tl;i++){
		option = '<option value='+mainProcessListEn[i].name+'>'+mainProcessListEn[i].name+'</option>';
		$(obj).append(option);
	}
}



//获取工艺索引
function getProcessIndex(str){
	
	var tl = mainProcessListEn.length;
	var option ='';
	for(var i=0;i<tl;i++){
		if(mainProcessListEn[i].name == str){
			return i;
		}
	}
}

