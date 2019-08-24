package com.example.Shopping;

import com.example.Shopping.DTOs.OrderRequestDTO;
import com.example.Shopping.OrderRequest.OrderRequest;
import com.example.Shopping.OrderResponse.OrderResponse;
import com.example.Shopping.OrderResponse.ResponseModel;
import com.example.Shopping.Persistence.Models.InventoryDAO;
import com.example.Shopping.Persistence.Repositories.InventoryRepository;
import com.example.Shopping.Services.OrderService;
import org.junit.*;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.RestTemplate;
import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class OrderCreationTest
{

    @Autowired
    private OrderService orderService;

    @Autowired
    private InventoryRepository inventoryRepository;

    @Test
    public void integrationTest() throws IOException {

        RestTemplate restTemplate = new RestTemplate();

        String url = "http://localhost:8080/ecommerce/placeorder";

        OrderRequest orderRequest1 = new OrderRequest("24", "test_54", 45);
        HttpEntity<OrderRequest> httpEntity1 = new HttpEntity<>(orderRequest1);
        ResponseModel<OrderResponse> responseEntity1 = restTemplate.exchange(url,
                        HttpMethod.POST, httpEntity1, new ParameterizedTypeReference<ResponseModel<OrderResponse>>() {}).getBody();
        String status1 = responseEntity1.getData().getStatus();
        Assert.assertEquals("Order successfully placed", status1);

        OrderRequest orderRequest2 = new OrderRequest("24", "test_54", 25);
        HttpEntity<OrderRequest> httpEntity2 = new HttpEntity<>(orderRequest2);
        ResponseModel<OrderResponse> responseEntity2 = restTemplate.exchange(url,
                HttpMethod.POST, httpEntity2, new ParameterizedTypeReference<ResponseModel<OrderResponse>>() {}).getBody();
        String status2 = responseEntity2.getData().getStatus();
        Assert.assertEquals("Only 9 available", status2);

        OrderRequest orderRequest3 = new OrderRequest("24", "test_54", 9);
        HttpEntity<OrderRequest> httpEntity3 = new HttpEntity<>(orderRequest3);
        ResponseModel<OrderResponse> responseEntity3 = restTemplate.exchange(url,
                HttpMethod.POST, httpEntity3, new ParameterizedTypeReference<ResponseModel<OrderResponse>>() {}).getBody();
        String status3 = responseEntity3.getData().getStatus();
        Assert.assertEquals("Order successfully placed", status3);

        OrderRequest orderRequest4 = new OrderRequest("24", "test_54", 2);
        HttpEntity<OrderRequest> httpEntity4 = new HttpEntity<>(orderRequest4);
        ResponseModel<OrderResponse> responseEntity4 = restTemplate.exchange(url,
                HttpMethod.POST, httpEntity4, new ParameterizedTypeReference<ResponseModel<OrderResponse>>() {}).getBody();
        String status4 = responseEntity4.getData().getStatus();
        Assert.assertEquals("Item sold out", status4);

    }

    @Test
    public void testrace() throws InterruptedException
    {
        ExecutorService es = Executors.newCachedThreadPool();
        OrderRequestDTO[] orderRequestDTOS = new OrderRequestDTO[5];

        OrderRequestDTO orderRequestDTO1 = new OrderRequestDTO("1", "test_59", 20);
        orderRequestDTOS[0] = orderRequestDTO1;

        OrderRequestDTO orderRequestDTO2 = new OrderRequestDTO("2", "test_59", 20);
        orderRequestDTOS[1] = orderRequestDTO2;

        OrderRequestDTO orderRequestDTO3 = new OrderRequestDTO("3", "test_59", 20);
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
        es.awaitTermination(1, TimeUnit.MINUTES);
        Assert.assertEquals(13, inventoryRepository.findById("test_59").orElse(null).getQuantity());

    }

    @Before
    public void seedData()
    {
        char ch = 'a';
        for (int i = 50; i <= 60; i++)
        {
            InventoryDAO inventoryDAO = new InventoryDAO("test_" + i,   ch + "", i);
            inventoryRepository.save(inventoryDAO);
            ch++;
        }
    }

    @After
    public void cleanUp()
    {
        for (int i = 50; i <= 60; i++)
        {
            InventoryDAO inventoryDAO = inventoryRepository.findById("test_" + i).orElse(null);
            inventoryRepository.delete(inventoryDAO);
        }
    }
}
