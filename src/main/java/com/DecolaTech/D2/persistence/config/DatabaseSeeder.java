package com.DecolaTech.D2.persistence.config;

import com.DecolaTech.D2.persistence.dao.BoardDAO;
import com.DecolaTech.D2.persistence.entity.*;
import com.DecolaTech.D2.service.BoardService;
import com.DecolaTech.D2.service.CardService;
import lombok.RequiredArgsConstructor;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
public class DatabaseSeeder {

    private final Connection connection;

    //Metodo para popular o banco de dados
    public static void seeding() throws SQLException {
        try(var connection = ConnectionConfig.getConnection()){
            var boardService = new BoardService(connection);
            var cardService = new CardService(connection);
            var boardDAO = new BoardDAO(connection);

            if(!boardDAO.existsAny()) {
                BoardEntity boardSeed = new BoardEntity();
                boardSeed.setId(1L);
                boardSeed.setName("Estudos");

                BoardColumnEntity col1 = new BoardColumnEntity();
                col1.setId(1L);
                col1.setName("A fazer");
                col1.setOrder(1);
                col1.setKind(BoardColumnKindEnum.INITIAL);
                col1.setBoard(boardSeed);

                BoardColumnEntity col2 = new BoardColumnEntity();
                col2.setId(2L);
                col2.setName("Em progresso");
                col2.setOrder(2);
                col2.setKind(BoardColumnKindEnum.PENDING);
                col2.setBoard(boardSeed);

                BoardColumnEntity col3 = new BoardColumnEntity();
                col3.setId(3L);
                col3.setName("Completo");
                col3.setOrder(3);
                col3.setKind(BoardColumnKindEnum.FINAL);
                col3.setBoard(boardSeed);

                BoardColumnEntity col4 = new BoardColumnEntity();
                col4.setId(4L);
                col4.setName("Cancelado");
                col4.setOrder(4);
                col4.setKind(BoardColumnKindEnum.CANCEL);
                col4.setBoard(boardSeed);

                boardSeed.setBoardColumns(List.of(col1, col2, col3, col4));

                CardEntity c1 = new CardEntity();
                c1.setTitle("Garbage Collector");
                c1.setDescription("Ver vídeo de Kipper sobre garbage collector");
                c1.setBoardColumn(col1);

                CardEntity c2 = new CardEntity();
                c2.setTitle("Duolingo");
                c2.setDescription("Usar duolingo por 2H essa semana.");
                c2.setBoardColumn(col2);

                CardEntity c3 = new CardEntity();
                c3.setTitle("Desafio de projeto 2");
                c3.setDescription("Concluir o desafio de projeto 2");
                c3.setBoardColumn(col3);

                CardEntity c4 = new CardEntity();
                c4.setTitle("Inicar Angular");
                c4.setDescription("Iniciar o curso de Angular do DecolaTech");
                c4.setBoardColumn(col1);


                boardService.insert(boardSeed);
                for (CardEntity card : List.of(c1, c2, c3, c4)) cardService.insert(card);
            }
        }catch (SQLException ex){
            throw ex;
        }

    }
}
