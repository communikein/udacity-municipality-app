package it.communikein.udacity_municipality.ui.detail;

import android.databinding.DataBindingUtil;
import android.support.design.widget.AppBarLayout;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;

import it.communikein.udacity_municipality.R;

import it.communikein.udacity_municipality.databinding.ActivityPoiDetailBinding;

public class PoiDetailActivity extends AppCompatActivity {

    private static final String TAG = PoiDetailActivity.class.getSimpleName();

    private ActivityPoiDetailBinding mBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_poi_detail);

        initToolbar();
    }

    private void initToolbar() {
        setSupportActionBar(mBinding.toolbar);
        if (getSupportActionBar() != null)
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mBinding.collapsingToolbar.setTitle(" ");
        mBinding.collapsingToolbar.setCollapsedTitleTextColor(getResources().getColor(R.color.white));
        mBinding.appbar.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            boolean isShow = false;
            int scrollRange = -1;

            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                if (scrollRange == -1) {
                    scrollRange = appBarLayout.getTotalScrollRange();
                }
                if (scrollRange + verticalOffset == 0) {
                    mBinding.collapsingToolbar.setTitle("Città dei mille");
                    mBinding.image.setAlpha(.7f);
                    isShow = true;
                } else if(isShow) {
                    // There should a space between double quote otherwise it won't work
                    mBinding.collapsingToolbar.setTitle(" ");
                    mBinding.image.setAlpha(1f);
                    isShow = false;
                }
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home)
            NavUtils.navigateUpFromSameTask(this);

        return super.onOptionsItemSelected(item);
    }
}
