package com.DecolaTech.D2.service;

import com.DecolaTech.D2.persistence.dao.BlockDAO;
import com.DecolaTech.D2.persistence.dao.CardDAO;
import com.DecolaTech.D2.persistence.dto.BoardColumnInfoDTO;
import com.DecolaTech.D2.persistence.entity.BoardColumnKindEnum;
import com.DecolaTech.D2.persistence.entity.CardEntity;
import com.DecolaTech.D2.persistence.exception.CardBlockedException;
import com.DecolaTech.D2.persistence.exception.CardFinishedException;
import com.DecolaTech.D2.persistence.exception.EntityNotFoundException;
import lombok.AllArgsConstructor;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

@AllArgsConstructor
public class CardService {

    private final Connection connection;

    public CardEntity insert(final CardEntity entity) throws SQLException{
        try{
            var dao = new CardDAO(connection);
            dao.insert(entity);
            connection.commit();
            return entity;
        }catch (SQLException ex){
            connection.rollback();
            throw ex;
        }
    }

    public void moveToNextColumn (final Long cardId, final List<BoardColumnInfoDTO> boardColumnsInfo) throws SQLException {
        try{
            var dao = new CardDAO(connection);
            var optional = dao.findById(cardId);
            var dto = optional.orElseThrow(()-> new EntityNotFoundException("O card de id %s não foi encontrado".formatted(cardId)));

            if(dto.blocked()){
                throw new CardBlockedException("O card está bloqueado, é necessário desbloqueá-lo para movê-lo".formatted(cardId));
            }

            var currentColumn = boardColumnsInfo.stream()
                    .filter(bc -> bc.id().equals(dto.columnId()))
                    .findFirst()
                    .orElseThrow(()-> new IllegalStateException("O card informado pertence a outro board"));

            if(currentColumn.kind().equals(BoardColumnKindEnum.FINAL)){
                throw new CardFinishedException("O card já foi finalizado.");
            }

            var nextColumn = boardColumnsInfo.stream()
                    .filter(bc -> bc.order() == currentColumn.order()+1)
                    .findFirst()
                    .orElseThrow(()-> new IllegalStateException("O card está cancelado"));

            dao.moveToColumn(nextColumn.id(), cardId);
            connection.commit();
        }catch (SQLException ex){
            connection.rollback();
            throw ex;
        }

    }

    public void cancel(final Long cardId, final Long cancelColumnId, final List<BoardColumnInfoDTO> boardColumnsInfo) throws SQLException {
        try {
            var dao = new CardDAO(connection);
            var optional = dao.findById(cardId);
            var dto = optional.orElseThrow(() -> new EntityNotFoundException("O card de id %s não foi encontrado".formatted(cardId)));

            if(dto.blocked()){
                throw new CardBlockedException("O card está bloqueado, é necessário desbloqueá-lo para movê-lo".formatted(cardId));
            }

            var currentColumn = boardColumnsInfo.stream()
                    .filter(bc -> bc.id().equals(dto.columnId()))
                    .findFirst()
                    .orElseThrow(()-> new IllegalStateException("O card informado pertence a outro board"));

            if(currentColumn.kind().equals(BoardColumnKindEnum.FINAL)){
                throw new CardFinishedException("O card já foi finalizado.");
            }

            boardColumnsInfo.stream()
                    .filter(bc -> bc.order() == currentColumn.order()+1)
                    .findFirst()
                    .orElseThrow(()-> new IllegalStateException("O card está cancelado"));

            dao.moveToColumn(cancelColumnId, cardId);
            connection.commit();
        } catch (SQLException ex) {
            connection.rollback();
            throw ex;
        }
    }

    public void block(final Long id, final String reason, final List<BoardColumnInfoDTO> boardColumnsInfo) throws SQLException{
        try{
            var dao = new CardDAO(connection);
            var optional = dao.findById(id);
            var dto = optional.orElseThrow(() -> new EntityNotFoundException("O card de id %s não foi encontrado".formatted(id)));

            if(dto.blocked()){
                throw new CardBlockedException("O card %s já está bloqueado".formatted(id));
            }
            var currentColumn = boardColumnsInfo.stream()
                    .filter(bc -> bc.id().equals(dto.columnId()))
                    .findFirst()
                    .orElseThrow();
            if(currentColumn.kind().equals(BoardColumnKindEnum.FINAL) || currentColumn.kind().equals(BoardColumnKindEnum.CANCEL)){
                var message = "O card está em um coluna do tipo %s e não pode ser bloqueado".formatted(currentColumn.kind());
                throw new IllegalStateException(message);
            }
            var blockDAO = new BlockDAO(connection);
            blockDAO.block(id, reason);
            connection.commit();;
        }catch (SQLException ex){
            connection.rollback();
            throw ex;
        }
    }

}


























