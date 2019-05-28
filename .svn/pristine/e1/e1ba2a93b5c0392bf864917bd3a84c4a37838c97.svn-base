package com.cbt.jdbc;

import java.sql.*;
import java.util.List;

import com.cbt.entity.DingBean.DingTalkMileStone;
import com.cbt.util.DBTaskHelper;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class AddMileStoneJdbc {

	private Logger log = LoggerFactory.getLogger(this.getClass());
	public void sendRequest(List<DingTalkMileStone> milestones) {
		try {		 	
			
			new Thread(new AddMileStoneJdbc().new SendHttp(milestones)).start();
			
		} catch (Exception e) {
			e.printStackTrace();
			log.error("发送失败", e);
		}
	}
	class SendHttp implements Runnable{
		
		private List<DingTalkMileStone> milestones;
		
		SendHttp(List<DingTalkMileStone> milestones){
			this.milestones = milestones;
		}

		@Override
		public void run() {		
			Connection conn = DBTaskHelper.getConnection();
			String sql = "insert into dingtalk_milestone (dingTalkId, mileStone_name,mileStone_date, project_no,process_instance_id) values (?,?,?,?,?);";
	 		try{
	 			//如果是更新，先删除之前的数据
	 			if(milestones!=null && milestones.size()>0){
					String delSql = "delete from dingtalk_milestone where process_instance_id = \""+milestones.get(0).getProcessInstanceId()+"\"";
					Statement statement = conn.createStatement();
					statement.execute(delSql);
				}

				PreparedStatement psmt =conn.prepareStatement(sql,ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);
	 			for(DingTalkMileStone mileStone:milestones){
	 				if(StringUtils.isNotBlank(mileStone.getMilestoneName()) && mileStone.getMilestoneDate()!=null){
						psmt.setString(1,mileStone.getDingtalkid());
						psmt.setString(2,mileStone.getMilestoneName());
						psmt.setDate(3,new java.sql.Date(mileStone.getMilestoneDate().getTime()));
						psmt.setString(4,mileStone.getProjectNo());
						psmt.setString(5,mileStone.getProcessInstanceId());
						psmt.addBatch();
					}
				}
				psmt.executeBatch();
	 		} catch (SQLException e) {
	 			e.printStackTrace();
	 			log.error("<<<<<<<<<<<<<<<<<<AddMileStoneJdbc>>>>>>>>>>>>>>>>>>>保存里程碑数据失败",e.getMessage());
	 		}	finally {
				DBTaskHelper.closeConnection(conn);
	 		} 
		}	
	}
}