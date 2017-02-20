package ro.ubbcluj.cs.exam;

import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.animation.Interpolator;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.Date;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import ro.ubbcluj.cs.exam.adapter.MyAdapter;
import ro.ubbcluj.cs.exam.test.R;
import ro.ubbcluj.cs.exam.domain.Item;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.TimeInterval;
import timber.log.Timber;

public class BuyItem extends AppCompatActivity implements MyCallback {

    @BindView(R.id.progress)
    ProgressBar progressBar;

    @BindView(R.id.fab)
    FloatingActionButton fab;

    private CoordinatorLayout coordinatorLayout;


    @BindView(R.id.itemName)
    EditText itemName;

    private Manager manager;

    private MyAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buy_item);
        ButterKnife.bind(this);
        manager = new Manager();
        coordinatorLayout = (CoordinatorLayout) findViewById(R.id
                .coordinatorLayout);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle(getTitle());

        progressBar.setVisibility(View.GONE);

        itemName.setText(getIntent().getStringExtra("name"));
        adapter = new MyAdapter();
    }

    @OnClick(R.id.save)
    public void buy(View view) {
        manager.buy(new Item(Integer.parseInt(getIntent().getStringExtra("id")), itemName.getText().toString(), 0,
                ""), this, progressBar);
        if (manager.networkConnectivity(this)) {
            Observable.interval(10, TimeUnit.SECONDS)
                    .timeInterval()
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Subscriber<TimeInterval<Long>>() {
                        @Override
                        public void onCompleted() {
                            Timber.v("Refresh data complete");
                        }

                        @Override
                        public void onError(Throwable e) {
                            Timber.e(e, "Error refresh data");
                            unsubscribe();
                        }

                        @Override
                        public void onNext(TimeInterval<Long> longTimeInterval) {
                            Timber.v("Refresh data");
                        }
                    });
        }

    }

    @Override
    public void add(Item item) {
        adapter.addData(item);
    }


    @Override
    public void showError(String error) {
        //progressBar.setVisibility(View.GONE);
        if (error.contains("Not Found")) {
            Snackbar.make(coordinatorLayout, error, Snackbar.LENGTH_INDEFINITE)
                    .setAction("RETRY", new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            buy(findViewById(R.id.save));
                        }
                    }).show();
        } else {
            Snackbar.make(coordinatorLayout, error, Snackbar.LENGTH_INDEFINITE)
                    .setAction("GO BACK", new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            finish();
                        }
                    }).show();
        }
    }

    @Override
    public void clear() {
        adapter.clear();
    }
}
