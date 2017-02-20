package ro.ubbcluj.cs.exam.service;

import java.util.List;

import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import ro.ubbcluj.cs.exam.domain.Item;
import rx.Observable;

public interface ItemService {
    String SERVICE_ENDPOINT = "http://172.30.115.173:3000";


    @GET("items")
    Observable<List<Item>> getItems();

    @POST("buy")
    Observable<Item> addBuy(@Body Item e);

    @POST("add")
    Observable<Item> addSell(@Body Item e);

}
