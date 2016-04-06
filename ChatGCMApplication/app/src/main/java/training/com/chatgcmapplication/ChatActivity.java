package training.com.chatgcmapplication;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.IntentSender;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.drive.Drive;
import com.google.android.gms.drive.DriveApi;
import com.google.android.gms.drive.DriveFile;
import com.google.android.gms.drive.DriveFolder;
import com.google.android.gms.drive.DriveId;
import com.google.android.gms.drive.DriveResource;
import com.google.android.gms.drive.ExecutionOptions;
import com.google.android.gms.drive.MetadataChangeSet;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.ParseException;
import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import training.com.adapter.MessageAdapter;
import training.com.common.AppConfig;
import training.com.common.RetrofitCallBackUtil;
import training.com.common.RetrofitGenerator;
import training.com.common.TimeUtil;
import training.com.dao.RESTDatabaseDAO;
import training.com.dao.RetrofitResponseCallBack;
import training.com.model.Message;
import training.com.model.Users;
import training.com.services.MessageSender;
import training.com.services.MessageSenderContent;

public class ChatActivity extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemClickListener, SwipeRefreshLayout.OnRefreshListener, RetrofitResponseCallBack, GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener {
    private static EditText txt_chat;
    private String registId;
    private MessageSender mgsSender;
    private int userId;
    private TimeUtil timeUtil;
    private MessageAdapter messageAdapter;
    private int offsetNumber = 5;
    private String id;
    @Bind(R.id.btn_send)
    Button btn_send;
    @Bind(R.id.swipeLayout)
    SwipeRefreshLayout swipeRefreshLayout;
    @Bind(R.id.listMessage)
    ListView lv_message;
    private RetrofitGenerator retrofitGenerator;
    private RetrofitCallBackUtil retrofitCallBackUtil;
    private RESTDatabaseDAO service;
    private static final int OFFSET_NUMBER_DEFAULT = 0;
    private static final int IMAGE_CODE = 1;

    private GoogleApiClient mGoogleApiClient;
    private static final String TAG = "chat-application";
    private static final int REQUEST_CODE_CAPTURE_IMAGE = 1;
    private static final int REQUEST_CODE_CREATOR = 4;
    private static final int REQUEST_CODE_RESOLUTION = 3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        ButterKnife.bind(this);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        txt_chat = (EditText) findViewById(R.id.txt_chat);
        swipeRefreshLayout.setOnRefreshListener(this);
        lv_message.setOnItemClickListener(this);
        timeUtil = new TimeUtil();
        retrofitGenerator = new RetrofitGenerator();
        retrofitCallBackUtil = new RetrofitCallBackUtil();
        Retrofit client = new Retrofit.Builder()
                .baseUrl(AppConfig.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(retrofitGenerator.gsonDateDeserializerGenerator()))
                .build();
        service = client.create(RESTDatabaseDAO.class);
        btn_send.setOnClickListener(this);
        Bundle bundle = getIntent().getExtras();
        String chatTitle = bundle.getString("titleName");
        if (getIntent().getBundleExtra("INFO") != null) {
            chatTitle = getIntent().getBundleExtra("INFO").getString("name");
            this.setTitle(chatTitle);
        } else {
            this.setTitle(chatTitle);
        }
        registId = bundle.getString("regId");
        userId = bundle.getInt("userId");
        retrofitCallBackUtil.getLastTenMessageCallBack(AppConfig.USER_ID, userId, OFFSET_NUMBER_DEFAULT, service, this);

    }

    private BroadcastReceiver onNotice = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String message = intent.getStringExtra("message");
            try {
                Message messageObj = new Message();
                messageObj.setMessage(message);
                messageObj.setUserId(userId);
                messageObj.setSender_id(AppConfig.USER_ID);
                messageObj.setExpiresTime(timeUtil.formatDateTime(timeUtil.getCurrentTime()));
                messageAdapter.add(messageObj);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            messageAdapter.notifyDataSetChanged();
        }
    };

    private BroadcastReceiver driveReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String driveId = intent.getStringExtra("driveId");
            Log.i("Drive Id", driveId);
        }
    };


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }


    @Override
    protected void onDestroy() {
        LocalBroadcastManager.getInstance(this).unregisterReceiver(onNotice);
        super.onDestroy();
    }


    private static MessageSenderContent createMegContent(String regId, String title) {
        String message = txt_chat.getText().toString();
        MessageSenderContent mgsContent = new MessageSenderContent();
        mgsContent.addRegId(regId);
        mgsContent.createData(title, message);
        return mgsContent;
    }

    @OnClick({R.id.btn_send, R.id.cryImg, R.id.smileImg, R.id.sadImg, R.id.angryImg, R.id.btn_image_picker})
    @Override
    public void onClick(View v) {
        final String message;
        switch (v.getId()) {
            case R.id.btn_send:

                message = txt_chat.getText().toString();
                if (!message.equals("")) {
                    mgsSender = new MessageSender();
                    new AsyncTask<Void, Void, Void>() {
                        @Override
                        protected Void doInBackground(Void... params) {
                            MessageSenderContent mgsContent = createMegContent(registId, AppConfig.USER_NAME);
                            Retrofit client = retrofitGenerator.createRetrofit();
                            RESTDatabaseDAO service = client.create(RESTDatabaseDAO.class);
                            if (mgsSender.sendPost(mgsContent)) {
                                Log.i("TEST_USER_ID_SEND", String.valueOf(userId));
                                Log.i("TEST_SENDER_ID_SEND", String.valueOf(AppConfig.USER_ID));
                                retrofitCallBackUtil.addMessageToServer(message, userId, AppConfig.USER_ID, service);
                            }
                            return null;
                        }
                    }.execute();
                    txt_chat.setText("");

                    try {
                        Message messageObj = new Message();
                        messageObj.setMessage(message);
                        messageObj.setUserId(AppConfig.USER_ID);
                        messageObj.setSender_id(userId);
                        messageObj.setExpiresTime(timeUtil.formatDateTime(timeUtil.getCurrentTime()));
                        messageAdapter.add(messageObj);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    messageAdapter.notifyDataSetChanged();
                }
                break;
            case R.id.cryImg:
                showEmoji(":((");
                break;
            case R.id.smileImg:
                showEmoji(":D");
                break;
            case R.id.angryImg:
                showEmoji(":@");
                break;
            case R.id.sadImg:
                showEmoji(":(");
                break;
            case R.id.btn_image_picker:
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), IMAGE_CODE);
                break;
        }
    }


    @Override
    public void onRefresh() {
        new AsyncTask<Void, Void, Void>() {

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                swipeRefreshLayout.setRefreshing(true);
            }

            @Override
            protected Void doInBackground(Void... params) {
//                retrofitCallBackUtil.getLastTenMessageCallBack(AppConfig.USER_ID, userId, offsetNumber, service, ChatActivity.this);
                retrofitCallBackUtil.getLastTenMessageCallBack(AppConfig.USER_ID, userId, offsetNumber, service, new RetrofitResponseCallBack() {

                    @Override
                    public void onSuccessMessages(ArrayList<Message> messages) {
                        messageAdapter.insertToTheFirst(messages);
                    }

                    @Override
                    public void onSuccessUser(Users user) {

                    }

                    @Override
                    public void onFailure() {

                    }
                });
                offsetNumber += 5;
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                messageAdapter.notifyDataSetChanged();
                swipeRefreshLayout.setRefreshing(false);
            }
        }.execute();
    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        TextView tvInfo = (TextView) view.findViewById(R.id.txtInfo);
        tvInfo.setVisibility(View.VISIBLE);
    }

    private void showEmoji(String emoji) {
        txt_chat.setText(txt_chat.getText().toString() + emoji);
        txt_chat.setSelection(txt_chat.getText().length());
    }


    @Override
    public void onSuccessMessages(ArrayList<Message> messages) {
        messageAdapter = new MessageAdapter(getApplicationContext(), R.layout.chat_item, messages);
        LocalBroadcastManager.getInstance(getApplicationContext()).registerReceiver(onNotice, new IntentFilter("Msg"));
        LocalBroadcastManager.getInstance(getApplicationContext()).registerReceiver(driveReceiver, new IntentFilter("GoogleDrive"));
        if (messages.size() > 0) lv_message.setAdapter(messageAdapter);
        getImageFromUrl("https://drive.google.com/uc?id=0ByI5I_-bniN3dUdtM01NT3o4WEU");
    }

    @Override
    public void onSuccessUser(Users user) {

    }

    @Override
    public void onFailure() {

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == IMAGE_CODE) {
                Uri selectedImage = data.getData();
                String[] filePathColumn = {MediaStore.Images.Media.DATA};

                Cursor cursor = getContentResolver().query(selectedImage,
                        filePathColumn, null, null, null);
                if (cursor != null) {
                    cursor.moveToFirst();
                    int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                    String picturePath = cursor.getString(columnIndex);
                    cursor.close();

                    TextView txt_content = (TextView) findViewById(R.id.txtMessage);
                    txt_content.setBackground(new BitmapDrawable(getResources(), BitmapFactory.decodeFile(picturePath)));
                    Bitmap bitmap = BitmapFactory.decodeFile(picturePath);

                    try {
                        File file = new File(getApplicationContext().getCacheDir(), "test_file");
                        file.createNewFile();
                        ByteArrayOutputStream bos = new ByteArrayOutputStream();
                        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bos);
                        byte[] bitmapdata = bos.toByteArray();

                        //write the bytes in file
                        FileOutputStream fos = new FileOutputStream(file);
                        fos.write(bitmapdata);
                        fos.flush();
                        fos.close();

                        saveFileToGoogleDrive(file);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }


                }
            } else if (requestCode == REQUEST_CODE_RESOLUTION) {


                Log.i(TAG, "Error resolution success.");

                // Make sure the app is not already connected or attempting to connect
                if (!mGoogleApiClient.isConnecting() &&
                        !mGoogleApiClient.isConnected()) {
                    mGoogleApiClient.connect();
                }

            } else if (requestCode == REQUEST_CODE_CREATOR) {
                Log.i(TAG, "File successfully saved.");
            } else {
                GooglePlayServicesUtil.getErrorDialog(requestCode, this, 0).show();
            }


        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        new AsyncTask<Void, Void, Void>() {

            @Override
            protected Void doInBackground(Void... params) {
                if (mGoogleApiClient == null) {
                    getDriveConnect();
                }
                mGoogleApiClient.connect();
                return null;
            }
        }.execute();

    }

    protected void getDriveConnect() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addApi(Drive.API)
                .addScope(Drive.SCOPE_FILE)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();
    }

    @Override
    protected void onPause() {
        if (mGoogleApiClient != null) {
            mGoogleApiClient.disconnect();
        }
        super.onPause();
    }

    @Override
    public void onConnected(Bundle bundle) {
        Log.i(TAG, "API client connected.");
    }

    @Override
    public void onConnectionSuspended(int i) {
        Log.i(TAG, "GoogleApiClient connection suspended");
    }

    @Override
    public void onConnectionFailed(ConnectionResult result) {
        Log.i(TAG, "GoogleApiClient connection failed: " + result.toString());
        if (!result.hasResolution()) {
            GooglePlayServicesUtil.getErrorDialog(result.getErrorCode(), this, 0).show();
            return;
        }
        try {
            result.startResolutionForResult(this, REQUEST_CODE_RESOLUTION);
        } catch (IntentSender.SendIntentException e) {
            Log.e(TAG, "Exception while starting resolution activity", e);
            // There was an error with the resolution intent. Try again.
            mGoogleApiClient.connect();
        }

    }

    private void saveFileToGoogleDrive(final File file) {
        Drive.DriveApi.newDriveContents(mGoogleApiClient)
                .setResultCallback(new ResultCallback<DriveApi.DriveContentsResult>() {
                    @Override
                    public void onResult(DriveApi.DriveContentsResult driveContentsResult) {
                        if (!driveContentsResult.getStatus().isSuccess()) {
                            Log.i(TAG, "Failed to create new contents.");
                            return;
                        }

                        OutputStream outputStream = driveContentsResult.getDriveContents().getOutputStream();
                        try {
                            FileInputStream fileInputStream = new FileInputStream(file);
                            byte[] buffer = new byte[1024];
                            int bytesRead;
                            while ((bytesRead = fileInputStream.read(buffer)) != -1) {
                                outputStream.write(buffer, 0, bytesRead);
                            }
                        } catch (Exception e) {
                            Log.e("FileNotFoundException", e.getMessage());
                        }
                        MetadataChangeSet metadataChangeSet = new MetadataChangeSet
                                .Builder()
                                .setMimeType("image/JPEG").setTitle("")
                                .setStarred(true)
                                .build();
                        Drive.DriveApi.getRootFolder(mGoogleApiClient)
                                .createFile(mGoogleApiClient,
                                        metadataChangeSet,
                                        driveContentsResult.getDriveContents(),
                                        new ExecutionOptions.Builder().setNotifyOnCompletion(true).build())
                                .setResultCallback(fileCallBack);
                    }
                });

    }

    final private ResultCallback<DriveFolder.DriveFileResult> fileCallBack =
            new ResultCallback<DriveFolder.DriveFileResult>() {
                @Override
                public void onResult(DriveFolder.DriveFileResult fileResult) {
                    if (fileResult.getStatus().isSuccess()) {
                        DriveId driveId = fileResult.getDriveFile().getDriveId();
                        DriveFile file = Drive.DriveApi.getFile(mGoogleApiClient, driveId);
                        file.addChangeSubscription(mGoogleApiClient);
                    }
                }


            };

    private void getImageFromUrl(String url) {
        try {
            Message messageObj = new Message();
            messageObj.setMessage(url);
            messageObj.setUserId(AppConfig.USER_ID);
            messageObj.setSender_id(userId);
            messageObj.setExpiresTime(timeUtil.formatDateTime(timeUtil.getCurrentTime()));
            messageAdapter.add(messageObj);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        messageAdapter.notifyDataSetChanged();
    }


}
