/* 选择国家对应国旗 */
	var country_zh = {
		"USA":"美国",
		"Canada":"加拿大",
		"France":"法国",
		"Germany":"德国",
		"Netherlands":"荷兰",
		"Israel":"以色列",
		"Mexico":"墨西哥",
		"Australia":"澳大利亚",
		"Italy":"意大利",
		"Switzerland":"瑞士",
		"Finland":"芬兰",
		"Sweden":"瑞典",
		"UK":"英国",
		"Argentina":"阿根廷",
		"Other":"美国"
	}
	
	//如果国家显示为Other或者空值，则统一美国显示
	function getCountryZh(countryEn){
		var country = country_zh[countryEn];
		if(country == null || country == ''){
			country = country_zh["Other"];
		}
		return country;
	}