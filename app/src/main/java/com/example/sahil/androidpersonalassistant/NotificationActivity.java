package com.example.sahil.androidpersonalassistant;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.NotificationCompat;
import android.view.View;
import android.widget.RemoteViews;

public class NotificationActivity extends AppCompatActivity {
    //referred from: http://www.androidbegin.com/tutorial/android-custom-notification-tutorial/
    private Context context;
    private RemoteViews remoteViews;
    private int notificationId;
    public int requestCode = 111;
    String mainMsg, sideMsg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle bundle = getIntent().getExtras();
        mainMsg = bundle.getString("mainMsg");
        sideMsg = bundle.getString("sideMsg");

        context = this;
        remoteViews = new RemoteViews(getPackageName(), R.layout.activity_notification);

        Intent intent = new Intent(this, LoginActivity.class);
        intent.putExtra("mainMsg", mainMsg);
        intent.putExtra("sideMsg", sideMsg);

        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        NotificationCompat.Builder builder = (NotificationCompat.Builder) new NotificationCompat.Builder(this)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setTicker(getString(R.string.app_name))
                .setAutoCancel(true)
                .setContentIntent(pendingIntent)
                .setContent(remoteViews);

        remoteViews.setImageViewResource(R.id.notificationLogo, R.mipmap.ic_launcher);
        remoteViews.setTextViewText(R.id.mainMsgTextView, mainMsg);
        remoteViews.setTextViewText(R.id.sideMsgTextView, sideMsg);

        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        notificationManager.notify(0, builder.build());

    }
}
