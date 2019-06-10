package com.example.Shopping.Mapper;

import com.example.Shopping.DTOs.OrderRequestDTO;
import com.example.Shopping.DTOs.OrderResponseDTO;
import com.example.Shopping.OrderRequest.OrderRequest;
import com.example.Shopping.OrderResponse.OrderResponse;
import org.springframework.stereotype.Component;

@Component
public class ECommerceServiceMapper
{
    public OrderRequestDTO maptoOrderRequestDTO(OrderRequest orderRequest)
    {
        OrderRequestDTO orderRequestDTO = new OrderRequestDTO();
        orderRequestDTO.setUserId(orderRequest.getUserId());
        orderRequestDTO.setItemId(orderRequest.getItemId());
        orderRequestDTO.setQuantity(orderRequest.getQuantity());
        return orderRequestDTO;
    }

    public OrderResponse maptoOrderResponse(OrderResponseDTO orderResponseDTO)
    {
        OrderResponse orderResponse = new OrderResponse();
        orderResponse.setStatus(orderResponseDTO.getStatus());
        return orderResponse;
    }
}
