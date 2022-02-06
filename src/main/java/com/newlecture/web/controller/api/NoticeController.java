package com.newlecture.web.controller.api;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.newlecture.web.entity.NoticeView;
import com.newlecture.web.service.NoticeService;

@RestController("apiNoticeController")
@RequestMapping("/api/notice/")
public class NoticeController {

	@Autowired
	private NoticeService service;
	
	@RequestMapping("list")
	public NoticeView list() {
		
		List<NoticeView> noticeList = service.getNoticeViewList();
		NoticeView notice = noticeList.get(0);
		return notice;
	}
}
