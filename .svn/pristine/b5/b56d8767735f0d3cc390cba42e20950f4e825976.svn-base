package com.cbt.service.impl;

import java.util.List;
import javax.servlet.http.HttpServletRequest;
import com.cbt.dao.NoteMessageEnMapper;
import com.cbt.entity.*;
import com.cbt.enums.ProjectStageEnum;
import com.cbt.translate.TranslateExecutor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.cbt.dao.NoteMessageMapper;
import com.cbt.dao.QuoteInfoMapper;
import com.cbt.dao.QuoteWeeklyReportMapper;
import com.cbt.enums.MessageTypeEnum;
import com.cbt.service.QuoteWeeklyReportService;
import com.cbt.util.DateFormat;
import com.cbt.util.GetServerPathUtil;
import com.cbt.util.WebCookie;

@Service
public class QuoteWeeklyReportServiceImpl implements QuoteWeeklyReportService {
 
	@Autowired
	private QuoteWeeklyReportMapper quoteWeeklyReportMapper;
	
	@Autowired
	private QuoteInfoMapper quoteInfoMapper;
	
	@Autowired
	private NoteMessageMapper noteMessageMapper;

	@Autowired
	private NoteMessageEnMapper noteMessageEnMapper;

	private static final String SELF_FACTORY_ID = "0";

	
	@Override
	public List<QuoteWeeklyReport> queryByOrderId(Integer orderId) {
		return quoteWeeklyReportMapper.queryByOrderId(orderId);
	}

	@Override
	public QuoteWeeklyReport selectByPrimaryKey(Integer id) {
		return quoteWeeklyReportMapper.selectByPrimaryKey(id);
	}

	@Override
	public int updateByPrimaryKeySelective(QuoteWeeklyReport record) {
		return quoteWeeklyReportMapper.updateByPrimaryKeySelective(record);
	}

	@Override
	public List<QuoteWeeklyReport> queryByOrderIdAndDate(Integer orderId,
			String uploadDate) {
		return quoteWeeklyReportMapper.queryByOrderIdAndDate(orderId, uploadDate);
	}

	@Override
	public int deleteByPrimaryKey(Integer id) {
		//同步redis
		//删除，重新插入
//		SysnReportRedis redisTask = new SysnReportRedis();
//		redisTask.pushReports(redisTask);
		return quoteWeeklyReportMapper.deleteByPrimaryKey(id);
	}

	
	@Transactional
	@Override
	public int insertSelective(HttpServletRequest request,QuoteWeeklyReport record,QuoteReportType report) {
		  QuoteInfo quoteInfo = quoteInfoMapper.queryByOrderId(record.getOrderId());
		  NoteMessage note = new NoteMessage();
		  note.setReceiverId(quoteInfo.getCustomerId());
		  note.setSendId(WebCookie.getFactoryId(request)); 
		  note.setOrderId(quoteInfo.getOrderId());

		  if(report.getProjectStage() == ProjectStageEnum.MATERIAL_REPORT.getCode()){
			  note.setMessageTitle((record.getOrderId() == null ? "" : record.getOrderId()) +(report.getCsgOrderId() == null ? "" : "("+report.getCsgOrderId()+")")+"询盘有新的材质报告上传");
			  note.setMessageDetails((record.getOrderId() == null ? "" : record.getOrderId())+"询盘有新的材质报告上传");
		  }else if(report.getProjectStage() == ProjectStageEnum.PROGRESS_REPORT.getCode()){
			  note.setMessageTitle((record.getOrderId() == null ? "" : record.getOrderId())+(report.getCsgOrderId() == null ? "" : "("+report.getCsgOrderId()+")")+"询盘有新的进度报告上传");
			  note.setMessageDetails((record.getOrderId() == null ? "" : record.getOrderId())+"询盘有新的进度报告上传");
		  }else if(report.getProjectStage() == ProjectStageEnum.QUANLITY_REPORT.getCode()){
			  note.setMessageTitle((record.getOrderId() == null ? "" : record.getOrderId())+(report.getCsgOrderId() == null ? "" : "("+report.getCsgOrderId()+")")+"询盘有新的出货质量报告上传");
			  note.setMessageDetails((record.getOrderId() == null ? "" : record.getOrderId())+"询盘有新的出货质量报告上传");
		  }else if(report.getProjectStage() == ProjectStageEnum.SHIPPING_REPORT.getCode()){
			  note.setMessageTitle((record.getOrderId() == null ? "" : record.getOrderId())+(report.getCsgOrderId() == null ? "" : "("+report.getCsgOrderId()+")")+"询盘有新的出运报告上传");
			  note.setMessageDetails((record.getOrderId() == null ? "" : record.getOrderId())+"询盘有新的出运报告上传");
		  }

		  note.setMessageType(MessageTypeEnum.WEEKLY_REPORT.getCode());
		  note.setUrl(GetServerPathUtil.getServerPath()+"/m-zh/big_good_self.html?factoryId="+WebCookie.getFactoryId(request)+"&orderId="+(record.getOrderId() == null?"":record.getOrderId())+"&csgOrderId="+(report.getCsgOrderId() == null?"":report.getCsgOrderId()));
		  note.setCreateTime(DateFormat.format());
		  
		  DialogueIds did = new DialogueIds();
		  noteMessageMapper.returnDialogueId(did);
		  int dialogueId = did.getId();
		  note.setDialogueId(dialogueId);
		  noteMessageMapper.insertSelective(note);


		//同步redis
		//删除，重新插入
//		SysnReportRedis redisTask = new SysnReportRedis();
//		redisTask.pushReports(redisTask);
		  
		return quoteWeeklyReportMapper.insertSelective(record);
	}

	
	@Transactional
	@Override
	public void insertPhotosBatch(HttpServletRequest request,List<QuoteWeeklyReport> reports) throws Exception {

		  String factoryId = WebCookie.getFactoryId(request);
		  String csgOrderId = "";
		  String customerId = "";
		  if(reports!=null && reports.size()>0){
			  csgOrderId = reports.get(0).getCsgOrderId();
		  }
		  QuoteInfo quoteInfo = quoteInfoMapper.queryByCgsOrderId(csgOrderId);
          if(quoteInfo!=null){
			  customerId = quoteInfo.getCustomerId();
		  }else{
			  customerId = SELF_FACTORY_ID;
		  }
		  NoteMessage note = new NoteMessage();
		  note.setReceiverId(customerId);
		  note.setSendId(WebCookie.getFactoryId(request));
		  note.setMessageTitle((quoteInfo == null ? "" : quoteInfo.getOrderId()) +(csgOrderId == null ? "" : "("+csgOrderId+")")+"询盘有新的周报上传");
		  note.setMessageDetails((quoteInfo == null ? "" :quoteInfo.getOrderId())+(csgOrderId == null ? "" : "("+csgOrderId+")")+"询盘有新的周报上传");
		  note.setOrderId(quoteInfo == null ? null :quoteInfo.getOrderId());
		  note.setMessageType(MessageTypeEnum.WEEKLY_REPORT.getCode());
		  note.setUrl(GetServerPathUtil.getServerPath()+"/report/reportListOther?csgOrderId="+csgOrderId+"&supplierId="+factoryId);
		  note.setCreateTime(DateFormat.format());

		  DialogueIds did = new DialogueIds();
		  noteMessageMapper.returnDialogueId(did);
		  int dialogueId = did.getId();
		  note.setDialogueId(dialogueId);
		  noteMessageMapper.insertSelective(note);
		  note = TranslateExecutor.translateMessage(note);
		  noteMessageEnMapper.insertSelective(note);
		  
		quoteWeeklyReportMapper.insertPhotosBatch(reports);

		//同步redis
		//删除，重新插入
//		SysnReportRedis redisTask = new SysnReportRedis();
//		redisTask.pushReports(redisTask);
	}

	@Override
	public int updateByPrimaryKey(QuoteWeeklyReport record) {
		return quoteWeeklyReportMapper.updateByPrimaryKey(record);
	}

	@Override
	public List<QuoteWeeklyReport> queryByCsgOrderIdAndType(String csgOrderId, Integer type,String factoryId) {
		return quoteWeeklyReportMapper.queryByCsgOrderIdAndType(csgOrderId,type,factoryId);
	}

	@Override
	public String queryMaxUploadDate(String csgOrderId, String factoryId) {
		return quoteWeeklyReportMapper.queryMaxUploadDate(csgOrderId,factoryId);
	}

	@Override
	public List<QuoteWeeklyReport> selectAll() {
		return quoteWeeklyReportMapper.selectAll();
	}

	@Override
	public List<QuoteWeeklyReport> queryByOrderIdAndSupplier(Integer orderId) {
		return quoteWeeklyReportMapper.queryByOrderIdAndSupplier(orderId);
	}

}
