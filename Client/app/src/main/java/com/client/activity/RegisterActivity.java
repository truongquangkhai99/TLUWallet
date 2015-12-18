package com.client.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.client.R;
import com.client.database.DataBaseHelper;

import java.util.regex.Pattern;


public class RegisterActivity extends Activity{
	
	EditText editTextPassword, editTextConfirmPassword, editTextEmail;
	Button btnCreateAccount;
	private DataBaseHelper dataBaseHelper;
	private static final Pattern EMAIL_PATTERN = Pattern
			.compile("^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$");
	
	@Override
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.register);
		
		//get Instance of Database Adapter
		dataBaseHelper = new DataBaseHelper(RegisterActivity.this);
		dataBaseHelper.open();
		
		//get Refferences of Views
		editTextPassword = (EditText)findViewById(R.id.reg_password);
		editTextEmail = (EditText)findViewById(R.id.reg_email);
		editTextConfirmPassword = (EditText)findViewById(R.id.reg_confirm_password);
		
		btnCreateAccount = (Button)findViewById(R.id.btnRegister);
		btnCreateAccount.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {

				String password = editTextPassword.getText().toString();
				String email = editTextEmail.getText().toString();
				String confirmPassword = editTextConfirmPassword.getText().toString();
				
				//check if any of fields are vaccant
				if(password.equals("") || confirmPassword.equals("")|| email.equals("")){
					Toast.makeText(getApplicationContext(), "Chưa điền thông tin", Toast.LENGTH_LONG).show();
					return;
				}
				
				//check if both password matches
				if(!password.equals(confirmPassword)){
					Toast.makeText(getApplicationContext(), "Mật khẩu không khớp", Toast.LENGTH_LONG).show();
					return;
				}

				if((!CheckEmail(email))){
					Toast.makeText(getApplicationContext(),"Mời nhập lại đúng kiểu email",Toast.LENGTH_LONG).show();
				}
				else{
					//Save the Data in Database

					if(!dataBaseHelper.checkemail(email)){
						Toast.makeText(getApplicationContext(), "Email đã có rồi", Toast.LENGTH_LONG).show();
					}else {
						dataBaseHelper.insertEntry(email,password);
						Toast.makeText(getApplicationContext(), "Đăng ký thành công", Toast.LENGTH_LONG).show();
						Intent i = new Intent(getApplicationContext(), LoginActivity.class);
						startActivity(i);
					}
				}
			}
		});
		
		
		TextView loginScreen = (TextView) findViewById(R.id.link_to_login);
		
		loginScreen.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				Intent i = new Intent(getApplicationContext(),LoginActivity.class);
				startActivity(i);		
			}
		});
	}
	
	@Override
	protected void onDestroy(){
		super.onDestroy();
		
		dataBaseHelper.close();
	}

	private boolean CheckEmail(String email) {

		return EMAIL_PATTERN.matcher(email).matches();
	}
}
