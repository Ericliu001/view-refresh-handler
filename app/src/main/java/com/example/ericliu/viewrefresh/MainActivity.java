package com.example.ericliu.viewrefresh;

import android.os.Bundle;
import android.support.annotation.Nullable;
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

        final TextView tvCounter = (TextView) findViewById(R.id.tvCounter);
        Button btnRefresh = (Button) findViewById(R.id.btnStartCounter);

        btnRefresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewRefreshHandler.executePeriodically(new CountUpRunnalbe(tvCounter, null), 1000);
            }
        });


        final TextView tvTimer = (TextView) findViewById(R.id.tvTimer);
        Button btnNextActivity = (Button) findViewById(R.id.btnStartTimer);
        btnNextActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewRefreshHandler.executePerSecond(new TimerRunnable(tvTimer, null));
            }
        });
    }


    /**
     * uses a static inner-class to avoid the runnable object holding a reference to the activity.
     */
    private static class CountUpRunnalbe extends ViewRefreshHandler.ViewRunnable<TextView> {
        private int count;

        CountUpRunnalbe(TextView textView, Bundle args) {
            super(textView, args);
        }

        @Override
        protected void run(TextView textView, Bundle args) {
            textView.setText(count + "");
            count++;
        }
    }


    private static class TimerRunnable extends ViewRefreshHandler.ViewRunnable<TextView> {
        private int second = 30;

        public TimerRunnable(TextView view, @Nullable Bundle args) {
            super(view, args);
        }

        @Override
        protected void run(TextView textView, Bundle args) {
            if (second == 0) {
                terminate();
            }
            textView.setText(second + "");
            second--;
        }
    }
}


