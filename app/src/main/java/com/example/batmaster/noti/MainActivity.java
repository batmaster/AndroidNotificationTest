package com.example.batmaster.noti;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.RemoteViews;
import android.widget.Toast;


public class MainActivity extends ActionBarActivity {

    private Button start;
    private Button stop;
    private NotificationManager nm;
    private Context context;

    private Thread thread;

    private static final int NOTI_ID = 1999;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        nm = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        context = getApplicationContext();


        final NotificationCompat.Builder noti = new NotificationCompat.Builder(context);
        noti.setSmallIcon(R.drawable.ic_launcher);



        start = (Button) findViewById(R.id.button);
        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                thread = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        for (int i = 0; i < 100; i++) {
                            noti.setContentText("Progress: " + i);

                            Notification n = noti.build();
                            n.flags |= Notification.FLAG_ONGOING_EVENT;

                            RemoteViews contentView = new RemoteViews(getPackageName(), R.layout.notification_layout);
                            contentView.setTextViewText(R.id.textView1, String.format("Progress: %d", i));
                            contentView.setTextViewText(R.id.textView2, String.valueOf(System.currentTimeMillis()));
                            n.contentView = contentView;

                            nm.notify(NOTI_ID, n);

                            Log.d("Noti", "Progress: " + i);

                            try {
                                Thread.sleep(1000);
                            } catch(InterruptedException e) {
                                Log.d("Noti", e.toString());
                            }
                        }
                    }
                });


                thread.start();
            }
        });


        stop = (Button) findViewById(R.id.button2);
        stop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                thread.interrupt();
                nm.cancel(NOTI_ID);
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
