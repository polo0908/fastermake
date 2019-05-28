package com.cbt;

import com.cbt.service.QuoteInfoService;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.web.context.WebApplicationContext;

@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration(locations = {"classpath:spring-mybatis.xml","classpath:spring-web.xml","classpath:spring-service.xml"})
public class TestSyncQuote {

	MockHttpServletRequest request;
	MockHttpServletResponse response;
	@Autowired
	protected WebApplicationContext wac;
	@Autowired
	private QuoteInfoService quoteInfoService;


	@Before
	public void setUp() throws Exception {
		request=new MockHttpServletRequest();
		response=new MockHttpServletResponse();
	}

	@After
	public void tearDown() throws Exception {
	}

	/**
	 * 测试同步状态
	 */
	@Test
	public void testSendMail() {
		quoteInfoService.updateQuoteStatusBatch("SHS18871,SHS19024,SHS19389","8");

	}
	
	
	
	
}
