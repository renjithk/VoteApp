package com.voting.ui.custom;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.voting.R;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Progress loader dialog with custom messages
 *
 * Created by Renjith Kandanatt on 02/12/2018.
 */
public class ProgressLoader extends DialogFragment {
    @Bind(R.id.progress_message) TextView progressMessage;

    private static final String KEY_MESSAGE = "key_message";

    /**
     * Creates a new instance of ProgressLoader which takes message to be displayed
     * @param message valid message to be displayed on the dialog
     * @return newly created instance of type {@link ProgressLoader}
     */
    public static ProgressLoader instance(String message) {
        ProgressLoader dialog = new ProgressLoader();

        Bundle bundle = new Bundle();
        bundle.putString(KEY_MESSAGE, message);
        dialog.setArguments(bundle);

        return dialog;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        setStyle(STYLE_NO_TITLE, android.R.style.Theme_DeviceDefault_Light_NoActionBar_Fullscreen);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.layout_progress_loader, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getDialog().getWindow().getAttributes().windowAnimations = R.style.Animation_Progress_Dialog;

        progressMessage.setText(getArguments().getString(KEY_MESSAGE));
    }

    @Override
    public void onStart() {
        super.onStart();
        getDialog().setCancelable(false);
        getDialog().setCanceledOnTouchOutside(false);
    }

    @Override
    public void onDestroyView() {
        if(null != getDialog() && getRetainInstance()) {
            getDialog().setDismissMessage(null);
        }
        super.onDestroyView();
        ButterKnife.unbind(this);
    }
}
