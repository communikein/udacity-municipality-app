package it.communikein.municipalia.ui.detail;

import android.app.Activity;
import android.arch.lifecycle.ViewModelProviders;
import android.databinding.DataBindingUtil;
import android.support.design.widget.AppBarLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;

import com.bumptech.glide.Glide;

import javax.inject.Inject;

import dagger.android.AndroidInjection;
import dagger.android.AndroidInjector;
import dagger.android.DispatchingAndroidInjector;
import dagger.android.HasActivityInjector;
import it.communikein.municipalia.R;
import it.communikein.municipalia.data.model.News;
import it.communikein.municipalia.databinding.ActivityNewsDetailBinding;
import it.communikein.municipalia.utilities.ComunicappDateUtils;
import it.communikein.municipalia.viewmodel.HomeViewModel;
import it.communikein.municipalia.viewmodel.NewsDetailViewModel;
import it.communikein.municipalia.viewmodel.factory.NewsDetailViewModelFactory;

public class NewsDetailActivity extends AppCompatActivity implements HasActivityInjector {

    private static final String TAG = NewsDetailActivity.class.getSimpleName();

    @Inject
    DispatchingAndroidInjector<Activity> mDispatchingAndroidInjector;

    public ActivityNewsDetailBinding mBinding;

    /* */
    @Inject
    NewsDetailViewModelFactory viewModelFactory;

    /* */
    private NewsDetailViewModel mViewModel;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        AndroidInjection.inject(this);
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_news_detail);

        String newsId = loadData();

        initUI(newsId);
    }

    private String loadData() {
        if (getIntent() != null)
            return getIntent().getStringExtra(News.ARG_ID);

        return null;
    }

    private void initUI(String newsId) {
        initViewModel(newsId);

        initToolbar();
    }

    private void initViewModel(String newsId) {
        mViewModel = ViewModelProviders
                .of(this, viewModelFactory)
                .get(NewsDetailViewModel.class);

        if (newsId != null) {
            mViewModel.setNewsId(newsId);
            mViewModel.getObservableNews().observe(this, news -> {
                if (news != null) {
                    long timestamp = news.getTimestamp();
                    String friendly_date = ComunicappDateUtils
                            .getFriendlyDateString(this, timestamp, true, true);

                    mBinding.titleTextview.setText(news.getTitle());
                    mBinding.descriptionTextview.setText(news.getDescription());
                    mBinding.timestampTextview.setText(friendly_date);
                    Glide.with(this).load(news.getImage()).into(mBinding.image);
                }
            });
        }
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
                    String title = " ";
                    if (mViewModel.getObservableNews().getValue() != null)
                        title = mViewModel.getObservableNews().getValue().getTitle();

                    mBinding.collapsingToolbar.setTitle(title);
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



    @Override
    public AndroidInjector<Activity> activityInjector() {
        return mDispatchingAndroidInjector;
    }
}
