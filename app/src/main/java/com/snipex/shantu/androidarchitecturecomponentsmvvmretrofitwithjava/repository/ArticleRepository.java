package com.snipex.shantu.androidarchitecturecomponentsmvvmretrofitwithjava.repository;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.LiveDataReactiveStreams;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;

import android.util.Log;

import com.snipex.shantu.androidarchitecturecomponentsmvvmretrofitwithjava.response.ArticleResponse;
import com.snipex.shantu.androidarchitecturecomponentsmvvmretrofitwithjava.retrofit.ApiRequest;
import com.snipex.shantu.androidarchitecturecomponentsmvvmretrofitwithjava.retrofit.RetrofitRequest;

import java.util.ArrayList;

import io.reactivex.functions.Function;
import io.reactivex.functions.Predicate;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ArticleRepository {

    //https://medium.com/@balakrishnanpt/livedata-and-rxjava-network-call-592c2e488f2e

    private static final String TAG = ArticleRepository.class.getSimpleName();
    private ApiRequest apiRequest;

    public ArticleRepository() {
        apiRequest = RetrofitRequest.createService(ApiRequest.class);
    }
    /*
    Observable : Let us consider that We are making an API call
    and receiving the response. And that response is wrapped inside
    Observable type so that it can be processed by RxJava operators.

Flowable : Each operator solves different problem, Flowable solving
Backpressure ( Backpressure happens when there are multiple responses
come at a speed that observers cannot keep up ). In Flowable,
 BackPressure is handled using BackPressure Strategies — MISSING,
  ERROR, BUFFER, DROP, LATEST. BackPressure is Handled by
  anyone of the mentioned strategies. Flowable is useful when we are making pagination call.

Single : Single always either emits one value or an error.
 It returns Latest response for all requests. It is ideal for making search call.

How to make API calls using RxJava?
For this example we are going to use Retrofit + Gson + RxJava + RxAndroid

Dependencies

Place this in your module level build.gradle file
Basic Retrofit setup

Interface for API calls

Stitch everything together
We have made basic setup Let us see, How we can make API call using this.
We are going to use Flowable for this example.


Where Live data comes in?
But, Why live data? If you look at the live data,
it handles the life cycle for us. In other hand RxJava gives us more

power to manipulate the data that we get from the response like filter, map,
flat map. So, why not combine best of both world and get some cool things out of it?
 To achieve this we are going to use LiveDataReactiveStreams,
 it basically converts Publisher’s Reactive Streams into LiveData.
  LiveDataReactiveStreams will take care of the Disposing the observers and subscriptions for RxJava.


The above mentioned example should work fine for most of all the process,
 but when there is no internet we have to handle Error by our self.
 Let’s consider that you have a “Activity-A” and “Activity-B”.
 You make a API call in “Activity-A” and move to “Activity-B”
  unfortunately internet connection is lost when you come back from “Activity-B” to “Activity-A”,
   Even if you didn’t trigger the API call on coming to the “Activity-A”. API call will be made.

But why API is call triggered here? If you notice that we are returning Livedata from makeAPICall(),
Live data lifecycle aware component,
when ever that UI comes into scope new Value from Life data is fetched,
 Hence the API call is made. To overcome this we will set and remove source making that live data will
 emit the data only once.
     */

    public LiveData<ArticleResponse> getMovieArticles(String query, String key) {
        MediatorLiveData<ArticleResponse> linkData = new MediatorLiveData<>();

        LiveData<ArticleResponse> responseLiveDataSource = LiveDataReactiveStreams.
                fromPublisher(apiRequest.getMovieArticles(query,key)
                .onErrorReturn(new Function<Throwable, ArticleResponse>() {
            @Override
            public ArticleResponse apply(Throwable throwable) throws Exception {
                ArticleResponse articleResponse=new ArticleResponse();
                articleResponse.setArticles(new ArrayList<>());
                articleResponse.setTotalResults(0);
                articleResponse.setStatus("");
                return articleResponse;
            }
        }).filter(new Predicate<ArticleResponse>() {
                            @Override
                            public boolean test(ArticleResponse articleResponse) throws Exception {
                                return articleResponse.getArticles().size()>=0;
                            }
                        }).map(new Function<ArticleResponse, ArticleResponse>() {
            @Override
            public ArticleResponse apply(ArticleResponse articleResponse) throws Exception {
                articleResponse.setStatus("Good");
                return articleResponse;
            }
        }).subscribeOn(Schedulers.io()));

        linkData.addSource(responseLiveDataSource, new Observer<ArticleResponse>() {
            @Override
            public void onChanged(ArticleResponse articleResponse) {
                if(articleResponse.getTotalResults()!=null) {
                    linkData.setValue(articleResponse);
                    linkData.removeSource(responseLiveDataSource);
                }
            }
        });

        return linkData;
//        apiRequest.getMovieArticles(query, key)
//                .enqueue(new Callback<ArticleResponse>() {
//
//
//                    @Override
//                    public void onResponse(Call<ArticleResponse> call, Response<ArticleResponse> response) {
//                        Log.d(TAG, "onResponse response:: " + response);
//
//
//
//                        if (response.body() != null) {
//                            data.setValue(response.body());
//
//                            Log.d(TAG, "articles total result:: " + response.body().getTotalResults());
//                            Log.d(TAG, "articles size:: " + response.body().getArticles().size());
//                            Log.d(TAG, "articles title pos 0:: " + response.body().getArticles().get(0).getTitle());
//                        }
//                    }
//
//                    @Override
//                    public void onFailure(Call<ArticleResponse> call, Throwable t) {
//                        data.setValue(null);
//                    }
//                });
//        return data;
    }
}
