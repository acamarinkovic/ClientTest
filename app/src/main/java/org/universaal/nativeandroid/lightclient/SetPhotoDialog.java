package org.universaal.nativeandroid.lightclient;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.view.Window;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * Created by Aleksandar Marinkovic on 13-Oct-17.
 * Copyright by Hypercube d.o.o.
 * www.hypercubesoft.com
 */

public class SetPhotoDialog extends Dialog {

    OnDoneClickListener nDoneClickListener;
    Context context;
    @BindView(R.id.root)
    ConstraintLayout root;

    public SetPhotoDialog(@NonNull Context context, OnDoneClickListener nDoneClickListener) {
        super(context, R.style.MyDialogNotFullScreen);
        this.nDoneClickListener = nDoneClickListener;
        this.context = context;


    }


    private Unbinder unbinder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.set_phot_dialog);
        unbinder = ButterKnife.bind(this, this);

    }


    @Override
    protected void onStop() {
        super.onStop();
        unbinder.unbind();
    }

    @OnClick(R.id.camera_roll_click)
    public void onCameraRollClickClicked() {
        nDoneClickListener.startGallery();
        dismiss();
    }

    @OnClick(R.id.take_a_photo_click)
    public void onTakeAPhotoClickClicked() {
        nDoneClickListener.startCamera();
        dismiss();
    }

    @OnClick(R.id.root)
    public void onViewClicked() {
        dismiss();
    }


    public interface OnDoneClickListener {
        void startCamera();

        void startGallery();

    }
}
