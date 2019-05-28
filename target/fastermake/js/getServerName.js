/* 选择的不同域名 */
//Online
	var server = {
		"en":"https://www.quickpart.cn",
		"zh":"https://www.kuaizhizao.cn"
	}
//Dev	
// 	var server = {
// 			"en":"http://192.168.1.151:8080",
// 			"zh":"http://192.168.1.151:8080"
// 		}

/*Test*/
// 	var server = {
// 	"en":"http://112.64.174.34:8083",
// 	"zh":"http://112.64.174.34:8083"
// }
	
	//根据国家语言选择域名
	function getServerName(lan){
		var server_name = server[lan];
		if(!lan){
			server_name = "https://www.kuaizhizao.cn";
		}
		return server_name;
	}