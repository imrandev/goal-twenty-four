package com.techdev.goalbuzz.network.client;

import javax.inject.Inject;

public class RetrofitClient {

    private final ApiRepository apiRepository;

    public RetrofitClient(ApiRepository apiRepository) {
        this.apiRepository = apiRepository;
    }

    public ApiRepository getRepository() {
        return apiRepository;
    }
}
