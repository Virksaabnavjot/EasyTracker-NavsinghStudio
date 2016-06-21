package navsingh.org.uk.easytracker.x13112406;

import android.os.Bundle;
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
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
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
            response = httpclient.execute(httppost);
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
    }
}