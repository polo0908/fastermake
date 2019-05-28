package com.cbt.service.impl;

import java.util.List;
import com.cbt.dao.NoteMessageEnMapper;
import com.cbt.dao.QuoteMessageEnMapper;
import com.cbt.translate.TranslateExecutor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cbt.dao.NoteMessageMapper;
import com.cbt.dao.QuoteMessageMapper;
import com.cbt.entity.DialogueIds;
import com.cbt.entity.NoteMessage;
import com.cbt.entity.QuoteMessage;
import com.cbt.service.QuoteMessageService;



@Service
public class QuoteMessageServiceImpl implements QuoteMessageService {

	@Autowired
	private QuoteMessageMapper quoteMessageMapper;
	@Autowired
	private QuoteMessageEnMapper quoteMessageEnMapper;
	@Autowired
	private NoteMessageMapper noteMessageMapper;
	@Autowired
	private NoteMessageEnMapper noteMessageEnMapper;


	@Override
	public List<QuoteMessage> queryMessageByFactoryId(String factoryId,Integer orderId) {        
		return quoteMessageMapper.queryMessageByFactoryId(factoryId,orderId);
	}

	
	@Transactional
	@Override
	public int insert(QuoteMessage record,NoteMessage note) throws Exception {
		
		DialogueIds did = new DialogueIds();
		noteMessageMapper.returnDialogueId(did);
		int dialogueId = did.getId();
		note.setDialogueId(dialogueId);
		noteMessageMapper.insertSelective(note);
		quoteMessageMapper.insert(record);
		note = TranslateExecutor.translateMessage(note);
		record = TranslateExecutor.translateQuoteMessage(record);
		noteMessageEnMapper.insert(note);
		return quoteMessageEnMapper.insert(record);
	}

	@Override
	public QuoteMessage selectByPrimaryKey(Integer id) {
		return quoteMessageMapper.selectByPrimaryKey(id);
	}

	@Override
	public List<QuoteMessage> queryMessageByCsgOrderId(String factoryId,String supplierId, Integer orderId, String csgOrderId) {
		return quoteMessageMapper.queryMessageByCsgOrderId(factoryId,supplierId,orderId,csgOrderId);
	}

}
