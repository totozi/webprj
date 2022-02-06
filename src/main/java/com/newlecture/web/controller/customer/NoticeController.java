package com.newlecture.web.controller.customer;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.newlecture.web.entity.NoticeView;
import com.newlecture.web.service.NoticeService;

@Controller
@RequestMapping("/customer/notice/")
public class NoticeController {
	@Autowired
	NoticeService noticeService;
	
	@RequestMapping("list")
	public String noticeList(@RequestParam(value="p", defaultValue = "1") Integer page, Model model) {
//		String p = request.getParameter("p");
		System.out.println(page);
		List<NoticeView> noticeList = noticeService.getNoticeViewListForAdmin("1","2",1);
		model.addAttribute("noticeList", noticeList);
		return "notice.list";
	}
	@RequestMapping("detail")
	public ModelAndView noticeDetail() {
		
		ModelAndView mv = new ModelAndView("notice.detail");
		
		return mv;
	}
}
