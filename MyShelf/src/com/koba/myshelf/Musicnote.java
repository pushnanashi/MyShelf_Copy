package com.koba.myshelf;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.List;
import java.util.regex.Pattern;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.text.SpannableStringBuilder;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubeIntents;
import com.google.api.client.http.HttpRequest;
import com.google.api.client.http.HttpRequestInitializer;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.youtube.YouTube;
import com.google.api.services.youtube.model.SearchListResponse;
import com.google.api.services.youtube.model.SearchResult;

public class Musicnote extends Activity{
	
	String DBPASS = Pass.DBPASS;
	String PATH = Pass.PATH;
	String USER = Pass.USER;
	InputMethodManager inputMethodManager;
	
	private LinearLayout mainLayout;
	
	int  tablecnt = 0;
	String notetext = null;

	String picture = null;
	 final Handler handler=new Handler();
	InputStream istream = null;
	
	URL url = null;

	Bitmap rtbitmap = null;
	EditText Edit = null;

	String table  = null ;
	String year= null ;
	String ECURL= null ;
	String manufacture= null ;
	String allmusic= null ;
	String alubam = null ;
	String music = null ;
	String summary= null ;
	String name = null;
	String artist = null;
	
	
	  
		//youtube
		  

		  Connection con = null;
		    Bitmap bitmap = null;
		    java.sql.Connection  con2 = null;
		    String thumbnail = null;
		    URL thumbnailURL = null;
		    private Button playVideoButton;
			/** Global instance of the HTTP transport. */
			private final HttpTransport HTTP_TRANSPORT = new NetHttpTransport();
			/** Global instance of the JSON factory. */
			private final JsonFactory JSON_FACTORY = new JacksonFactory();
			/** Global instance of Youtube object to make all API requests. */
			private YouTube youtube;
			public static final String DEVELOPER_KEY = Pass.DEVELOPER_KEY; //APIkey，自分の入れてください

			private static final int REQ_START_STANDALONE_PLAYER = 1;
			private static final int REQ_RESOLVE_SERVICE_MISSING = 2;

			public String video_ID; //videoID
			public String queryTerm="進撃の巨人"; //動画検索用文字列 ここに入れた文字列がyoutubeで検索される
			

			
			

			public void youtubethum(){
				
				
				Thread th;

				(th = new Thread(new Runnable() { //新しいスレッド(thスレッド)の作成
					@Override
					public void run() {
		           //ここから動画検索
						try {
							//おまじない
							youtube = new YouTube.Builder(HTTP_TRANSPORT, JSON_FACTORY, new HttpRequestInitializer() {
							public void initialize(HttpRequest request) throws IOException {
							}
								}).setApplicationName("com.example.youtube").build();

							//動画検索条件の指定
							YouTube.Search.List search = youtube.search().list("id,snippet");
							//search.setPart("id");
							search.setKey(DEVELOPER_KEY);    // APIkey設定
							search.setQ(queryTerm);   // 検索文字列
							search.setType("video");  // 検索する対象
							search.setMaxResults((long)1); // 検索で出す数
							search.setFields("items(id/videoId)"); //取得するアイテム(videoIDだけ取得する)

							SearchListResponse searchResponse = search.execute(); //検索

							List<SearchResult> searchResultList = searchResponse.getItems();

							SearchResult singleVideo = searchResultList.get(0);

							video_ID = singleVideo.getId().getVideoId().toString(); //検索結果を動画再生のときに使用するvideoIDの変数に入れる
							
							
							
							thumbnail = "http://img.youtube.com/vi/"+ video_ID + "/0.jpg";

							
							
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
							        	System.out.println("video_ID" + video_ID);
								      String UPDATEquery = "UPDATE " +  table + " SET  video_id = '" + video_ID + "' where id = " + tablecnt ;
								      
								  	System.out.println("UPDATEquery" + UPDATEquery);
							        try {
										stmt.executeUpdate(UPDATEquery);
							        } catch (SQLException e) {
										// TODO 自動生成された catch ブロック
										e.printStackTrace();
							        }
							        
							        
							
					      
							
							
							System.out.println(thumbnail);
							thumbnailURL = new URL(thumbnail);
							istream =thumbnailURL.openStream();

			 				//画像を取得
			 				bitmap= BitmapFactory.decodeStream(istream);
			 				if(bitmap == null){

			 					System.out.println("ccccc");

			 				}else{

			 					System.out.println("dddddddd");


			 				}

			 				istream.close();



			 				  handler.post(new Runnable() {
			            	    	@Override
			                        public void run() {

			            	    	ImageView youtubeview=(ImageView) findViewById(R.id.youtubeview);
			            	           
			            	    	 youtubeview.setOnClickListener(on4);
			            	    	 
			            	    	youtubeview.setImageBitmap(bitmap);




			                        }
			                    });



							System.out.println(video_ID);
						} catch (IOException e) {
							e.printStackTrace();
						}
					}
				})).start();//←thスレッド終わり


				
				
				
				
			}
			
			
			

		
	

	
	public View.OnClickListener on4 = new View.OnClickListener() {

		public void onClick(View v) {
			System.out.println("xxxxxxxxxxxxxxx");


			int startTimeMillis = 0; //動画開始位置
		    boolean autoplay = true; //自動再生するかどうか
		    boolean lightboxMode = true; //全画面の動画再生にしないかどうか

		    Intent youtubeintent = null;

		    	youtubeintent = YouTubeIntents.createPlayVideoIntentWithOptions(
		    		  Musicnote.this ,video_ID,false,false);

		    	startActivity(youtubeintent);
		    if (youtubeintent != null) {




		      if (canResolveIntent(youtubeintent)) {
		        startActivityForResult(youtubeintent, REQ_START_STANDALONE_PLAYER);
		      } else {
		        // Could not resolve the intent - must need to install or update the YouTube API service.
		        YouTubeInitializationResult.SERVICE_MISSING
		            .getErrorDialog(Musicnote.this, REQ_RESOLVE_SERVICE_MISSING).show();
		      }
		    }


		}
	};


	  private boolean canResolveIntent(Intent youtubeintent) {
	    List<ResolveInfo> resolveInfo = getPackageManager().queryIntentActivities(youtubeintent, 0);
	    return resolveInfo != null && !resolveInfo.isEmpty();
	  }


	
	public View.OnClickListener EC = new View.OnClickListener() {

		public void onClick(View v) {



			 Intent intentEC = new Intent();
	            intentEC.setClassName(
	                    "com.koba.myshelf",
	                    "com.koba.myshelf.ECsite");

	            intentEC.putExtra("EC",ECURL);

	        
	            startActivity(intentEC);



		}
	};
	
	public View.OnClickListener shelf = new View.OnClickListener() {

		public void onClick(View v) {

			 Intent intent = new Intent();
	            intent.setClassName(
	                    "com.koba.myshelf",
	                    "com.koba.myshelf.MainActivity");

	            startActivity(intent);

			finish();



		}
	};
	

      
	
	public void notecommit()   {// 検索したいDVD名を入力することで,そのDVDのあらすじを返す

		
		   (new Thread(new Runnable() {
             @Override
             public void run() {

		
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

        
        
        String commit = "UPDATE " +  table +" SET  note = '" + notetext + "' where id = " + tablecnt ;
        
        try {
			stmt.executeUpdate(commit);
        } catch (SQLException e) {
			// TODO 自動生成された catch ブロック
			e.printStackTrace();
        }
        
        
		
        
        handler.post(new Runnable() {
	    	@Override
            public void run() {
        
        
	    	 AlertDialog.Builder dlg;
          dlg = new AlertDialog.Builder(Musicnote.this);
          dlg.setTitle("完了");
          dlg.setMessage("書き込みました。再度書き込みで \n訂正できます");
          dlg.setPositiveButton("Yes", null);
          dlg.show();



         }
     });

	    		
	    		
		
     }
		
             
             
             
             
         


 })).start();
             
             
		
	}
	

	
	
	public View.OnClickListener notecommit = new View.OnClickListener() {

		public void onClick(View v) {



		
			
			 SpannableStringBuilder sb = (SpannableStringBuilder)Edit.getText();
		     notetext = sb.toString();
		     
		     
		     
		     //入力判断
		     
		     if(notetext.equals("ノートを残せます") || notetext == null ){
		    	 
		   	  AlertDialog.Builder dlg;
              dlg = new AlertDialog.Builder(Musicnote.this);
              dlg.setTitle("エラー");
              dlg.setMessage("空白、もしくは初期文字です");
              dlg.setPositiveButton("Yes", null);
              dlg.show();

		    	 
		    	 
		     }else{
		    	 
		    	 
		    	 notecommit();
		    	 
		    	 
		    	 
		    	 
		    	 
		     }
		     
		     

		}
	};

	
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Log.d("ログ", "起動完了");

		
		setContentView(R.layout.musicnote);
		Button btn = (Button) findViewById(R.id.notecommit);
		Button btn2 = (Button) findViewById(R.id.button2);
		Button btn3 = (Button) findViewById(R.id.lobt);
		
		
		
		
		
		
		final ImageView IV = (ImageView)findViewById(R.id.imageView1);  //Bitmap
		
		
		  btn.setOnClickListener(notecommit);
		  btn2.setOnClickListener(EC);
		  btn3.setOnClickListener(shelf);
		
	
		  
		  Intent intent = getIntent();
		
		  table = intent.getStringExtra("table");
		  year = intent.getStringExtra("year");
		  ECURL = intent.getStringExtra("ECURL");
		  manufacture = intent.getStringExtra("manufacture");
		  picture = intent.getStringExtra("picture");
		  allmusic = intent.getStringExtra("allmusic");
		  alubam = intent.getStringExtra("alubam");
		  //music = intent.getStringExtra("music");
		  summary = intent.getStringExtra("summary");
		  name = intent.getStringExtra("name");
		  artist = intent.getStringExtra("artist");	 
		   System.out.println("tablecnt" + tablecnt);
		  
		  tablecnt = intent.getIntExtra("tablecnt",0);
		  System.out.println("tablecnt" + tablecnt);
		  
		  queryTerm=artist + " " + name; 
		  youtubethum();
		  

		inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
		  
		 
		  
			Edit = (EditText) findViewById(R.id.note);
			   Edit.setOnClickListener(
		        		new View.OnClickListener() {

		                    public void onClick(View v) {
		                    	Edit.setMaxLines(3);
		            			Edit.setFocusable(true);
		            			Edit.setFocusableInTouchMode(true);
		            		    Edit.requestFocus();
		                    }
		            });
			
			
		  
		  
		  if( allmusic != null){
		  
		  Pattern pattern = Pattern.compile("/");
			String[] ss = pattern.split(allmusic, 0); //「:」で分割
			
			int counter = 0;
			for(String s:ss){
				
				if(counter == 0){}else{
				
					//分割を表示　後でlistviewに入れる
					
					
				System.out.println(s);
				}
				
				counter++;
				
			}
		  }
		  
		   TextView t1 = (TextView)findViewById(R.id.textView1);//title
		   TextView t3 = (TextView)findViewById(R.id.textView2);//年
		   TextView t4 = (TextView)findViewById(R.id.textView3);// artist
		  TextView t5 = (TextView)findViewById(R.id.textView4);//販売元
		 // TextView t6 = (TextView)findViewById(R.id.textView5);//挿入歌
		 //  TextView t7 = (TextView)findViewById(R.id.textView7);//summary
		 //  TextView t9 = (TextView)findViewById(R.id.TextView01);//Note
		  
		  
		 

		    t1.setText(alubam);
		    t3.setText("発売日:" + year );
		    t4.setText("Artist:" +  artist );
		    t5.setText("レーベル:" + manufacture);
		   
		  
		  
		  
		   (new Thread(new Runnable() {
	            @Override
	            public void run() {


	            	try {
	            		System.out.println(picture);
						url = new URL(picture);
					} catch (MalformedURLException e) {
						// TODO 自動生成された catch ブロック
						e.printStackTrace();
					}
	 				//入力ストリームを開く
					System.out.println("nullpo2");

					try {
						istream = url.openStream();
					} catch (IOException e) {
						// TODO 自動生成された catch ブロック
						e.printStackTrace();
					}

	 				//画像を取得
					rtbitmap= BitmapFactory.decodeStream(istream);
					try {
						istream.close();
					} catch (IOException e1) {
						// TODO 自動生成された catch ブロック
						e1.printStackTrace();
					}

					if(rtbitmap == null){

						System.out.println("nullpo3");

					}
					try {
						istream.close();
					} catch (IOException e) {
						// TODO 自動生成された catch ブロック
						e.printStackTrace();
					}




				    handler.post(new Runnable() {
           	    	@Override
                       public void run() {
           	    		if(rtbitmap == null){

           					System.out.println("nullpo5");

           				}

           			IV.setImageBitmap(rtbitmap);


                       }
                   });



	            }


	            })).start();
		  
		  
		
	}
	
	
	 @Override
	  public boolean onTouchEvent(MotionEvent event) {

	  // キーボードを隠す
	  inputMethodManager.hideSoftInputFromWindow(mainLayout.getWindowToken(),
	  InputMethodManager.HIDE_NOT_ALWAYS);
	  // 背景にフォーカスを移す
	  mainLayout.requestFocus();

	  return true;

	  }
	  

	

}
