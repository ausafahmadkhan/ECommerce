package com.example.Shopping.Persistence.Repositories;

import com.example.Shopping.Persistence.Models.AccountDAO;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface AccountRepository extends MongoRepository<AccountDAO, String> {}
