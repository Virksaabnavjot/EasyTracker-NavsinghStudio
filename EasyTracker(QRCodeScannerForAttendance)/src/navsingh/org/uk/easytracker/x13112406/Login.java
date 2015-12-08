package navsingh.org.uk.easytracker.x13112406;

import android.os.Bundle;

import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import com.mysql.jdbc.Connection;
import com.mysql.jdbc.PreparedStatement;
import com.mysql.jdbc.Statement;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class Login extends Activity  implements OnClickListener{

	Button submitme;
	EditText et,pass;
	HttpPost httppost;
	HttpResponse response;
	HttpClient httpclient;
	List<NameValuePair> nameValuePairs;
	StringBuffer buffer;
	ProgressDialog dialog = null;
	TextView tv;
	MyPreference pref;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		
		submitme = (Button)findViewById(R.id.loginnav);
		et = (EditText)findViewById(R.id.username);
		pass = (EditText)findViewById(R.id.password);
		tv = (TextView) findViewById(R.id.tv);
		pref = new MyPreference(this);
		et.setText(pref.getQRCODE().toString());
		submitme.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				dialog = ProgressDialog.show(Login.this, "","Validating..", true);
				new Thread(new Runnable(){
					public void run(){
						loginlala();
					}
                }).start();               
      }
  });
		
		/*
		try
	    {
	      // Step 1: "Load" the JDBC driver
	      Class.forName("com.imaginary.sql.msql.MsqlDriver"); 

	      // Step 2: Establish the connection to the database 
	      String url = "jdbc:msql://mysql2.gear.host/adminteamsnoop"; 
	      Connection conn = (Connection) DriverManager.getConnection(url,"adminteamsnoop","Zc1d-T9_N9YA");  
	      String sql = "INSERT INTO LOGIN (Admin_ID,username, password) values (0005,'nav','yoyo')";
	      Statement statement = (Statement) conn.createStatement();   
	      statement.executeUpdate(sql);
	    }
	    catch (Exception e)
	    {
	      System.err.println("D'oh! Got an exception!"); 
	      System.err.println(e.getMessage()); 
	    } 
	    
	    */
}
	
	 void loginlala(){
        try{            
              
            httpclient=new DefaultHttpClient();
            httppost= new HttpPost("http://snooptrack.gear.host/loginapi/check.php");
            //httppost= new HttpPost("http://navsingh.org.uk/loginapi/check.php");
            
            nameValuePairs = new ArrayList<NameValuePair>(2);
             
            nameValuePairs.add(new BasicNameValuePair("username",et.getText().toString().trim()));  // $Edittext_value = $_POST['Edittext_value'];
            nameValuePairs.add(new BasicNameValuePair("password",pass.getText().toString().trim())); 
            httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
            //Execute HTTP Post Request
            response=httpclient.execute(httppost);
            
            ResponseHandler<String> responseHandler = new BasicResponseHandler();
            final String response = httpclient.execute(httppost, responseHandler);
            System.out.println("Response : " + response); 
            runOnUiThread(new Runnable() {
                public void run() {
                    tv.setText("Response from PHP : " + response);
                    dialog.dismiss();
                }
            });
             
            if(response.equalsIgnoreCase("User Found")){
                runOnUiThread(new Runnable() {
                    public void run() {
                        Toast.makeText(Login.this,"Login Success", Toast.LENGTH_LONG).show();
                        pref.setQRCODE(et.getText().toString());
                        
                        
                    }
                });
                
                startActivity(new Intent(Login.this, BarcodeReader.class));
                finish();
            }else{
                showAlert();                
            }
             
        }catch(Exception e){
            dialog.dismiss();
            System.out.println("Exception : " + e.getMessage());
        }
    }
    public void showAlert(){
        Login.this.runOnUiThread(new Runnable() {
            public void run() {
                AlertDialog.Builder builder = new AlertDialog.Builder(Login.this);
                builder.setTitle("Login Error.");
                builder.setMessage("User not Found.")  
                       .setCancelable(false)
                       .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                           public void onClick(DialogInterface dialog, int id) {
                           }
                       });                     
                AlertDialog alert = builder.create();
                alert.show();               
            }
        });
    }

	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub
		
}
}
/*
	public void meme(View v) {
		
		Intent intent = new Intent(this, BarcodeReader.class);
	    
	    startActivity(intent);
	}
	
	

}
*/
