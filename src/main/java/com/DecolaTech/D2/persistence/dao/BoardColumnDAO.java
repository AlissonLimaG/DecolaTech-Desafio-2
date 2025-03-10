package com.DecolaTech.D2.persistence.dao;

import com.DecolaTech.D2.persistence.dto.BoardColumnDTO;
import com.DecolaTech.D2.persistence.entity.BoardColumnEntity;
import com.DecolaTech.D2.persistence.entity.BoardColumnKindEnum;
import com.DecolaTech.D2.persistence.entity.CardEntity;
import com.mysql.cj.jdbc.StatementImpl;
import lombok.RequiredArgsConstructor;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

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

    public List<BoardColumnEntity> findByBoardId(Long boardId) throws SQLException{
        List<BoardColumnEntity> boardColumns = new ArrayList<>();
        var sql = "SELECT * FROM BOARDS_COLUMNS WHERE board_id = ? ORDER BY `order`";
        try(var statement = connection.prepareStatement(sql)) {
            statement.setLong(1, boardId);
            statement.executeQuery();
            var resultSet = statement.getResultSet();
            while (resultSet.next()) {
                var entity = new BoardColumnEntity();
                entity.setId(resultSet.getLong("id"));
                entity.setName(resultSet.getString("name"));
                entity.setOrder(resultSet.getInt("order"));
                entity.setKind(BoardColumnKindEnum.findByName(resultSet.getString("kind")));
                boardColumns.add(entity);
            }
        }return boardColumns;
    }

    public List<BoardColumnDTO> findByBoardIdWithDetails(Long boardId) throws SQLException{
        List<BoardColumnDTO> dtos = new ArrayList<>();
        var sql =
                """
                SELECT bc.id,
                       bc.name,
                       bc.kind, 
                       (SELECT COUNT(c.id) 
                                FROM CARDS c 
                                    WHERE c.board_column_id = bc.id) cards_amount
                FROM BOARDS_COLUMNS bc
                WHERE board_id = ? ORDER BY `order`;
                """;
        try(var statement = connection.prepareStatement(sql)) {
            statement.setLong(1, boardId);
            statement.executeQuery();
            var resultSet = statement.getResultSet();
            while (resultSet.next()) {
                var dto = new BoardColumnDTO(
                        resultSet.getLong("bc.id"),
                        resultSet.getString("bc.name"),
                        BoardColumnKindEnum.findByName(resultSet.getString("bc.kind")),
                        resultSet.getInt("cards_amount")
                );
                dtos.add(dto);
            }
        }
        return dtos;
    }

    public Optional<BoardColumnEntity> findById(Long boardId) throws SQLException{
        var sql = 
            """
            SELECT bc.name,
                   bc.kind,
                   c.id,
                   c.title,
                   c.description
                   FROM BOARDS_COLUMNS bc
                   LEFT JOIN CARDS c
                        ON c.board_column_id = bc.id
                   WHERE bc.id = ?
            """;
        try(var statement = connection.prepareStatement(sql)) {
            statement.setLong(1, boardId);
            statement.executeQuery();
            var resultSet = statement.getResultSet();
            if(resultSet.next()){
                var entity = new BoardColumnEntity();
                entity.setName(resultSet.getString("bc.name"));
                entity.setKind(BoardColumnKindEnum.findByName(resultSet.getString("bc.kind")));
                do{
                    if(Objects.isNull(resultSet.getString("c.title"))) {
                        break;
                    }
                    var card = new CardEntity();
                    card.setId(resultSet.getLong("c.id"));
                    card.setTitle(resultSet.getString("c.title"));
                    card.setDescription(resultSet.getString("c.title"));
                    entity.getCards().add(card);
                }while (resultSet.next());

                return Optional.of(entity);
            }
        }return Optional.empty();
    }
    
}


