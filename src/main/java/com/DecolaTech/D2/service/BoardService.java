package com.DecolaTech.D2.service;

import com.DecolaTech.D2.persistence.dao.BoardColumnDAO;
import com.DecolaTech.D2.persistence.dao.BoardDAO;
import com.DecolaTech.D2.persistence.entity.BoardColumnEntity;
import com.DecolaTech.D2.persistence.entity.BoardEntity;
import lombok.AllArgsConstructor;

import java.sql.Connection;
import java.sql.SQLException;

@AllArgsConstructor
public class BoardService {

    private final Connection connection;

    //INSERIR BOARD
    public BoardEntity insert(BoardEntity entity) throws SQLException{
        var dao = new BoardDAO(connection);
        var boardColumnDAO = new BoardColumnDAO(connection);
        try{
            dao.insert(entity);
            var columns = entity.getBoardColumns().stream().map(c -> {
                c.setBoard(entity);
                return c;
            }).toList();

            for (BoardColumnEntity column : columns) boardColumnDAO.insert(column);

            connection.commit();
        } catch (SQLException e) {
            connection.rollback();
            throw new RuntimeException(e);
        }
        return entity;
    }

    //DELETAR BOARD
    public boolean delete(final Long id) throws SQLException {
        var dao = new BoardDAO(connection);
        try{
            if(!dao.exists(id)) return false;
            dao.delete(id);
            connection.commit();
            return true;
            
        } catch (SQLException e) {
            connection.rollback();
            throw new RuntimeException(e);
        }
    }

}
