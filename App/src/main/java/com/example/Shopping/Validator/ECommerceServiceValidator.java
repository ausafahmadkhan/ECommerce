package com.example.Shopping.Validator;

import com.example.Shopping.OrderRequest.OrderRequest;
import org.springframework.stereotype.Component;

@Component
public class ECommerceServiceValidator
{
    public boolean validateOrderRequest(OrderRequest orderRequest)
    {
        if (orderRequest.getQuantity() != 0 &&
            orderRequest.getItemId() != "" &&
            orderRequest.getItemId() != null &&
            orderRequest.getUserId() != "" &&
            orderRequest.getUserId() != null)
            return true;
        throw new IllegalArgumentException();
    }
}
