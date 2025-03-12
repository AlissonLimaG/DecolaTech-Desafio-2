package com.DecolaTech.D2.persistence.dao;

import com.DecolaTech.D2.persistence.converter.OffsetDateTimeConverter;
import lombok.AllArgsConstructor;

import java.sql.Connection;
import java.sql.SQLException;
import java.time.OffsetDateTime;

import static com.DecolaTech.D2.persistence.converter.OffsetDateTimeConverter.toTimestamp;

@AllArgsConstructor
public class BlockDAO {

    final Connection connection;

    public void block(final Long cardId, final String reason) throws SQLException {
        var sql =
                """
                INSERT INTO BLOCKS(block_at, block_reason, card_id) VALUES(?,?,?);
                """;
        try(var statement = connection.prepareStatement(sql)){
            var i = 1;
            statement.setTimestamp(i++, OffsetDateTimeConverter.toTimestamp(OffsetDateTime.now()));
            statement.setString(i++, reason);
            statement.setLong(i, cardId);
            statement.executeUpdate();
        }
    }

    public void unblock(final String reason, final Long cardId) throws SQLException{
        var sql =
                """
                UPDATE BLOCKS SET unblock_at = ?, unblock_reason = ? WHERE card_id = ? AND unblock_reason IS NULL;
                """;
        try(var statement = connection.prepareStatement(sql)){
            var i = 1;
            statement.setTimestamp(i++, OffsetDateTimeConverter.toTimestamp(OffsetDateTime.now()));
            statement.setString(i++, reason);
            statement.setLong(i, cardId);
            statement.executeUpdate();
        }
    }
}
