package com.example.Shopping.Persistence.Repositories;

import com.example.Shopping.Persistence.Models.InventoryDAO;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface InventoryRepository extends MongoRepository<InventoryDAO, String> {}
