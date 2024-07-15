package com.example.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;
import com.example.demo.data.User;
import java.util.List;

@Service
public interface UserRepository extends JpaRepository<User, Integer> {
    public List<User> findByEmailAndPassword(String email, String password);
}