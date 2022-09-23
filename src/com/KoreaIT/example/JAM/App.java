package com.KoreaIT.example.JAM;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import com.KoreaIT.example.JAM.util.DBUtil;
import com.KoreaIT.example.JAM.util.SecSql;

public class App {
	public void run() {
		Scanner sc = new Scanner(System.in);

		while (true) {
			System.out.printf("명령어: ");
			String cmd = sc.nextLine().trim();

			// DB연결
			Connection conn = null;

			try {
				Class.forName("com.mysql.jdbc.Driver");

			} catch (ClassNotFoundException e) { //ClassNotFoundException : Class 클래스가 없는 경우 예외처리. jdk를 깔면 보통 있다. 
				System.out.println("예외 : 클래스가 없습니다.");
				System.out.println("프로그램을 종료합니다.");
				break;
			}

			String url = "jdbc:mysql://127.0.0.1:3306/article_manager?useUnicode=true&characterEncoding=utf8&autoReconnect=true&serverTimezone=Asia/Seoul&useOldAliasMetadataBehavior=true&zeroDateTimeNehavior=convertToNull";

			try {
				conn = DriverManager.getConnection(url, "root", ""); // (url, id, pw) 조건의 db에 커넥트.

				int actionResult = doAction(conn, sc, cmd);

				if (actionResult == -1) {
					break;
				}

			} catch (SQLException e) {  // db에 커넥트 실패한 경우 try-catch 예외처리로 그냥 종료시킨다.
				System.out.println("@@@@에러@@@@: " + e);
				break;
			} finally {
				try {
					if (conn != null && !conn.isClosed()) {
						conn.close();
					}
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
	}

	private int doAction(Connection conn, Scanner sc, String cmd) {

		if (cmd.equals("article write")) {
			System.out.println("== 게시물 작성 ==");

			System.out.printf("제목 : ");
			String title = sc.nextLine();
			System.out.printf("내용 : ");
			String body = sc.nextLine();
			
			SecSql sql = new SecSql();
			
			sql.append("INSERT INTO article");
			sql.append(" SET regDate = NOW()");
			sql.append(", updateDate = NOW()");
			sql.append(", title = ?", title);
			sql.append(", `body` = ?", body);
			
			int id = DBUtil.insert(conn, sql);
			
			System.out.printf("%d번 게시물이 생성되었습니다.\n", id);
			

//			PreparedStatement pstmt = null;
//
//			try {
//				String sql = "INSERT INTO article";
//				sql += " SET regDate = NOW()";
//				sql += ", updateDate = NOW()";
//				sql += ", title = '" + title + "'";
//				sql += ", `body` = '" + body + "';";
//
//				System.out.println(sql);
//
//				pstmt = conn.prepareStatement(sql); // 커넥트된 db의 준비된 서술(sql)?
//
//				pstmt.executeUpdate(); // pstmt.executeUpdate : pstmt에 담겨져있는 내용을 db에 업데이트해라.
//
//			} catch (SQLException e) {
//				System.out.println("@@@@에러@@@@: " + e);
//			} finally {
//				try {
//					if (pstmt != null && !pstmt.isClosed()) {
//						pstmt.close();
//					}
//				} catch (SQLException e) {
//					e.printStackTrace();
//				}
//			}

		} else if (cmd.startsWith("article modify ")) {
			int id = Integer.parseInt(cmd.split(" ")[2]);
			System.out.printf("== %d번 게시물 수정 ==\n", id);

			System.out.printf("(수정)제목 : ");
			String title = sc.nextLine();
			System.out.printf("(수정)내용 : ");
			String body = sc.nextLine();
			
			SecSql sql = new SecSql();
			
			sql.append("UPDATE article");
			sql.append(" SET updateDate = NOW()");
			sql.append(", title = ?", title);
			sql.append(", `body` = ?", body);
			sql.append(" WHERE id = ?", id);
			
			DBUtil.update(conn, sql);

//			PreparedStatement pstmt = null;
//
//			try {
//				String sql = "UPDATE article";
//				sql += " SET updateDate = NOW()";
//				sql += ", title = '" + title + "'";
//				sql += ", `body` = '" + body + "'";
//				sql += " WHERE id = " + id + ";";
//
//				System.out.println(sql);
//
//				pstmt = conn.prepareStatement(sql); // 커넥트된 db의 준비된 서술(sql)?
//
//				pstmt.executeUpdate(); // pstmt.executeUpdate : pstmt에 담겨져있는 내용을 db에 업데이트해라.
//
//			} catch (SQLException e) {
//				System.out.println("@@@@에러@@@@ : " + e);
//			} finally {
//				try {
//					if (pstmt != null && !pstmt.isClosed()) {
//						pstmt.close();
//					}
//				} catch (SQLException e) {
//					e.printStackTrace();
//				}
//			}
			
			System.out.printf("%d번 글이 수정되었습니다.\n", id);

		} else if (cmd.equals("article list")) {
			System.out.println("== 게시물 리스트 ==");

			List<Article> articles = new ArrayList<>();
			
			SecSql sql = new SecSql();
			
			sql.append("SELECT *");
			sql.append(" FROM article");
			sql.append(" ORDER BY id DESC");
			
			List<Map<String, Object>> articlesListMap = DBUtil.selectRows(conn, sql);
			
			for (Map<String, Object> articleMap : articlesListMap) {
				articles.add(new Article(articleMap));
			}

//			PreparedStatement pstmt = null;
//			ResultSet rs = null;
//
//			try {
//				String sql = "SELECT *";
//				sql += "FROM article";
//				sql += " ORDER BY id DESC";
//
//				System.out.println(sql);
//
//				pstmt = conn.prepareStatement(sql); // 커넥트된 db의 준비된 서술(sql)?
//				rs = pstmt.executeQuery(); // rs는 데이터의 압축파일 ex) article = [body, title, date]. 여기서 article이 한 페이지.
//
//				while (rs.next()) { // next는 한 페이지를 넘긴다. = 다음 압축파일을 불러온다. / 다음 장이 있을때까지 true.
//					int id = rs.getInt("id");
//					String regDate = rs.getString("regDate");
//					String updateDate = rs.getString("updateDate");
//					String title = rs.getString("title");
//					String body = rs.getString("body"); // 압축파일을 푸는과정. 각각의 변수에 저장.
//
//					Article article = new Article(id, regDate, updateDate, title, body);
//					articles.add(article);
//
//				}
//
//			} catch (SQLException e) {
//				System.out.println("에러: " + e);
//			} finally { // 실행종료. 시작 순서와 반대순서로 끄는게 좋다.
//				try {
//					if (rs != null && !rs.isClosed()) {
//						rs.close();
//					}
//				} catch (SQLException e) {
//					e.printStackTrace();
//				}
//				try {
//					if (pstmt != null && !pstmt.isClosed()) {
//						pstmt.close();
//					}
//				} catch (SQLException e) {
//					e.printStackTrace();
//				}
//			}

			System.out.println("결과 : " + articles);

			if (articles.size() == 0) {
				System.out.println("게시물이 없습니다.");
				return 0;
			}

		}

		if (cmd.equals("exit")) {
			System.out.println("프로그램을 종료합니다.");
			return -1;
		}

		return 0;
	}
}
