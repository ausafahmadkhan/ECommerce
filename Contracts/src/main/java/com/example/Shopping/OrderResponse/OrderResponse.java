package com.example.Shopping.OrderResponse;

public class OrderResponse
{
    private String status;
    private Error error;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Error getError() {
        return error;
    }

    public void setError(Error error) {
        this.error = error;
    }

    public OrderResponse(String status, Error error) {
        this.status = status;
        this.error = error;
    }

    public OrderResponse() {
    }
}
