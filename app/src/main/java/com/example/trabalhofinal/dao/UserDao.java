package com.example.trabalhofinal.dao;

import androidx.room.Dao;
import androidx.room.Insert;

import com.example.trabalhofinal.entities.User;

@Dao
public class UserDao {
    @Insert
    void insert(User user);

    // TODO - Consulta pelo Usuário
}
