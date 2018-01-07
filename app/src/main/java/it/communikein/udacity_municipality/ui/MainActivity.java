package it.communikein.udacity_municipality.ui;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import it.communikein.udacity_municipality.R;
import it.communikein.udacity_municipality.data.login.LoginFirebase;
import it.communikein.udacity_municipality.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding mBinding;


    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    mBinding.message.setText(R.string.title_home);
                    return true;
                case R.id.navigation_news_and_events:
                    mBinding.message.setText(R.string.title_news_and_events);
                    return true;
                case R.id.navigation_pois:
                    mBinding.message.setText(R.string.title_pois);
                    return true;
            }
            return false;
        }
    };


    /** Called when the user taps the Login button */
    public void startLogin(View view) {
        // Do something in response to button
        Intent intent = new Intent(this, LoginFirebase.class);
        startActivity(intent);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_main);

        if (mBinding != null) {
            mBinding.navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        }
    }

}
