package com.gliesereum.karma.data.network.json.status;

import com.google.gson.annotations.SerializedName;

import javax.annotation.Generated;

@Generated("com.robohorse.robopojogenerator")
public class StatusResponse {

    @SerializedName("SOCKET-SERVICE")
    private String socketService;

    @SerializedName("KARMA-SERVICE")
    private String karmaService;

    @SerializedName("ACCOUNT-SERVICE")
    private String accountService;

    @SerializedName("MAIL-SERVICE")
    private String mailService;

    public String getSocketService() {
        return socketService;
    }

    public void setSocketService(String socketService) {
        this.socketService = socketService;
    }

    public String getKarmaService() {
        return karmaService;
    }

    public void setKarmaService(String karmaService) {
        this.karmaService = karmaService;
    }

    public String getAccountService() {
        return accountService;
    }

    public void setAccountService(String accountService) {
        this.accountService = accountService;
    }

    public String getMailService() {
        return mailService;
    }

    public void setMailService(String mailService) {
        this.mailService = mailService;
    }
}