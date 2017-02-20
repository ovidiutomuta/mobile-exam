package ro.ubbcluj.cs.exam;


import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.view.View;
import android.widget.ProgressBar;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import io.realm.Realm;
import io.realm.RealmResults;
import ro.ubbcluj.cs.exam.adapter.MyAdapter;
import ro.ubbcluj.cs.exam.domain.Item;
import ro.ubbcluj.cs.exam.service.ItemService;
import ro.ubbcluj.cs.exam.service.ServiceFactory;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import timber.log.Timber;

public class Manager {


    private ItemService service;
    private Realm realm = Realm.getDefaultInstance();

    public Manager() {
        service = ServiceFactory.createRetrofitService(ItemService.class, ItemService.SERVICE_ENDPOINT);
    }

    public boolean networkConnectivity(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = cm.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isConnected();
    }


    public void loadEvents(final ProgressBar progressBar, final MyCallback callback) {

        service.getItems()
                .timeout(15, TimeUnit.SECONDS)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<List<Item>>() {
                    @Override
                    public void onCompleted() {
                        Timber.v("Item Service completed");
                        progressBar.setVisibility(View.GONE);
                    }

                    @Override
                    public void onError(Throwable e) {
                        Timber.e(e, "Error while loading the events");
                        callback.clear();
                        realm.executeTransaction(new Realm.Transaction() {
                            @Override
                            public void execute(Realm realm) {
                                RealmResults<Item> result = realm.where(Item.class).findAll();
                                List<Item> items = realm.copyFromRealm(result);
                                for (Item item : items) {
                                    callback.add(item);
                                }
                            }
                        });
                        callback.showError("Not connected!");
                    }

                    @Override
                    public void onNext(final List<Item> items) {
                        callback.clear();
                        for (Item item : items)
                            callback.add(item);
                        realm.executeTransactionAsync(new Realm.Transaction() {
                            @Override
                            public void execute(Realm realm) {
                                realm.copyToRealmOrUpdate(items);
                            }
                        });
                    }
                });

    }

    public List<Item> getItems() {
        final List<Item> itemz = new ArrayList<Item>();
        service.getItems()
                .timeout(15, TimeUnit.SECONDS)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<List<Item>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(final List<Item> items) {
                        for (Item item : items)
                            itemz.add(item);
                        realm.executeTransactionAsync(new Realm.Transaction() {
                            @Override
                            public void execute(Realm realm) {
                                realm.copyToRealmOrUpdate(items);

                            }
                        });
                    }
                });
        return itemz;
    }

    public void buyOffline(Item item, final MyCallback callback, final ProgressBar progressBar) {

    }

    public void save(Item item, final MyCallback callback, final ProgressBar progressBar) {
        progressBar.setVisibility(View.VISIBLE);
        service.addSell(item)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Item>() {
                    @Override
                    public void onCompleted() {
                        progressBar.setVisibility(View.GONE);
                        Timber.v("Item Added");
                        callback.showError("Item Added");
                    }

                    @Override
                    public void onError(Throwable e) {
                        progressBar.setVisibility(View.GONE);
                        Timber.e(e, "Error while persisting an item");
                    }

                    @Override
                    public void onNext(Item item) {
                        progressBar.setVisibility(View.GONE);
                        Timber.v("Item persisted");
                    }
                });
    }

    public void buy(Item item, final MyCallback callback, final ProgressBar progressBar) {
        progressBar.setVisibility(View.VISIBLE);
        service.addBuy(item)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Item>() {
                    @Override
                    public void onCompleted() {
                        progressBar.setVisibility(View.GONE);
                        Timber.v("Item Service completed");
                        callback.showError("Item Successfully Bought.Return to list");
                    }

                    @Override
                    public void onError(Throwable e) {
                        progressBar.setVisibility(View.GONE);
                        Timber.e(e, "Item not found");
                        callback.showError("Item Not Found");
                    }

                    @Override
                    public void onNext(Item item) {
                        progressBar.setVisibility(View.GONE);
                        Timber.v("Item persisted");
                    }
                });
    }
}
