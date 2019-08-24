package com.example.Shopping.Persistence.Repositories;

import com.example.Shopping.Persistence.Models.OrderDAO;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface OrderRepository extends MongoRepository<OrderDAO, String> {}
