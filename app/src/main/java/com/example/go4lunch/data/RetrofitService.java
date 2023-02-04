package com.example.go4lunch.data;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitService {

    private static final Gson gson = new GsonBuilder().setLenient().create();
    private static final OkHttpClient httpClient = new OkHttpClient.Builder().build();
    static Retrofit retrofitRouteBuilder(String url){
        return new Retrofit.Builder()
                .baseUrl(url)
                .client(httpClient)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();
    }


    public static GoForLunchApi getStaticMap() {
        return retrofitRouteBuilder(retrofitRoute.MAP.getUrl()).create(GoForLunchApi.class);
    }

    enum retrofitRoute {
        MAP("https://www.mapquestapi.com/staticmap/v5/getmap?size=1080,1920&type=map&zoom=7&center=39.740112,-104.984856&imagetype=JPEG&key=2tJV0L5cZZAcDG39Zh6YcJVJBlQ9tjnG");
        //autre appel

        private final String url;
        retrofitRoute(String url) {
            this.url = url;
        }

        public String getUrl() {
            return this.url;
        }
    }
}