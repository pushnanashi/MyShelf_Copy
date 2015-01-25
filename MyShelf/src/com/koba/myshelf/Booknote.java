package com.koba.myshelf;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.List;

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

public class Booknote extends Activity{

	
	Pass passes = new Pass();
	String DBPASS = passes.DBPASS;
	String PATH = passes.PATH;
	String USER = passes.USER;
	
	
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
	String director = null;
	String cast = null;
	String title = null;
	String wikipedia = null;
	String author = null;
	int  tablecnt = 0;
	
	
	String notetext = null;
	
		//youtube


		  Connection con = null;
		    Bitmap bitmap = null;
		    java.sql.Connection  con2 = null;
	

		//keybord操作
			InputMethodManager inputMethodManager;
		
			private LinearLayout mainLayout;
			
			
			
			
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
                     dlg = new AlertDialog.Builder(Booknote.this);
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
              dlg = new AlertDialog.Builder(Booknote.this);
              dlg.setTitle("エラー");
              dlg.setMessage("空白、もしくは初期文字です");
              dlg.setPositiveButton("Yes", null);
              dlg.show();

		    	 
		    	 
		     }else{
		    	 
		    	 
		    	 notecommit();
		    	 
		    	 
		    	 
		    	 
		    	 
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



	
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Log.d("ログ", "起動完了");


		setContentView(R.layout.booknote);
		Button btn = (Button) findViewById(R.id.notecommit);
		Button btn2 = (Button) findViewById(R.id.button2);
		Button btn3 = (Button) findViewById(R.id.lobt);



		inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);


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
		  director  = intent.getStringExtra("director");
		  //music = intent.getStringExtra("music");
		  summary = intent.getStringExtra("summary");
		  name = intent.getStringExtra("name");
		  artist = intent.getStringExtra("artist");
		  cast = intent.getStringExtra("cast");
		  title = intent.getStringExtra("title");
		  wikipedia = intent.getStringExtra("wikipedia");
		  author = intent.getStringExtra("author");
		  tablecnt = intent.getIntExtra("tablecnt",0);
		  
		  
	

		  
			Edit = (EditText) findViewById(R.id.EditText);
			   Edit.setOnClickListener(
		        		new View.OnClickListener() {

		                    public void onClick(View v) {
		                    	Edit.setMaxLines(3);
		            			Edit.setFocusable(true);
		            			Edit.setFocusableInTouchMode(true);
		            		    Edit.requestFocus();
		                    }
		            });
			
			
		  
		  
		  

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

		  
		  
		  


		   TextView t1 = (TextView)findViewById(R.id.textView1);//title
		   TextView t3 = (TextView)findViewById(R.id.textView2);//年
		   TextView t4 = (TextView)findViewById(R.id.textView3);// 著者
		   TextView t5 = (TextView)findViewById(R.id.textView4);//
		  
		   TextView t7 = (TextView)findViewById(R.id.textView7);//summary
	


      
           
		    t1.setText(title);
		  
		    t4.setText("著者:"+ author);
		    t3.setText("出版日:"+ year);
		    t5.setText("出版元:" + manufacture);
		 
		    t7.setText(summary);
		   // t9.setText(brige.bnote);






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
