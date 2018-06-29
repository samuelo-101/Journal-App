package journal.samuel.ojo.com.journalapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.BaseTransientBottomBar;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.tasks.Task;

import journal.samuel.ojo.com.journalapp.util.SharedPreferencesUtil;


public class LoginActivity extends AppCompatActivity {

    private final int GOOGLE_SIGNIN_ACTION = 99;

    private ConstraintLayout loginParentView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_login);

        loginParentView = findViewById(R.id.loginParentView);

        SignInButton signInButton = findViewById(R.id.sign_in_button);
        signInButton.setSize(SignInButton.SIZE_STANDARD);

        GoogleSignInOptions googleSignInOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        final GoogleSignInClient mGoogleSignInClient = GoogleSignIn.getClient(this, googleSignInOptions);

        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent signInIntent = mGoogleSignInClient.getSignInIntent();
                startActivityForResult(signInIntent, GOOGLE_SIGNIN_ACTION);
            }
        });

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == GOOGLE_SIGNIN_ACTION) {
            try {
                Task<GoogleSignInAccount> googleSignInAccountTask = GoogleSignIn.getSignedInAccountFromIntent(data);
                GoogleSignInAccount account = googleSignInAccountTask.getResult();

                SharedPreferencesUtil.putString(this, getString(R.string.g_id), account.getId());
                SharedPreferencesUtil.putString(this, getString(R.string.g_email), account.getEmail());
                SharedPreferencesUtil.putString(this, getString(R.string.g_firstName), account.getGivenName());
                SharedPreferencesUtil.putString(this, getString(R.string.g_lastName), account.getFamilyName());

                Intent intent = new Intent(this, MainActivity.class);
                startActivity(intent);
                finish();
            } catch (Exception ex) {
                ex.printStackTrace();
                Log.e("G_Login", ex.getMessage());
                Snackbar.make(loginParentView, getString(R.string.error_login_failed), BaseTransientBottomBar.LENGTH_LONG)
                        .show();
            }

        }
    }

    @Override
    public void onBackPressed() {
        finish();
    }
}

