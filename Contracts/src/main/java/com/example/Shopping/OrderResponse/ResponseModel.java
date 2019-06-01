package com.example.Shopping.OrderResponse;

public class ResponseModel<T>
{
    private T data;

    public ResponseModel(T data) {
        this.data = data;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
