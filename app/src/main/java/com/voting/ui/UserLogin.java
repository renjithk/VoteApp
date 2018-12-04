package com.voting.ui;

import android.animation.LayoutTransition;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.ActivityCompat;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;

import com.voting.R;
import com.voting.entity.UserEntity;
import com.voting.ui.background.DataFetchService;
import com.voting.utils.CustomResultReceiver;
import com.voting.utils.StatusOptions;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnTextChanged;

/**
 * Entry activity for the application
 * This will cater the following functions
 * <p>
 *     <ol>
 *         <li>
 *             Checks for existing candidate and black list data. Retrieves them using API service
 *             if not present and saves locally
 *         </li>
 *         <li>
 *             User will be shown a loading message while it fetches data from the server
 *         </li>
 *         <li>
 *             Once data fetch sand save are completed, user will be shown login screen
 *         </li>
 *     </ol>
 * </p>
 * Created by Renjith Kandanatt on 02/12/2018.
 */
public class UserLogin extends BaseActivity implements CustomResultReceiver.IReceiverCallback {
    @Bind(R.id.layout_parent) ConstraintLayout parentLayout;
    @Bind(R.id.layout_forename) TextInputLayout forename;
    @Bind(R.id.layout_surname) TextInputLayout surname;
    @Bind(R.id.layout_pesel_number) TextInputLayout pesel;
    @Bind(R.id.btn_signin) Button signIn;

    private static final int MIN_PESEL_DIGITS_REQUIRED = 11;
    private static final int REQUEST_CODE_OPEN_VOTE_CAST = 124;
    private static final int REQUEST_CODE_OPEN_TREND = 125;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.layout_user_login);
        ButterKnife.bind(this);

        //adding transition animation to root layout
        parentLayout.getLayoutTransition().enableTransitionType(LayoutTransition.CHANGING);

        //display a loading message while background service is running
        showProgressLoader(getString(R.string.loader_initial_data_check));

        CustomResultReceiver resultReceiver = new CustomResultReceiver(new Handler());
        resultReceiver.setCallback(this);
        DataFetchService.enqueueWork(this.getApplicationContext(), resultReceiver);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        resetUI();
        switch(requestCode) {
            case REQUEST_CODE_OPEN_VOTE_CAST:
                switch (resultCode) {
                    case StatusOptions.BLACKLISTED:
                        showMessageBox(getString(R.string.warning_blacklisted_title),
                                        getString(R.string.warning_blacklisted_message));
                        break;
                    case StatusOptions.REGISTER_VOTE_OK:
                        alertAndShowElectionTrend(R.string.message_after_voting);
                        break;
                }
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onReceiveResult(int resultCode, Bundle resultData) {
        switch(resultCode) {
            case StatusOptions.SYNC_OK:
                parentLayout.setVisibility(View.VISIBLE);
                removeProgressLoader();
                break;
            case StatusOptions.LOGIN_FAIL_BLACKLISTED:
                showMessageBox(getString(R.string.warning_blacklisted_title),
                                getString(R.string.warning_blacklisted_message));
                break;
            case StatusOptions.LOGIN_ALREADY_VOTED:
                alertAndShowElectionTrend(R.string.message_already_voted_user);
                break;
            case StatusOptions.FAILED_NO_INTERNET:
                removeProgressLoader();
                Snackbar.make(parentLayout, R.string.alert_when_no_internet, Snackbar.LENGTH_LONG).show();
                break;
            case StatusOptions.LOGIN_NEW_USER:
            case StatusOptions.LOGIN_OK:
                Intent intent = new Intent(this, CastVoteActivity.class);
                intent.putExtras(resultData);
                ActivityCompat.startActivityForResult(this, intent,
                                                            REQUEST_CODE_OPEN_VOTE_CAST, null);
                break;
        }
    }

    @OnTextChanged(value = {R.id.forename}, callback = OnTextChanged.Callback.TEXT_CHANGED)
    public void onForenameChanged(CharSequence text, int start, int before, int count) {
        forename.setError(TextUtils.isEmpty(text) ? getString(R.string.login_warning_empty_forename) : null);
        enableButtonIfAllFieldsFilled();
    }

    @OnTextChanged(value = {R.id.surname}, callback = OnTextChanged.Callback.TEXT_CHANGED)
    public void onSurnameChanged(CharSequence text, int start, int before, int count) {
        surname.setError(TextUtils.isEmpty(text) ? getString(R.string.login_warning_empty_surname) : null);
        enableButtonIfAllFieldsFilled();
    }

    @OnTextChanged(value = {R.id.pesel}, callback = OnTextChanged.Callback.TEXT_CHANGED)
    public void onPeselChanged(CharSequence text, int start, int before, int count) {
        pesel.setError(TextUtils.isEmpty(text) ? getString(R.string.login_warning_empty_pesel) : null);
        enableButtonIfAllFieldsFilled();
    }

    @OnClick(R.id.btn_signin)
    public void onSaveClicked() {
        //check for 11 digits is only validation in place for Pesel number
        if(MIN_PESEL_DIGITS_REQUIRED != TextUtils.getTrimmedLength(pesel.getEditText().getText())) {
            pesel.setError(getString(R.string.login_warning_pesel_invalid));
            return;
        }
        //proceed with sign
        UserEntity user = new UserEntity();
        user.setForename(forename.getEditText().getText().toString());
        user.setSurname(surname.getEditText().getText().toString());
        user.setPesel(Long.valueOf(pesel.getEditText().getText().toString()));

        //designation job intent service to perform long running task of syncing and saving required
        //data before user can proceed
        CustomResultReceiver resultReceiver = new CustomResultReceiver(new Handler());
        resultReceiver.setCallback(this);
        DataFetchService.enqueueWork(this.getApplicationContext(), resultReceiver, user);
    }

    /**
     * Enables/Disables signin button based on the state of other UI components
     */
    private void enableButtonIfAllFieldsFilled() {
        signIn.setEnabled(!TextUtils.isEmpty(forename.getEditText().getText()) &&
                          !TextUtils.isEmpty(surname.getEditText().getText()) &&
                          !TextUtils.isEmpty(pesel.getEditText().getText()));
    }

    /**
     * Resets UI components
     * Not used in real scenarios
     */
    private void resetUI() {
        forename.getEditText().setText(null);
        surname.getEditText().setText(null);
        pesel.getEditText().setText(null);

        forename.setErrorEnabled(false);
        surname.setErrorEnabled(false);
        pesel.setErrorEnabled(false);
    }

    /**
     * Alerts users with a short message and when clicked OK button will then be redirected to
     * election trends page
     * @param resId valid resource id for the message
     */
    private void alertAndShowElectionTrend(int resId) {
        //show graph page
        Snackbar snackbar = Snackbar.make(parentLayout, resId, Snackbar.LENGTH_INDEFINITE);
        snackbar.setActionTextColor(Color.WHITE);
        snackbar.setAction(android.R.string.ok, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //open election trend page
                Intent intent = new Intent(UserLogin.this, ElectionTrendActivity.class);
                ActivityCompat.startActivityForResult(UserLogin.this, intent,
                                                        REQUEST_CODE_OPEN_TREND, null);
            }
        });
        snackbar.show();
    }
}
