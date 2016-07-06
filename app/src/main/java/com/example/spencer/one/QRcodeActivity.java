package com.example.spencer.one;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.SparseArray;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.backendless.Backendless;
import com.backendless.BackendlessCollection;
import com.backendless.async.callback.AsyncCallback;
import com.backendless.exceptions.BackendlessFault;
import com.backendless.persistence.BackendlessDataQuery;
import com.example.spencer.one.model.Friends;
import com.example.spencer.one.model.Users;
import com.google.android.gms.vision.CameraSource;
import com.google.android.gms.vision.Detector;
import com.google.android.gms.vision.barcode.Barcode;
import com.google.android.gms.vision.barcode.BarcodeDetector;

import java.io.IOException;

public class QRcodeActivity extends AppCompatActivity {

    private TextView tvCode;
    private CameraSourcePreview cameraSourcePreview;
    private CameraSource cameraSource;
    private BarcodeDetector barcodeDetector;
    private Button qrBtn;
    private String friendID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qrcode);
        tvCode = (TextView) findViewById(R.id.tvCode);
        cameraSourcePreview = (CameraSourcePreview) findViewById(R.id.camerSourcePreview);

        setupBarcodeDetector();
        setupCameraSource();

        qrBtn = (Button) findViewById(R.id.addFrand);
        qrBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                friendID = tvCode.getText().toString();
                saveFriend();
            }
        });




    }

    @Override
    protected void onResume() {
        super.onResume();
        startCameraSource();
    }

    private void startCameraSource() {
        if (cameraSource != null) {
            try {
                cameraSourcePreview.start(cameraSource);
            } catch (IOException e) {
                cameraSource.release();
                cameraSource = null;
            }
        }
    }

    private void setupBarcodeDetector() {
        barcodeDetector = new BarcodeDetector.Builder(this)
                .setBarcodeFormats(Barcode.ALL_FORMATS)
                .build();

        barcodeDetector.setProcessor(new Detector.Processor<Barcode>() {
            @Override
            public void release() {

            }

            @Override
            public void receiveDetections(Detector.Detections<Barcode> detections) {
                final SparseArray<Barcode> barcodes = detections.getDetectedItems();

                if (barcodes.size() != 0) {
                    tvCode.post(new Runnable() {    // Use the post method of the TextView
                        public void run() {
                            tvCode.setText(    // Update the TextView
                                    barcodes.valueAt(0).displayValue
                            );
                        }
                    });
                }
            }
        });

        if (!barcodeDetector.isOperational()) {
            Log.w("TAG_QR", "Detector dependencies are not yet available.");
        }

    }

    private void setupCameraSource() {
        cameraSource = new CameraSource.Builder(this, barcodeDetector)
                .setFacing(CameraSource.CAMERA_FACING_BACK)
                .setRequestedFps(15.0f)
                .setRequestedPreviewSize(640, 640)
                .build();
    }

    @Override
    protected void onPause() {
        super.onPause();
        cameraSourcePreview.stop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (cameraSource != null) {
            cameraSource.release();
        }
    }

    private void saveFriend() {
        Backendless.Persistence.of(Users.class).findById(friendID, new AsyncCallback<Users>(){
            @Override
            public void handleResponse(Users response) {
                    final Friends friendToAdd = new Friends();
                    friendToAdd.setCurrentUserId(Backendless.UserService.CurrentUser().getUserId());
                    friendToAdd.setFriendId(response.getObjectId());
                    friendToAdd.setActualName(response.getName());
                    friendToAdd.setUserName(response.getUserName());
                    Backendless.Persistence.of(Friends.class).save(friendToAdd, new AsyncCallback<Friends>() {
                        @Override
                        public void handleResponse(Friends response) {
                            String friendName;
                            if (friendToAdd.getActualName() != null)
                                friendName = friendToAdd.getActualName();
                            else friendName = friendToAdd.getUserName();
                            Intent result = new Intent();
                            result.putExtra(AddFriendActivity.FRIEND_NAME,friendToAdd.getActualName());
                            result.putExtra(AddFriendActivity.FRIEND_USER_NAME, friendToAdd.getUserName());
                            result.putExtra(AddFriendActivity.FRIEND_USER_ID, friendToAdd.getFriendId());
                            result.putExtra(AddFriendActivity.CURRENT_USER_ID, friendToAdd.getCurrentUserId());
                            result.putExtra(AddFriendActivity.OBJECT_ID, response.getObjectId());
                            setResult(Activity.RESULT_OK, result);
                            Toast.makeText(QRcodeActivity.this, friendName + " added as a friend", Toast.LENGTH_SHORT).show();
                            finish();
                        }
                        @Override
                        public void handleFault(BackendlessFault fault) {
                            Toast.makeText(QRcodeActivity.this, "Error saving friend: "+
                                    fault.getMessage(), Toast.LENGTH_SHORT).show();
                            Log.d("TAG", "SAVing friend error: " + fault.getMessage());
                            finish();
                        }
                    });
                }
            @Override
            public void handleFault(BackendlessFault fault) {
                Toast.makeText(QRcodeActivity.this, "Error", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
