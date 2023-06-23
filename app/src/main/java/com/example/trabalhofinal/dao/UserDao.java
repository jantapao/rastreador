package com.example.trabalhofinal.dao;

import androidx.room.Dao;
import androidx.room.Insert;

import com.example.trabalhofinal.entities.User;

@Dao
public interface UserDao {
    @Insert
    void insertUser(User user);

    // TODO - Consulta pelo Usuário
}
