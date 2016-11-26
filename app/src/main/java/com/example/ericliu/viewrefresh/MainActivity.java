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

        final View tvMain = findViewById(R.id.tvMain);
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


    private static class RefreshRunnalbe extends ViewRefreshHandler.ViewRunnable {
        private static final String ARGS_COUNT = "args.count";

        public RefreshRunnalbe(View view, Bundle args) {
            super(view, args);
        }

        @Override
        protected void run(View view, Bundle args) {
            TextView tvMain = (TextView) view;
            int count = args.getInt(ARGS_COUNT);
            tvMain.setText(count  + " ");

            args.putInt(ARGS_COUNT, ++ count);
        }
    }
}

