package com.voting.ui;

import android.app.Activity;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.voting.R;
import com.voting.ui.custom.MessageBox;
import com.voting.ui.custom.ProgressLoader;
import com.voting.utils.AppConstants;

import butterknife.Bind;

/**
 * Base activity for both {@link UserLogin} and {@link CastVoteActivity} and {@link ElectionTrendActivity}
 * }
 * Created by Renjith Kandanatt on 03/12/2018.
 */
public class BaseActivity extends AppCompatActivity {
    @Nullable @Bind(R.id.toolbar) Toolbar toolbar;

    private static final String TAG_MESSAGE_BOX = "tag_msg_box";

    protected void showProgressLoader(final String message) {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                ProgressLoader progressLoader = ProgressLoader.instance(message);
                progressLoader.show(getSupportFragmentManager(), AppConstants.TAG_PROGRESS_LOADER);
            }
        }, 150);
    }

    protected void removeProgressLoader() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Fragment f = getSupportFragmentManager().findFragmentByTag(AppConstants.TAG_PROGRESS_LOADER);
                if(null != f) ((DialogFragment) f).dismiss();
            }
        },100);
    }

    protected void showMessageBox(String title, String message) {
        MessageBox messageBox = MessageBox.instance(title, message);
        messageBox.show(getSupportFragmentManager(), TAG_MESSAGE_BOX);
    }

    protected void addNavIconListener() {
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setResult(Activity.RESULT_CANCELED);
                finishAfterTransition();
            }
        });
    }
}
