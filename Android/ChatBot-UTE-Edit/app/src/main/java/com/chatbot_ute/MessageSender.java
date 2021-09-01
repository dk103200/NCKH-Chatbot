package com.chatbot_ute;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface MessageSender {
    @POST("webhook")
    Call <ArrayList<BotResponse>> messageSender(@Body MessageClass message );
}
