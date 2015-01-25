package com.koba.myshelf;


import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.auth.AccessToken;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.net.Uri;
import android.os.Bundle;


public class CallBackActivity extends Activity {

	String DBPASS = Pass.DBPASS;
	String PATH = Pass.PATH;
	String USER = Pass.USER;
	String OAUTH = Pass.OAUTH;
	String SECRET = Pass.SECRET;
	
	
	Connection con = null;
    PreparedStatement ps = null;
    AccessToken token = null;
    twitter4j.User user = null;
    String tablename = null;
    String friendtable = null ;
	String grouptable= null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.callback);

    	Thread th;
		(th = new Thread(new Runnable() { //新しいスレッド(thスレッド)の作成
			@Override
			public void run() {

        //Twitterの認証画面から発行されるIntentからUriを取得
        Uri uri = getIntent().getData();

        if(uri != null && uri.toString().startsWith("call://back")){
            //oauth_verifierを取得する
            String verifier = uri.getQueryParameter("oauth_verifier");
            try {
                //AccessTokenオブジェクトを取得
                token = MainActivity._oauth.getOAuthAccessToken(MainActivity._req, verifier);
            } catch (TwitterException e) {
                e.printStackTrace();
            }
        }


        System.out.println("AccessToken:"+ token.getToken() );
        System.out.println("AccessToken:"+ token.getTokenSecret());


        //認証した人のユーザ情報



     Twitter twitter = new TwitterFactory().getInstance();

        //Twitterオブジェクト作成

        twitter.setOAuthConsumer(OAUTH, SECRET);;

        //Twitterオブジェクトにアプリケーションのconsumer keyとconsumer secretをセット
        AccessToken accessToken = new AccessToken(
        		token.getToken(), token.getTokenSecret());


        twitter.setOAuthAccessToken(accessToken);

		try {
			user = twitter.verifyCredentials();
		} catch (TwitterException e) {
			// TODO 自動生成された catch ブロック
			e.printStackTrace();
		}
		System.out.println(user.getId());
		System.out.println(user.getName());
		System.out.println(user.getScreenName());
		System.out.println(user.getProfileImageURL());


        user.getId();//自分のアカウントのIDの取得（数字のID）
        user.getName();//自分のアカウントの名前を取得
        user.getScreenName();//自分のアカウントのUserNameを取得（アルファベットのみの名前）
        user.getProfileImageURL(); //自分のアカウントのプロフィール画像のURLを取得

        Connection con = null;
        // データベースへ接続
       try {
    	   con = DriverManager.getConnection(PATH,USER,DBPASS);
       } catch (SQLException e) {
		// TODO 自動生成された catch ブロック
		e.printStackTrace();
       }

       java.sql.Statement stmt = null;
	try {
		stmt = con.createStatement();
	} catch (SQLException e2) {
		// TODO 自動生成された catch ブロック
		e2.printStackTrace();
	}



       String maxcntsql = "select max(id) from all_user";

       System.out.println(maxcntsql);
       ResultSet rs = null;
       try {
    	   rs = stmt.executeQuery(maxcntsql);
		} catch (SQLException e) {
			// TODO 自動生成された catch ブロック
			 System.out.println("aaa");
			e.printStackTrace();
		}


		int max = 0;

	   try {
		while(rs.next()) {

			 System.out.println("dddddd");
			  max = rs.getInt("max(id)");


		   }
	   } catch (SQLException e1) {
		// TODO 自動生成された catch ブロック
		e1.printStackTrace();
	   }

	  int newcnt = max+1;

		  tablename = "table_"+ user.getId() ;
		  friendtable = "friend_"+user.getId() ;
		  grouptable= "group_" + user.getId();
		  String favotable =  "favo_" + user.getId();

		  String searchquery = "select id from all_user where uid=" +  user.getId()  ;
			try {

				System.out.println("1");
				ps = con.prepareStatement(searchquery);
			} catch (SQLException e1) {
				// TODO 自動生成された catch ブロック
				System.out.println("2");
				e1.printStackTrace();
			}


			  ResultSet rs2 = null;
	 			try {
	 				System.out.println("3");
	 				rs2 = ps.executeQuery();
	 			} catch (SQLException e) {
	 				// TODO 自動生成された catch ブロック
	 				System.out.println("4");
	 				e.printStackTrace();
	 			}


	 			try {
					rs2.next();
				} catch (SQLException e2) {
					// TODO 自動生成された catch ブロック
					e2.printStackTrace();
				}



			try {
				if (!rs2.wasNull()) {
					// データあり

					//一応ユーザの情報を消去する(oauth文字列がたまに変わってるから)

					String deletequery = "DELETE FROM all_user where id =" + user.getId() ;
					 stmt.executeUpdate(deletequery);
					} else {
					// データなし
					//新規
					//各テーブルCREATE
						
							
						String tablequery = "create table "+ tablename + " select * from table_sample";
						String friendquery = "create table "+ friendtable + " select * from group_2956602438";
						String groupquery = "create table "+ grouptable +  " select * from friend_2956602438";
						String favoquery ="create table "+ favotable  +  " select * from favo_1";
						
						
						  stmt.executeUpdate(tablequery);
						  stmt.executeUpdate(friendquery);
						  stmt.executeUpdate(groupquery);
						  stmt.executeUpdate(favoquery);


					}
			} catch (SQLException e1) {
				// TODO 自動生成された catch ブロック
				e1.printStackTrace();
			}



		  String Insertquery = "INSERT INTO all_user(id,uid,name,screenname,token,tokensecret,table_name,friend_table,group_table,opt)"
		  		+ "VALUES(" + newcnt + "," +  user.getId()  + ",'" + user.getName() + "','" + user.getScreenName() + "','" + token.getToken() + "','" + token.getTokenSecret()
		  		+ "','" +tablename+ "','" +friendtable+ "','" +grouptable+ "','1'" +")";


		  System.out.println(Insertquery);

          try {
				int num = stmt.executeUpdate(Insertquery);
				System.out.println(num);


          } catch (SQLException e) {
				// TODO 自動生成された catch ブロック

          }


          //設定値


          SharedPreferences pref =getSharedPreferences("pref",MODE_PRIVATE);
          
          Editor se = pref.edit();
        	se.putString("token",token.getToken() );
        	se.putString("tokensecret",token.getTokenSecret());
        	se.putLong("uid",user.getId());
        	se.putString("name",user.getName());
        	se.putString("screenname",user.getScreenName() );
        	se.putString("tablename",tablename);
        	se.putString("frinedtable",friendtable);
        	se.putString("grouptable",grouptable);
        	se.commit();



    }

		})).start();//←thスレッド終わり



		 Intent intent = new Intent();
           intent.setClassName(
                   "com.koba.myshelf",
                   "com.koba.myshelf.MainActivity");
     
           startActivity(intent);

    }




}