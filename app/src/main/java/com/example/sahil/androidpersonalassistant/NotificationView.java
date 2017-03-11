package com.example.sahil.androidpersonalassistant;

import android.app.Activity;
import android.app.NotificationManager;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

public class NotificationView extends Activity {
    String mainMsg;
    String sideMsg;
    TextView mainMsgTextView;
    TextView sideMsgTextView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);

        //referred from: http://www.androidbegin.com/tutorial/android-custom-notification-tutorial/
        NotificationManager notificationmanager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        notificationmanager.cancel(0);

        Intent intent = getIntent();
        mainMsg = intent.getStringExtra("mainMsg");
        sideMsg = intent.getStringExtra("sideMsg");

        mainMsgTextView = (TextView) findViewById(R.id.mainMsgTextView);
        sideMsgTextView = (TextView) findViewById(R.id.sideMsgTextView);
        mainMsgTextView.setText(mainMsg);
        sideMsgTextView.setText(sideMsg);
    }
}