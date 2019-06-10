package com.example.Shopping.Services;

import com.example.Shopping.DTOs.OrderRequestDTO;
import com.example.Shopping.DTOs.OrderResponseDTO;

public interface OrderService
{
    public OrderResponseDTO placeOrder(OrderRequestDTO orderRequestDTO);
}
