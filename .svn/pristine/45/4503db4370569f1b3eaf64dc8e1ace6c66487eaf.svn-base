package com.cbt.service.impl;

import java.util.List;

import javax.annotation.Resource;

import com.cbt.dao.*;
import com.cbt.translate.TranslateExecutor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cbt.entity.DialogueIds;
import com.cbt.entity.NoteMessage;
import com.cbt.entity.QuoteInfo;
import com.cbt.entity.QuoteMessage;
import com.cbt.enums.MessageTypeEnum;
import com.cbt.enums.PermissionEnum;
import com.cbt.service.NoteMessageService;
import com.cbt.util.DateFormat;

@Service
public class NoteMessageServiceImpl implements
		NoteMessageService {
	
	@Autowired
	private NoteMessageMapper mapper;
	@Autowired
	private QuoteMessageMapper quoteMessageMapper;
	@Autowired
	private QuoteInfoMapper quoteInfoMapper;
	@Autowired
	private QuoteMessageEnMapper quoteMessageEnMapper;
	@Autowired
	private NoteMessageEnMapper noteMessageEnMapper;


	@Override
	public int insert(NoteMessage record) {
	
		return mapper.insert(record);
	}
	
	
	

	@Override
	public NoteMessage selectByPrimaryKey(Integer id) {
		return mapper.selectByPrimaryKey(id);
	}

	@Override
	public NoteMessage selectByPrimaryKeyEn(Integer id) {
		return noteMessageEnMapper.selectByPrimaryKey(id);
	}


	@Override
	@Transactional
	public int insertSelective(NoteMessage record) throws Exception {
		
		if(record.getMessageType()!=null&&record.getMessageType() == MessageTypeEnum.QUOTE_MESSAGE.getCode()){
			  QuoteMessage quoteMessage = new QuoteMessage();
			  quoteMessage.setFactoryId(record.getSendId());
			  quoteMessage.setMessageDetails(record.getMessageDetails());
			  quoteMessage.setOrderId(record.getOrderId());
			  quoteMessage.setFilePath(record.getFilePath());
			  quoteMessage.setFileName(record.getFileName());
			  quoteMessage.setSendTime(DateFormat.format());
			  quoteMessage.setReplyFactoryId(record.getReceiverId());
			  QuoteInfo quoteInfo = quoteInfoMapper.queryByOrderId(record.getOrderId());
			  if(quoteInfo != null && !"".equals(quoteInfo)){
			     if(record.getSendId().equals(quoteInfo.getCustomerId())){
			    	 quoteMessage.setReplyStatus(0);  
			     }else{
			    	 quoteMessage.setReplyStatus(1); 
			     }
			 }
			  quoteMessageMapper.insertSelective(quoteMessage);
			  quoteMessage = TranslateExecutor.translateQuoteMessage(quoteMessage);
			  quoteMessageEnMapper.insertSelective(quoteMessage);
		}


		mapper.insertSelective(record);
		//翻译后保存
		record = TranslateExecutor.translateMessage(record);

		return noteMessageEnMapper.insertSelective(record);
	}




	@Override
	public int insertNewDialogue(NoteMessage record) throws Exception {
		
		DialogueIds did = new DialogueIds();
		mapper.returnDialogueId(did);
		int dialogueId = did.getId();
		record.setDialogueId(dialogueId);
		mapper.insertSelective(record);
		//翻译后保存
		record = TranslateExecutor.translateMessage(record);

		return noteMessageEnMapper.insertSelective(record);
		
		
	}




	@Override
	public List<NoteMessage> queryListAdmin(String factoryId, Integer start,
			Integer pageSize,boolean checkRead,boolean checkSend,String selectKey,Integer messageType) {
		return mapper.queryListAdmin(factoryId, start, pageSize,checkRead,checkSend,selectKey, messageType);
	}

	@Override
	public List<NoteMessage> queryListAdminEn(String factoryId, Integer start, Integer pageSize, boolean checkRead, boolean checkSend, String selectKey, Integer messageType) {
		return noteMessageEnMapper.queryListAdmin(factoryId, start, pageSize,checkRead,checkSend,selectKey, messageType);
	}


	@Override
	public int totalOrderAdmin(String factoryId,boolean checkRead,boolean checkSend,String selectKey,Integer messageType) {
		return mapper.totalOrderAdmin(factoryId,checkRead,checkSend,selectKey,messageType);
	}




	@Override
	public List<NoteMessage> queryDetail(Integer dialogueId) {
		
		return mapper.selectByDialogueId(dialogueId);
	}

	@Override
	public List<NoteMessage> queryDetailEn(Integer dialogueId) {
		return noteMessageEnMapper.selectByDialogueId(dialogueId);
	}

	@Override
	public int updateMessageData(NoteMessage noteMessage){
        noteMessageEnMapper.updateByPrimaryKeySelective(noteMessage);
		return mapper.updateByPrimaryKeySelective(noteMessage);
	}




	@Override
	public List<NoteMessage> queryList(String factoryId, Integer start,
			Integer pageSize, boolean checkRead, boolean checkSend,
			String selectKey, Integer messageType, Integer factoryUserId) {
		return mapper.queryList(factoryId, start, pageSize, checkRead, checkSend, selectKey, messageType, factoryUserId);
	}

	@Override
	public List<NoteMessage> queryListEn(String factoryId, Integer start, Integer pageSize, boolean checkRead, boolean checkSend, String selectKey, Integer messageType, Integer factoryUserId) {
		return noteMessageEnMapper.queryList(factoryId, start, pageSize, checkRead, checkSend, selectKey, messageType, factoryUserId);
	}


	@Override
	public int totalOrder(String factoryId, boolean checkRead,
			boolean checkSend, String selectKey, Integer messageType,
			Integer factoryUserId) {
		return mapper.totalOrder(factoryId, checkRead, checkSend, selectKey, messageType, factoryUserId);
	}




	@Override
	public int updateBatch(List<?> records, int permission,String factoryId,Integer factoryUserId,int isRead) {
		int updateType = 0;
		if(records == null || records.size() == 0){
			if(permission == PermissionEnum.ADMIN.getCode()){
				List<?> ids = mapper.queryIdsByFactoryIdAdmin(factoryId);
				updateType = mapper.updateBatch(ids,isRead);
				noteMessageEnMapper.updateBatch(ids,isRead);
			}else{
				List<?> ids = mapper.queryIdsByFactoryId(factoryId, factoryUserId);
				updateType = mapper.updateBatch(ids,isRead);
				noteMessageEnMapper.updateBatch(ids,isRead);
			}			
		}else{
			updateType = mapper.updateBatch(records,isRead);
			noteMessageEnMapper.updateBatch(records,isRead);
		}
		return updateType;
	}




	@Override
	public List<?> queryByBackstage() {
		return mapper.queryByBackstage();
	}

}
