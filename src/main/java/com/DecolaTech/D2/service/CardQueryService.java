package com.DecolaTech.D2.service;

import com.DecolaTech.D2.persistence.dao.BoardColumnDAO;
import com.DecolaTech.D2.persistence.dao.CardDAO;
import com.DecolaTech.D2.persistence.dto.CardDetailsDTO;
import com.DecolaTech.D2.persistence.entity.BoardColumnEntity;
import lombok.AllArgsConstructor;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Optional;

@AllArgsConstructor
public class CardQueryService {

    private final Connection connection;

    public Optional<CardDetailsDTO> findById(final Long id) throws SQLException {
        var dao = new CardDAO(connection);
        return dao.findById(id);
    }
}
