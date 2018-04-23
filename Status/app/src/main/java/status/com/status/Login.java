package status.com.status;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import android.support.v7.widget.Toolbar;

import java.util.concurrent.TimeUnit;

public class Login extends AppCompatActivity {

    private FirebaseAuth mAuth;
    public TextInputLayout login,verify;
    public PhoneAuthProvider.OnVerificationStateChangedCallbacks mcallback;
    public String code;

    public Toolbar login_tool;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        login_tool = (Toolbar) findViewById(R.id.login_bar);
        setSupportActionBar(login_tool);
        getSupportActionBar().setTitle("Login");
        mAuth = FirebaseAuth.getInstance();
        login = (TextInputLayout) findViewById(R.id.login_phone);
        verify = (TextInputLayout) findViewById(R.id.verify_txt);

        mcallback = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            @Override
            public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {

            }

            @Override
            public void onVerificationFailed(FirebaseException e) {

            }

            @Override
            public void onCodeSent(String s, PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                super.onCodeSent(s, forceResendingToken);
                code = s;
                Toast.makeText(getApplicationContext(),"CODE SENT",Toast.LENGTH_SHORT).show();
            }
        };

    }

    public void sent_sms(View v){
        String num = "+91"+ login.getEditText().getText().toString();
        if(num.length() == 13)
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                num,60, TimeUnit.SECONDS,this,mcallback
        );
        else {
            Toast.makeText(getApplicationContext(), "INVALID NUMBER", Toast.LENGTH_SHORT).show();
            Intent home = new Intent(Login.this,Home.class);
            startActivity(home);
            finish();
        }
        }

    public void signInWithPhone(PhoneAuthCredential credential){
        mAuth.signInWithCredential(credential).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    Toast.makeText(getApplicationContext(),"SIGNIN SUCCESSFULL",Toast.LENGTH_SHORT).show();
                    Intent home = new Intent(Login.this,Home.class);
                    startActivity(home);
                    finish();
                }else {
                    Toast.makeText(getApplicationContext(),"SIGNUP FAILED",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void verify(View v){
        String input = verify.getEditText().getText().toString();

        if(!input.equals("")){
            verifyNumber(code,input);
        }
    }

    public void verifyNumber(String ver_code,String input_code){
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(ver_code, input_code);
        signInWithPhone(credential);
    }

}
