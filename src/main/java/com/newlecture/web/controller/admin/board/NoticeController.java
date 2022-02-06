package com.newlecture.web.controller.admin.board;

import java.io.File;
import java.io.IOException;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

@Controller("adminNoticeContoller")
@RequestMapping("/admin/board/notice")
public class NoticeController {

	@Autowired
	private ServletContext ctx;

	@RequestMapping("list")
	public String list() {
		return "admin.board.notice.list";
	}

	@GetMapping("reg")
	public String reg() {
		return "admin.board.notice.reg";
	}
	
	@PostMapping("reg")
	public String reg(String title, String content, MultipartFile[] files, String category, String food,
			HttpServletRequest request) {
		System.out.println(food);
		for (MultipartFile file : files) {
			String fileName = file.getOriginalFilename();

			System.out.println("fileName : " + fileName);
			System.out.println("fileSize : " + file.getSize());

//			ServletContext ctx = request.getServletContext();
			String webPath = "/static/upload";
			String realPath = ctx.getRealPath(webPath);
			File savePath = new File(realPath);
			if (!(savePath.exists()))
				savePath.mkdirs();
			realPath += File.separator + fileName;
			File saveFile = new File(realPath);
			try {
				file.transferTo(saveFile);
			} catch (IllegalStateException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		return "admin.board.notice.list";
	}

	@RequestMapping("edit")
	public String edit() {
		return "admin.board.notice.edit";
	}

	@RequestMapping("del")
	public String del() {
		return "admin.board.notice.del";
	}

}
