package org.universaal.nativeandroid.lightclient;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.util.Base64;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import com.getbase.floatingactionbutton.FloatingActionButton;
import com.getbase.floatingactionbutton.FloatingActionsMenu;
import com.google.gson.Gson;
import com.pixplicity.easyprefs.library.Prefs;

import org.greenrobot.eventbus.EventBus;
import org.universaal.nativeandroid.lightclient.organizer.FragmentEvent;
import org.universaal.nativeandroid.lightclient.organizer.FragmentOrganizer;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnTextChanged;
import de.hdodenhof.circleimageview.CircleImageView;
import permissions.dispatcher.NeedsPermission;
import permissions.dispatcher.RuntimePermissions;

@RuntimePermissions
public class MainActivity extends AppCompatActivity {

    public static final String PREFIX = "org.universaal.nativeandroid.light.";
    public static final String ACTION_CALL_ON = PREFIX + "CALL_ON";
    public static final String ACTION_CALL_GETLAMPS = PREFIX + "CALL_GETLAMPS";
    public static final String ACTION_REPLY_GETLAMPS = PREFIX + "REPLY_GETLAMPS";
    public static final String EXTRA_REPLYACTION = "org.universAAL.android.action.META_REPLYTOACT";
    public static final String EXTRA_REPLYCATEGORY = "org.universAAL.android.action.META_REPLYTOCAT";
    public static final int REQUEST_CODE_EDIT_PROFILE_IMAGE_CAPTURE = 104;
    public static final int REQUEST_CODE_EDIT_PROFILE_IMAGE_PICK = 105;
    @BindView(R.id.profile_image_view)
    CircleImageView profileImageView;
    @BindView(R.id.add_image)
    ImageView addImage;
    @BindView(R.id.personal_name)
    EditText personalName;
    @BindView(R.id.card_view)
    ConstraintLayout cardView;
    @BindView(R.id.choose_disability)
    FloatingActionButton chooseDisability;
    @BindView(R.id.settings_icon)
    FloatingActionButton settingsIcon;
    @BindView(R.id.multiple_actions)
    FloatingActionsMenu multipleActions;
    @BindView(R.id.simulator_icon)
    FloatingActionButton simulatorIcon;
    FragmentOrganizer fragmentOrganizer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        String images = Prefs.getString(Constants.AVATAR, null);
        personalName.setText(Prefs.getString(Constants.FIRST_NAME, ""));
        if (images != null) {
            addImage.setVisibility(View.GONE);
            byte[] decodedString = Base64.decode(images, Base64.DEFAULT);
            Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
            profileImageView.setImageBitmap(decodedByte);
        }
        fragmentOrganizer = new FragmentOrganizer(getSupportFragmentManager(), SimulatorFragment.class);
        ArrayList<Class> fragments = new ArrayList<>();
        fragmentOrganizer.setUpContainer(R.id.fragment_holder, fragments);
        inic();

        multipleActions.setOnFloatingActionsMenuUpdateListener(new FloatingActionsMenu.OnFloatingActionsMenuUpdateListener() {
            @Override
            public void onMenuExpanded() {
                chooseDisability.setVisibility(View.VISIBLE);
                settingsIcon.setVisibility(View.VISIBLE);
                simulatorIcon.setVisibility(View.VISIBLE);
            }

            @Override
            public void onMenuCollapsed() {
                chooseDisability.setVisibility(View.GONE);
                settingsIcon.setVisibility(View.GONE);
                simulatorIcon.setVisibility(View.GONE);
            }
        });
        EventBus.getDefault().post(new FragmentEvent(SimulatorFragment.class));
    }

    private void inic() {
        Intent intent = new Intent(ACTION_CALL_GETLAMPS);
        intent.addCategory(Intent.CATEGORY_DEFAULT);
        intent.putExtra(EXTRA_REPLYACTION, ACTION_REPLY_GETLAMPS);
        intent.putExtra(EXTRA_REPLYCATEGORY, Intent.CATEGORY_DEFAULT);
        sendBroadcast(intent);
    }

    public void sendData(String data) {
        Intent i = new Intent(ACTION_CALL_ON);
        i.addCategory(Intent.CATEGORY_DEFAULT);
        i.putExtra("lamp", data);

        sendBroadcast(i);
    }

    @OnClick(R.id.profile_image_view)
    public void onProfileImageViewClicked() {
    }

    @OnClick(R.id.add_image)
    public void onAddImageClicked() {
        openImageDialog();
    }

    String currentPath;

    @NeedsPermission({Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE})
    public void invokeImageCapture() {

        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            File photoFile = null;
            try {
                photoFile = createImageFile(this);
                currentPath = photoFile.getAbsolutePath();
            } catch (IOException ex) {
                // Error occurred while creating the File
            }
            if (photoFile != null) {
                Uri photoURI = FileProvider.getUriForFile(this, BuildConfig.APPLICATION_ID + ".fileprovider", photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);

            }
            startActivityForResult(takePictureIntent, REQUEST_CODE_EDIT_PROFILE_IMAGE_CAPTURE);
        }
    }

    public static File createImageFile(Context context) throws IOException {
        // Create an image file name
        String imageFileName = "JPEG_" + System.currentTimeMillis() + "_";
        File storageDir = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        return File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );
    }

    private void uploadImage(String path) {
        String filename =
                "photo_" +
                        System.currentTimeMillis() +
                        ".png";
        uploadCompressedImage(filename, path, this.getFilesDir());
    }

    public void uploadCompressedImage(String filename, String filepath, File filesDir) {
        File image = new File(filepath);
        BitmapFactory.Options bmOptions = new BitmapFactory.Options();
        Bitmap bitmap = BitmapFactory.decodeFile(image.getAbsolutePath(), bmOptions);
        bitmap = ExifUtil.rotateBitmap(filepath, bitmap);
        int outWidth = bitmap.getWidth();
        int outHeight = bitmap.getHeight();
        Bitmap resizedBitmap = Bitmap.createScaledBitmap(bitmap, outWidth, outHeight, true);
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        resizedBitmap.compress(Bitmap.CompressFormat.JPEG, 70, bos);
        final Bitmap resizedBitmapFinal = resizedBitmap;
        byte[] byteArray = bos.toByteArray();
        String stringImage = Base64.encodeToString(byteArray, Base64.DEFAULT);
        Prefs.putString(Constants.AVATAR, stringImage);
        profileImageView.setImageBitmap(resizedBitmap);
        addImage.setVisibility(View.GONE);



    }

    public void openImageDialog() {
        new SetPhotoDialog(this, new SetPhotoDialog.OnDoneClickListener() {
            @Override
            public void startCamera() {
                MainActivityPermissionsDispatcher.invokeImageCaptureWithPermissionCheck(MainActivity.this);
            }

            @Override
            public void startGallery() {
                MainActivityPermissionsDispatcher.invokeImageSelectionWithPermissionCheck(MainActivity.this);
            }
        }).show();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        if (resultCode == Activity.RESULT_OK) {
            switch (requestCode) {
                case REQUEST_CODE_EDIT_PROFILE_IMAGE_CAPTURE:
                    uploadImage(currentPath);
                    break;
                case REQUEST_CODE_EDIT_PROFILE_IMAGE_PICK:
                    Uri selectedImageURI = intent.getData();
                    String realPath = getRealPathFromURI(this, selectedImageURI);
                    uploadImage(realPath);
                    break;
            }
        }
    }

    public static String getRealPathFromURI(Context context, Uri contentUri) {
        Cursor cursor = null;
        try {
            String[] proj = {MediaStore.Images.Media.DATA};
            cursor = context.getContentResolver().query(contentUri, proj, null, null, null);
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            return cursor.getString(column_index);
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }

    @NeedsPermission({Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE})
    public void invokeImageSelection() {
        Intent pickPhoto = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(pickPhoto, REQUEST_CODE_EDIT_PROFILE_IMAGE_PICK);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        MainActivityPermissionsDispatcher.onRequestPermissionsResult(this, requestCode, grantResults);
    }

    @OnTextChanged(value = R.id.personal_name,
            callback = OnTextChanged.Callback.TEXT_CHANGED)
    void afterInput4(Editable editable) {
        Prefs.putString(Constants.FIRST_NAME, editable.toString());

    }

    @OnClick(R.id.choose_disability)
    public void onChooseDisabilityClicked() {
    }

    @OnClick(R.id.settings_icon)
    public void onSettingsIconClicked() {
    }

    @OnClick(R.id.simulator_icon)
    public void onSimulatorIconClicked() {
        EventBus.getDefault().post(new FragmentEvent(SimulatorFragment.class));
    }

    @OnClick(R.id.multiple_actions)
    public void onMultipleActionsClicked() {
    }

    @OnClick(R.id.update)
    public void onViewClicked() {

        User user = new User(Prefs.getString(Constants.AVATAR, ""), Prefs.getString(Constants.FIRST_NAME, ""),Constants.AVATAR,"");
        Gson gson= new Gson();
        String json = gson.toJson(user, User.class);
        sendData(json);
    }
}
