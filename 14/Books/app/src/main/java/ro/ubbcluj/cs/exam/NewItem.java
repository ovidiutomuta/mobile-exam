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

import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import ro.ubbcluj.cs.exam.test.R;
import ro.ubbcluj.cs.exam.domain.Item;

public class NewItem extends AppCompatActivity implements MyCallback {

    @BindView(R.id.progress)
    ProgressBar progressBar;

    @BindView(R.id.fab)
    FloatingActionButton fab;

    private CoordinatorLayout coordinatorLayout;

    @BindView(R.id.itemName)
    EditText itemName;

    @BindView(R.id.itemQuantity)
    EditText itemQuantity;

    private Manager manager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_item);
        ButterKnife.bind(this);
        manager = new Manager();
        coordinatorLayout = (CoordinatorLayout) findViewById(R.id
                .coordinatorLayout);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle(getTitle());

        progressBar.setVisibility(View.GONE);
    }

    @OnClick(R.id.save)
    public void save(View view) {

        manager.save(new Item(0, itemName.getText().toString(), Integer.parseInt(itemQuantity.getText().toString()),
                "Pending"), this, progressBar);
    }

    @Override
    public void add(Item item) {

    }

    @Override
    public void showError(String error) {
        //progressBar.setVisibility(View.GONE);
        if(error.contains("Added")) {
            Snackbar.make(coordinatorLayout, error, Snackbar.LENGTH_INDEFINITE)
                    .setAction("GO BACK", new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            finish();
                        }
                    }).show();
        } else {
            Snackbar.make(coordinatorLayout, error, Snackbar.LENGTH_INDEFINITE)
                    .setAction("RETRY", new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            save(findViewById(R.id.save));
                        }
                    }).show();
        }
    }

    @Override
    public void clear() {

    }
}
