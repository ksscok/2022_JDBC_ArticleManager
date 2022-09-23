package com.KoreaIT.example.JAM.test;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.KoreaIT.example.JAM.Article;

public class JDBCselectTEST {
	public static void main(String[] args) {
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		List<Article> articles = new ArrayList<>();

		try {
			Class.forName("com.mysql.jdbc.Driver");
			String url = "jdbc:mysql://127.0.0.1:3306/article_manager?useUnicode=true&characterEncoding=utf8&autoReconnect=true&serverTimezone=Asia/Seoul&useOldAliasMetadataBehavior=true&zeroDateTimeNehavior=convertToNull";

			conn = DriverManager.getConnection(url, "root", ""); // db에 커넥트.
			System.out.println("연결 성공!");
			
			String sql = "SELECT *";
			sql += "FROM article";
			sql += " ORDER BY id DESC";
			
			System.out.println(sql);
			
			pstmt = conn.prepareStatement(sql); //커넥트된 db의 준비된 서술(sql)?
			rs = pstmt.executeQuery(); // rs는 데이터의 압축파일 ex) article = [body, title, date]. 여기서 article이 한 페이지.
			
			while (rs.next()) { //next는 한 페이지를 넘긴다. = 다음 압축파일을 불러온다. / 다음 장이 있을때까지 true.
				int id = rs.getInt("id"); 
				String regDate = rs.getString("regDate");
				String updateDate = rs.getString("updateDate");
				String title = rs.getString("title");
				String body = rs.getString("body"); // 압축파일을 푸는과정. 각각의 변수에 저장.
				
				Article article = new Article(id, regDate, updateDate, title, body);
				articles.add(article);
				
			}

		} catch (ClassNotFoundException e) { 
			System.out.println("드라이버 로딩 실패");
		} catch (SQLException e) {
			System.out.println("에러: " + e);
		} finally { // 실행종료. 시작 순서와 반대순서로 끄는게 좋다.
			try {
				if (rs != null && !rs.isClosed()) {
					rs.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
			try {
				if (pstmt != null && !pstmt.isClosed()) {
					pstmt.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
			try {
				if (conn != null && !conn.isClosed()) {
					conn.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		
		System.out.println("결과 : " + articles);
	} //main method
} //class
