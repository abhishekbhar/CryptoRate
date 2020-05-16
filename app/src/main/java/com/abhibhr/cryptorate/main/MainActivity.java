package com.abhibhr.cryptorate.main;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.abhibhr.cryptorate.R;
import com.abhibhr.cryptorate.recview.CryptoAdapter;
import com.abhibhr.cryptorate.recview.Divider;
import com.abhibhr.cryptorate.screen.MainScreen;
import com.abhibhr.cryptorate.viewModel.CryptoViewModel;
import com.abhibhr.data.models.CoinModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

public class MainActivity extends LocationActivity implements MainScreen {

    private static final String TAG = MainActivity.class.getSimpleName();
    private static final int DATA_FETCHING_INTERVAL = 5 * 1000; //5 seconds
    private RecyclerView mRecView;
    private CryptoAdapter mAdapter;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private CryptoViewModel mViewModel;
    private long mLastFetchedDataTimeStamp;


    private final Observer<List<CoinModel>> dataObserver = coinModels -> updateData(coinModels);
    private final Observer<String> errorObserver = error -> setError(error);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        bindView();
        mViewModel = ViewModelProviders.of(this).get(CryptoViewModel.class);
        mViewModel.getCoinsMarketData().observe(this, dataObserver);
        mViewModel.getErrorUpdates().observe(this, errorObserver);
        mViewModel.fetchData();

        //getSupportFragmentManager().beginTransaction()
        //        .add(new UILessFragment(), "UILessFragment").commit();
    }

    private void bindView() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        mRecView = findViewById(R.id.rcView);
        mSwipeRefreshLayout = findViewById(R.id.swipeToRefresh);
        mSwipeRefreshLayout.setOnRefreshListener(() -> {
            if (System.currentTimeMillis() - mLastFetchedDataTimeStamp < DATA_FETCHING_INTERVAL) {
                Log.d(TAG, "\tNot fetching from network interval dosn't reach");
                mSwipeRefreshLayout.setRefreshing(false);
                return;
            }
            mViewModel.fetchData();
        });

        mAdapter = new CryptoAdapter();
        LinearLayoutManager lm = new LinearLayoutManager(this);
        lm.setOrientation(LinearLayoutManager.VERTICAL);
        mRecView.setLayoutManager(lm);
        mRecView.setAdapter(mAdapter);
        mRecView.addItemDecoration(new Divider(this));
        setSupportActionBar(toolbar);
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(view -> mRecView.smoothScrollToPosition(0));
        fab = findViewById(R.id.fabExit);
        fab.setOnClickListener(view -> finish());
    }


    private void showErrorToast(String error) {
        Toast.makeText(this, "Error: " + error, Toast.LENGTH_LONG).show();
    }

    @Override
    public void updateData(List<CoinModel> data) {
        mLastFetchedDataTimeStamp = System.currentTimeMillis();
        mAdapter.setItems(data);
        mSwipeRefreshLayout.setRefreshing(false);
    }

    @Override
    public void setError(String msg) {
        showErrorToast(msg);
    }
//
//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        getMenuInflater().inflate(R);
//    }
}
