package com.DecolaTech.D2.persistence.dao;

import com.DecolaTech.D2.persistence.dto.CardDetailsDTO;
import lombok.AllArgsConstructor;

import java.sql.Connection;

@AllArgsConstructor
public class CardDAO {

    private Connection connection;

    public CardDetailsDTO findById(Long id){
        return null;
    }
}
