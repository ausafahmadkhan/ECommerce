package com.example.Shopping.DTOs;

public class OrderResponseDTO
{
    private String status;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public OrderResponseDTO(String status) {
        this.status = status;
    }

    public OrderResponseDTO() {
    }
}
