package it.communikein.udacity_municipality.ui;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.TextView;

import it.communikein.udacity_municipality.R;
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
                case R.id.navigation_dashboard:
                    mBinding.message.setText(R.string.title_dashboard);
                    return true;
                case R.id.navigation_notifications:
                    mBinding.message.setText(R.string.title_notifications);
                    return true;
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_main);

        mBinding.navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
    }

}
