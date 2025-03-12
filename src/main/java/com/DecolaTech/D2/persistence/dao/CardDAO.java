package com.DecolaTech.D2.persistence.dao;

import com.DecolaTech.D2.persistence.converter.OffsetDateTimeConverter;
import com.DecolaTech.D2.persistence.dto.CardDetailsDTO;
import com.DecolaTech.D2.persistence.entity.CardEntity;
import com.mysql.cj.jdbc.StatementImpl;
import lombok.AllArgsConstructor;

import javax.smartcardio.Card;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Objects;
import java.util.Optional;

@AllArgsConstructor
public class CardDAO {

    private Connection connection;

    public CardEntity insert(final CardEntity entity) throws SQLException{
        var sql = "INSERT INTO CARDS(title, description, board_column_id) VALUES(?,?,?)";
        try(var statement = connection.prepareStatement(sql)){
            var i = 1;
            statement.setString(i++, entity.getTitle());
            statement.setString(i++, entity.getDescription());
            statement.setLong(i, entity.getBoardColumn().getId());
            statement.executeUpdate();

            if(statement instanceof StatementImpl impl){
                entity.setId(impl.getLastInsertID());
            }
        }
        return entity;
    }

    public void moveToColumn(final Long columnId, final Long cardId) throws SQLException {
        var sql = "UPDATE CARDS SET board_column_id = ? WHERE id = ?;";
        try(var statement = connection.prepareStatement(sql)){
            var i = 1;
            statement.setLong(i++, columnId);
            statement.setLong(i, cardId);
            statement.executeUpdate();
        }
    }

    public Optional<CardDetailsDTO> findById(Long id) throws SQLException {
        var sql =
                """
                SELECT c.id,
                       c.title,
                       c.description,
                       b.block_at,
                       b.block_reason,
                       c.board_column_id,
                       bc.name,
                       (SELECT COUNT(sub_b.id)
                                FROM BLOCKS sub_b
                                    WHERE sub_b.card_id = c.id) blocks_amount
                       FROM CARDS c
                       LEFT JOIN BLOCKS b
                            ON c.id = b.card_id
                            AND b.unblock_at IS NULL
                       INNER JOIN BOARDS_COLUMNS bc
                            ON bc.id = c.board_column_id
                       WHERE c.id = ?;
                """;
        try(var statement = connection.prepareStatement(sql)){
            statement.setLong(1, id);
            statement.executeQuery();
            var resultset = statement.getResultSet();
            if(resultset.next()){
                var dto = new CardDetailsDTO(
                        resultset.getLong("c.id"),
                        resultset.getString("c.title"),
                        resultset.getString("c.description"),
                        !Objects.isNull(resultset.getString("b.block_reason")),
                        OffsetDateTimeConverter.toOffsetDateTime(resultset.getTimestamp("block_at")),
                        resultset.getString("b.block_reason"),
                        resultset.getInt("blocks_amount"),
                        resultset.getLong("c.board_column_id"),
                        resultset.getString("bc.name")
                        );
                return Optional.of(dto);
            }
        }
        return Optional.empty();
    }


}
