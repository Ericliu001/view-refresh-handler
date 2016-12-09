package com.example.ericliu.viewrefresh;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private ViewRefreshHandler viewRefreshHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        viewRefreshHandler = new ViewRefreshHandler();

        final TextView tvMain = (TextView) findViewById(R.id.tvMain);
        Button btnRefresh = (Button) findViewById(R.id.btnRefresh);

        btnRefresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewRefreshHandler.executePeriodically(new RefreshRunnalbe(tvMain, null), 1000);
            }
        });


        Button btnNextActivity = (Button) findViewById(R.id.btnNextActivity);
        btnNextActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, PlaceHolderActivity.class));
            }
        });
    }


    /**
     *  uses a static inner-class to avoid the runnable object holding a reference to the activity.
     */
    private static class RefreshRunnalbe extends ViewRefreshHandler.ViewRunnable<TextView> {
        private static final String ARGS_COUNT = "args.count";

        RefreshRunnalbe(TextView textView, Bundle args) {
            super(textView, args);
        }

        @Override
        protected void run(TextView textView, Bundle args) {
            int count = args.getInt(ARGS_COUNT);
            textView.setText(count + " ");

            args.putInt(ARGS_COUNT, ++count);
        }
    }
}

