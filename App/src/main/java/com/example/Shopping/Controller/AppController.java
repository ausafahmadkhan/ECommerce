package com.example.Shopping.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.example.Shopping.Mapper.ECommerceServiceMapper;
import com.example.Shopping.DTOS.OrderRequestDTO.OrderRequestDTO;
import com.example.Shopping.DTOS.OrderResponseDTO.OrderResponseDTO;
import com.example.Shopping.OrderRequest.OrderRequest;
import com.example.Shopping.OrderResponse.Error;
import com.example.Shopping.OrderResponse.OrderResponse;
import com.example.Shopping.OrderResponse.ResponseModel;
import com.example.Shopping.Services.OrderService;
import com.example.Shopping.Validator.ECommerceServiceValidator;

@RestController
@RequestMapping(path = "/ecommerce")
public class AppController
{
    @Autowired
    private ECommerceServiceValidator validator;

    @Autowired
    private ECommerceServiceMapper mapper;

    @Autowired
    private OrderService orderService;


    @RequestMapping(path = "/placeorder", method = RequestMethod.POST, consumes = {"application/json"}, produces = {"application/json"})
    ResponseEntity<ResponseModel<OrderResponse>> placeOrder(@RequestBody OrderRequest orderRequest)
    {
        OrderResponse orderResponse = new OrderResponse();
        Error error = new Error();
        try
        {
            validator.validateOrderRequest(orderRequest);
            OrderRequestDTO orderRequestDTO = mapper.maptoOrderRequestDTO(orderRequest);
            OrderResponseDTO orderResponseDTO = orderService.placeOrder(orderRequestDTO);
            orderResponse = mapper.maptoOrderResponse(orderResponseDTO);
        }
        catch (IllegalArgumentException e)
        {
            error.setErrorMessage(e.getMessage());
            error.setHttpStatus(HttpStatus.BAD_REQUEST);
        }
        catch (Exception e)
        {
            error.setErrorMessage(e.getMessage());
            error.setHttpStatus(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        finally
        {
            if (error.getErrorMessage() != null && !error.getErrorMessage().isEmpty()) {
                orderResponse.setError(error);
                return new ResponseEntity<ResponseModel<OrderResponse>>(new ResponseModel<OrderResponse>(orderResponse), error.getHttpStatus());
            } else {
                return new ResponseEntity<ResponseModel<OrderResponse>>(new ResponseModel<OrderResponse>(orderResponse), HttpStatus.OK);
            }
        }
    }


}