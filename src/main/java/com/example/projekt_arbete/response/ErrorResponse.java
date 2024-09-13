package com.example.projekt_arbete.response;

public class ErrorResponse implements Response{

    private String responseMessage;

    public ErrorResponse (String responseMessage) {
        this.responseMessage = responseMessage;
    }

    public String getResponseMessage() {
        return responseMessage;
    }

    public void setResponseMessage(String responseMessage) {
        this.responseMessage = responseMessage;
    }

}
