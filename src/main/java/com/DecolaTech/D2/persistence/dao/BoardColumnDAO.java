package com.DecolaTech.D2.persistence.dao;

import com.DecolaTech.D2.persistence.entity.BoardColumnEntity;
import com.DecolaTech.D2.persistence.entity.BoardColumnKindEnum;
import com.mysql.cj.jdbc.StatementImpl;
import lombok.RequiredArgsConstructor;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
public class BoardColumnDAO {

    private final Connection connection;

    public BoardColumnEntity insert(BoardColumnEntity entity) throws SQLException{
        var sql = "INSERT INTO BOARDS_COLUMNS(name,`order`,kind,board_id) VALUES(?, ?, ?, ?)";
        try(var statement = connection.prepareStatement(sql)){
            var i = 1;
            statement.setString(i++, entity.getName());
            statement.setInt(i++, entity.getOrder());
            statement.setString(i++, entity.getKind().name());
            statement.setLong(i++, entity.getBoard().getId());
            statement.executeUpdate();
            if(statement instanceof StatementImpl imp){
                entity.setId(imp.getLastInsertID());
            }
            return entity;
        }
    }

    public List<BoardColumnEntity> findByBoardId(Long id) throws SQLException{
        List<BoardColumnEntity> boardColumns = new ArrayList<>();
        var sql = "SELECT * FROM BOARDS_COLUMNS WHERE board_id = ? ORDER BY `order`";
        try(var statement = connection.prepareStatement(sql)) {
            statement.setLong(1, id);
            statement.executeQuery();
            var resultSet = statement.getResultSet();
            while (resultSet.next()) {
                var entitiy = new BoardColumnEntity();
                entitiy.setId(resultSet.getLong("id"));
                entitiy.setName(resultSet.getString("name"));
                entitiy.setOrder(resultSet.getInt("order"));
                entitiy.setKind(BoardColumnKindEnum.findByName(resultSet.getString("kind")));
                boardColumns.add(entitiy);
            }
        }return boardColumns;
    }
}
