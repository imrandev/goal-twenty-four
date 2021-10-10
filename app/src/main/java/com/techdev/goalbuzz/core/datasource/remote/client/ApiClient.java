package com.techdev.goalbuzz.core.datasource.remote.client;

public class ApiClient {

    private final ApiRepository apiRepository;

    public ApiClient(ApiRepository apiRepository) {
        this.apiRepository = apiRepository;
    }

    public ApiRepository getRepository() {
        return apiRepository;
    }
}
