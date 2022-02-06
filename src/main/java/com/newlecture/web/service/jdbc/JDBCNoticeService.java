package com.newlecture.web.service.jdbc;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import com.newlecture.web.entity.Notice;
import com.newlecture.web.entity.NoticeView;
import com.newlecture.web.service.NoticeService;


@Service
public class JDBCNoticeService implements NoticeService{
	
	@Autowired
	private DataSource dataSource;
	
	public int removeNoticeAll(int[] ids) {

		return 0;
	}

	public int pubNoticeAll(int[] oids, int[] cids) {
		List<String> oidsList = new ArrayList<>();
		List<String> cidsList = new ArrayList<>();
		
		for(int i=0;i<oids.length;i++) {
			oidsList.add(String.valueOf(oids[i]));
		}
		for(int i=0;i<oids.length;i++) {
			cidsList.add(String.valueOf(oids[i]));
		}
		
		return pubNoticeAll(oidsList, cidsList);
	}
	public int pubNoticeAll(List<String> oidsList, List<String> cidsList) {
		String oidsCSV = String.join(",", oidsList);
		String cidsCSV = String.join(",", cidsList);
		return pubNoticeAll(oidsCSV, cidsCSV);
	}
	public int pubNoticeAll(String oidsCSV, String cidsCSV) {
		int result = -1;
		String sqlOpen=String.format("UPDATE NOTICE SET PUB=1 WHERE ID IN(%s)",oidsCSV);
		String sqlClose=String.format("UPDATE NOTICE SET PUB=0 WHERE ID IN(%s)",cidsCSV);
		try {
			Connection conn = dataSource.getConnection();
			Statement statementOpen = conn.createStatement();
			result = statementOpen.executeUpdate(sqlOpen);
			
			Statement statementClose = conn.createStatement();
			result += statementClose.executeUpdate(sqlClose);
			
			statementOpen.close();
			statementClose.close();
			conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return result;
	}
	

	public int deleteNoticeAll(int[] ids) {
		int result = -1;

		String params = "";
		for (int i = 0; i < ids.length; i++) {
			params += ids[i];
			if (i <= ids.length - 1)
				params += ",";
		}
		
		String sql = "DELETE NOTICE WHERE ID (" + params + ")";

		try {
			Connection conn = dataSource.getConnection();
			Statement statement = conn.createStatement();
			result = statement.executeUpdate(sql);
			
			statement.close();
			conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}

		return result;
	}

	public int insertNotice(Notice notice) {
		int result = -1;

		
		String sql = "INSERT INTO NOTICE VALUES("
				+ "NOTICE_SEQ.NEXTVAL, ? , ? , ? ,SYSDATE,0,?,?)";
		
		
		
		try {
			Connection conn = dataSource.getConnection();
			PreparedStatement statement = conn.prepareStatement(sql);
			statement.setString(1, notice.getTitle());
			statement.setString(2, notice.getWriterId());
			statement.setString(3, notice.getContent());
			statement.setString(4, notice.getFiles());
			statement.setBoolean(5, notice.getPub());
			result = statement.executeUpdate();
			statement.close();
			conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}

		return result;
	}

	public int deleteNotice(int id) {
		return 0;
	}

	public int updateNotice(Notice notice) {
		return 0;
	}

	public List<Notice> getNoticeNewestList() {
		return null;
	}

	public List<NoticeView> getNoticeViewList() {

		return getNoticeViewList("title", "", 1);
	}

	public List<NoticeView> getNoticeViewList(int page) {

		return getNoticeViewList("title", "", page);
	}

	public List<NoticeView> getNoticeViewList(String field, String query, int page) {
		List<NoticeView> noticeList = new ArrayList<NoticeView>();

		if (field.equals(""))
			field = "TITLE";

		String sql = "SELECT * FROM( " + "SELECT ROWNUM RNUM, N.* FROM " + "(SELECT * FROM NOTICE_VIEW WHERE " + field
				+ " LIKE ? AND PUB=1 ORDER BY ID DESC) N " + ") " + "WHERE RNUM BETWEEN ? AND ? ";

		int startPage = 1 + (page - 1) * 5;
		int endPage = page * 5;

		try {
			Connection conn = dataSource.getConnection();
			PreparedStatement statement = conn.prepareStatement(sql);
			statement.setString(1, "%" + query + "%");
			statement.setInt(2, startPage);
			statement.setInt(3, endPage);
			ResultSet resultSet = statement.executeQuery();
			while (resultSet.next()) {
				int id = resultSet.getInt("ID");
				String title = resultSet.getString("TITLE");
				Date regdate = resultSet.getDate("REGDATE");
				String writerId = resultSet.getString("WRITER_ID");
				int hit = resultSet.getInt("HIT");
				String files = resultSet.getString("FILES");
				int cmtCount = resultSet.getInt("CMT_C");
				boolean pub = resultSet.getBoolean("PUB");

				NoticeView notice = new NoticeView(id, title, regdate, writerId, hit, files, pub, cmtCount);
				noticeList.add(notice);

			}
			resultSet.close();
			statement.close();
			conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}

		return noticeList;
	}
	public List<NoticeView> getNoticeViewListForAdmin(String field, String query, int page) {
		List<NoticeView> noticeList = new ArrayList<NoticeView>();
		
		if (field.equals(""))
			field = "TITLE";
		
		String sql = "SELECT * FROM( " + "SELECT ROWNUM RNUM, N.* FROM " + "(SELECT * FROM NOTICE_VIEW WHERE " + field
				+ " LIKE ? ORDER BY ID DESC) N " + ") " + "WHERE RNUM BETWEEN ? AND ? ";
		sql = "SELECT * FROM NOTICE_VIEW order by id";
		int startPage = 1 + (page - 1) * 5;
		int endPage = page * 5;
		
		JdbcTemplate template = new JdbcTemplate();
		template.setDataSource(dataSource);
		noticeList = template.query(sql, new BeanPropertyRowMapper(NoticeView.class));
		
		return noticeList;
	}

	public int getNoticeCount() {

		return 0;
	}

	public int getNoticeCount(String field, String query) {
		int count = 0;
		String sql = "SELECT COUNT(ID) AS COUNT FROM( " + "    SELECT ROW_NUMBER() OVER (ORDER BY REGDATE DESC) NUM, "
				+ "    NOTICE.* FROM NOTICE WHERE " + field + " LIKE ? " + " AND PUB=1) ";

		try {
			Connection conn = dataSource.getConnection();
			PreparedStatement statement = conn.prepareStatement(sql);
			statement.setString(1, "%" + query + "%");
			ResultSet resultSet = statement.executeQuery();
			resultSet.next();
			count = resultSet.getInt("COUNT");
			resultSet.close();
			statement.close();
			conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}

		return count;
	}
	public int getNoticeCountForAdmin(String field, String query) {
		int count = 0;
		String sql = "SELECT COUNT(ID) AS COUNT FROM( " + "    SELECT ROW_NUMBER() OVER (ORDER BY REGDATE DESC) NUM, "
				+ "    NOTICE.* FROM NOTICE WHERE " + field + " LIKE ? " + ") ";
		
		try {
			Connection conn = dataSource.getConnection();
			PreparedStatement statement = conn.prepareStatement(sql);
			statement.setString(1, "%" + query + "%");
			ResultSet resultSet = statement.executeQuery();
			resultSet.next();
			count = resultSet.getInt("COUNT");
			resultSet.close();
			statement.close();
			conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return count;
	}

	public Notice getNotice(int id) {
		Notice notice = null;
		String sql = "SELECT * FROM NOTICE WHERE ID=?";

		try {
			Connection conn = dataSource.getConnection();
			PreparedStatement statement = conn.prepareStatement(sql);
			statement.setInt(1, id);
			ResultSet resultSet = statement.executeQuery();

			if (resultSet.next()) {
				int nid = resultSet.getInt("ID");
				String title = resultSet.getString("TITLE");
				Date regdate = resultSet.getDate("REGDATE");
				String writerId = resultSet.getString("WRITER_ID");
				int hit = resultSet.getInt("HIT");
				String files = resultSet.getString("FILES");
				String content = resultSet.getString("CONTENT");
				boolean pub = resultSet.getBoolean("PUB");
				notice = new Notice(nid, title, regdate, writerId, hit, files, content, pub);
			}

			resultSet.close();
			statement.close();
			conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}

		return notice;
	}

	public Notice getNextNotice(int id) {
		Notice notice = null;
		String sql = "SELECT ID FROM NOTICE " + " WHERE ID > (SELECT ID FROM NOTICE WHERE ID=?) " + " AND ROWNUM=1";

		try {
			Connection conn = dataSource.getConnection();
			PreparedStatement statement = conn.prepareStatement(sql);
			statement.setInt(1, id);
			ResultSet resultSet = statement.executeQuery();

			if (resultSet.next()) {
				int nid = resultSet.getInt("ID");
				String title = resultSet.getString("TITLE");
				Date regdate = resultSet.getDate("REGDATE");
				String writerId = resultSet.getString("WRITER_ID");
				int hit = resultSet.getInt("HIT");
				String files = resultSet.getString("FILES");
				String content = resultSet.getString("CONTENT");
				boolean pub = resultSet.getBoolean("PUB");
				notice = new Notice(nid, title, regdate, writerId, hit, files, content, pub);
			}

			resultSet.close();
			statement.close();
			conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}

		return notice;
	}

	public Notice getPrevNotice(int id) {
		Notice notice = null;
		String sql = "SELECT MAX(ID) FROM NOTICE " + " WHERE ID < (SELECT ID FROM NOTICE WHERE ID=?)";

		try {
			Connection conn = dataSource.getConnection();
			PreparedStatement statement = conn.prepareStatement(sql);
			statement.setInt(1, id);
			ResultSet resultSet = statement.executeQuery();

			if (resultSet.next()) {
				int nid = resultSet.getInt("ID");
				String title = resultSet.getString("TITLE");
				Date regdate = resultSet.getDate("REGDATE");
				String writerId = resultSet.getString("WRITER_ID");
				int hit = resultSet.getInt("HIT");
				String files = resultSet.getString("FILES");
				String content = resultSet.getString("CONTENT");
				boolean pub = resultSet.getBoolean("PUB");
				notice = new Notice(nid, title, regdate, writerId, hit, files, content, pub);
			}

			resultSet.close();
			statement.close();
			conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}

		return notice;
	}
}
