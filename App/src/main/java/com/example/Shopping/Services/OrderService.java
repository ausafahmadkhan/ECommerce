package com.example.Shopping.Services;

import com.example.Shopping.DTOS.OrderRequestDTO.OrderRequestDTO;
import com.example.Shopping.DTOS.OrderResponseDTO.OrderResponseDTO;

public interface OrderService
{
    public OrderResponseDTO placeOrder(OrderRequestDTO orderRequestDTO);
}
