package com.example.Shopping.Services;

import com.example.Shopping.DTOs.OrderRequestDTO;
import com.example.Shopping.DTOs.OrderResponseDTO;
import com.example.Shopping.Persistence.Models.InventoryDAO;
import com.example.Shopping.Persistence.Models.OrderDAO;
import com.example.Shopping.Persistence.Repositories.InventoryRepository;
import com.example.Shopping.Persistence.Repositories.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Date;

@Service
public class OrderServiceImpl implements OrderService
{
    private static final int  RETRY_LIMIT = 5;

    private static final String FAILURE_MSG = "Order could not be placed, Retry limit exceeded";

    @Autowired
    private InventoryRepository inventoryRepository;

    @Autowired
    private OrderRepository orderRepository;

    @Override
    public OrderResponseDTO placeOrder(OrderRequestDTO orderRequestDTO)
    {
        OrderResponseDTO orderResponseDTO = new OrderResponseDTO();
        String orderStatus = createOrderWithRetry(orderRequestDTO);
        orderResponseDTO.setStatus(orderStatus);
        return orderResponseDTO;
    }

    private String createOrderWithRetry(OrderRequestDTO orderRequestDTO)
    {
        int retryCount = 0;

        //Retrying in case the current item is being accessed by some other client.
        //If a thread is in the process of placing the order and at the same time the item is accessed by
        //some other thread, the latter task is aborted. It throws an exception (OptimisticLockingFailureException) and retried after some time
        //so that we have the updated status of the inventory.
        while (retryCount < RETRY_LIMIT)
        {
            try
            {
                return createOrder(orderRequestDTO);
            }
            catch (OptimisticLockingFailureException o)
            {
                retryCount++;
            }
            catch (Exception e)
            {
                throw e;
            }
        }

        return FAILURE_MSG;
    }

    private String createOrder(OrderRequestDTO orderRequestDTO)
    {
        InventoryDAO inventoryDAO = inventoryRepository.findById(orderRequestDTO.getItemId()).orElse(null);
        String status = "";

        if (inventoryDAO == null)
        {
            throw new IllegalArgumentException();
        }
        else
        {
            if (inventoryDAO.getQuantity() == 0)
            {
                status = "Item sold out";
            }
            else if (inventoryDAO.getQuantity() < orderRequestDTO.getQuantity())
            {
                status = "Only " + inventoryDAO.getQuantity() + " available";
            }
            else
            {
                //retrieving the item from the inventory, placing the order and updating the inventory.
                inventoryDAO.setQuantity(inventoryDAO.getQuantity() - orderRequestDTO.getQuantity());
                inventoryRepository.save(inventoryDAO);

                //keeping track of all the orders placed.
                OrderDAO orderDAO = new OrderDAO(orderRequestDTO.getItemId(), orderRequestDTO.getUserId(), orderRequestDTO.getQuantity());
                orderDAO.setDate(getCurrentTimeUsingDate());
                orderRepository.save(orderDAO);
                status = "Order successfully placed";
            }
        }

        return status;
    }

    private String getCurrentTimeUsingDate()
    {
        String pattern = "yyyy-MM-dd HH:mm:ssZ";
        Date date = new Date();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
        return simpleDateFormat.format(date);
    }
}
