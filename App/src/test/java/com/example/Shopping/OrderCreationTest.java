package com.example.Shopping;

import com.example.Shopping.DTOS.OrderRequestDTO.OrderRequestDTO;
import com.example.Shopping.Persistence.Models.InventoryDAO;
import com.example.Shopping.Persistence.Repositories.InventoryRepository;
import com.example.Shopping.Services.OrderService;
import org.junit.*;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.context.junit4.SpringRunner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;


@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class OrderCreationTest
{

    @Autowired
    private OrderService orderService;

    @Autowired
    private InventoryRepository inventoryRepository;

    @LocalServerPort
    private int port;

    @Test
    public void testrace() throws InterruptedException
    {
        seedData();

        ExecutorService es = Executors.newCachedThreadPool();
        OrderRequestDTO[] orderRequestDTOS = new OrderRequestDTO[5];

        OrderRequestDTO orderRequestDTO1 = new OrderRequestDTO("1", "test_59", 3);
        orderRequestDTOS[0] = orderRequestDTO1;

        OrderRequestDTO orderRequestDTO2 = new OrderRequestDTO("2", "test_59", 3);
        orderRequestDTOS[1] = orderRequestDTO2;

        OrderRequestDTO orderRequestDTO3 = new OrderRequestDTO("3", "test_59", 3);
        orderRequestDTOS[2] = orderRequestDTO3;

        OrderRequestDTO orderRequestDTO4 = new OrderRequestDTO("4", "test_59", 3);
        orderRequestDTOS[3] = orderRequestDTO4;

        OrderRequestDTO orderRequestDTO5 = new OrderRequestDTO("5", "test_59", 3);
        orderRequestDTOS[4] = orderRequestDTO5;

        for(int i=0;i<5;i++) {
            OrderRequestDTO orderRequestDTO = orderRequestDTOS[i];
            es.execute(() -> orderService.placeOrder(orderRequestDTO));
        }

        es.shutdown();
        boolean finished = es.awaitTermination(1, TimeUnit.MINUTES);
        Assert.assertEquals(44, inventoryRepository.findById("test_59").orElse(null).getQuantity());

        cleanUp();
    }

    private void seedData()
    {
        char ch = 'a';
        for (int i = 50; i <= 60; i++)
        {
            InventoryDAO inventoryDAO = new InventoryDAO("test_" + i,   ch + "", i);
            inventoryRepository.save(inventoryDAO);
            ch++;
        }
    }

    private void cleanUp()
    {
        for (int i = 50; i <= 60; i++)
        {
            InventoryDAO inventoryDAO = inventoryRepository.findById("test_" + i).orElse(null);
            inventoryRepository.delete(inventoryDAO);
        }
    }
}
