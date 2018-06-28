package journal.samuel.ojo.com.journalapp.util;

import android.app.Activity;
import android.content.Intent;
import android.text.format.DateFormat;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;

public class AppUtil {

    public static String getFormattedDate(Long mills) {
        return DateFormat.format("dd MMM yyyy", mills).toString();
    }

    public static String getFormattedTime(Long mills) {
        return DateFormat.format("hh:mm", mills).toString();
    }

    public static void signOut(Activity activity) {
        SharedPreferencesUtil.purge(activity);
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        GoogleSignInClient mGoogleClient = GoogleSignIn.getClient(activity, gso);
        mGoogleClient.signOut();
    }
}
