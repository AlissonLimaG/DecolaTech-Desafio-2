package com.DecolaTech.D2.persistence.dao;

import com.DecolaTech.D2.persistence.converter.OffsetDateTimeConverter;
import com.DecolaTech.D2.persistence.dto.CardDetailsDTO;
import lombok.AllArgsConstructor;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Optional;

@AllArgsConstructor
public class CardDAO {

    private Connection connection;

    public Optional<CardDetailsDTO> findById(Long id) throws SQLException {
        var sql =
                """
                SELECT c.id,
                       c.name,
                       c.title,
                       c.description,
                       b.block_at,
                       b.block_reason,
                       c.board_column_id,
                       bc.name,
                       COUNT(SELECT sub_b.id
                                FROM BLOCKS sub_b
                                    WHERE sub_b.id = c.id) blocks_amount
                       FROM CARDS c
                       LEFT JOIN BLOCKS b
                            ON c.id = b.card_id
                            AND b.unblocked_at IS NULL
                       INNER JOIN BOARDS_COLUMNS bc
                            ON bc.id = c.board_column_id
                       WHERE id = ?;
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
                        resultset.getString("b.block_reason").isEmpty(),
                        OffsetDateTimeConverter.toOffsetDateTime(resultset.getTimestamp("blocked_at")),
                        resultset.getString("b.block_reason"),
                        resultset.getInt("blocks_amount"),
                        resultset.getLong("c.board_column_id"),
                        resultset.getString("bc.name")
                        );
                return Optional.of(dto);
            }
        }
        return null;
    }
}
