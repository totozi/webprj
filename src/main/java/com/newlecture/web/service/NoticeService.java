package com.newlecture.web.service;

import static com.newlecture.web.jdbc.JDBCTemplete.getConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.newlecture.web.entity.Notice;
import com.newlecture.web.entity.NoticeView;

public interface NoticeService {

	int removeNoticeAll(int[] ids);

	int pubNoticeAll(int[] oids, int[] cids);
	int pubNoticeAll(List<String> oidsList, List<String> cidsList);
	int pubNoticeAll(String oidsCSV, String cidsCSV);
	int deleteNoticeAll(int[] ids);
	int insertNotice(Notice notice);
	int deleteNotice(int id);
	int updateNotice(Notice notice);
	List<Notice> getNoticeNewestList();
	List<NoticeView> getNoticeViewList();
	List<NoticeView> getNoticeViewList(int page);
	List<NoticeView> getNoticeViewList(String field, String query, int page);
	List<NoticeView> getNoticeViewListForAdmin(String field, String query, int page);
	int getNoticeCount();
	int getNoticeCount(String field, String query);
	int getNoticeCountForAdmin(String field, String query);
	Notice getNotice(int id);
	Notice getNextNotice(int id);
	Notice getPrevNotice(int id);

}
