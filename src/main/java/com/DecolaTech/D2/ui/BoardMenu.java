package com.DecolaTech.D2.ui;

import com.DecolaTech.D2.persistence.config.ConnectionConfig;
import com.DecolaTech.D2.persistence.dto.BoardColumnInfoDTO;
import com.DecolaTech.D2.persistence.entity.BoardColumnEntity;
import com.DecolaTech.D2.persistence.entity.BoardColumnKindEnum;
import com.DecolaTech.D2.persistence.entity.BoardEntity;
import com.DecolaTech.D2.persistence.entity.CardEntity;
import com.DecolaTech.D2.service.BoardColumnQueryService;
import com.DecolaTech.D2.service.BoardQueryService;
import com.DecolaTech.D2.service.CardQueryService;
import com.DecolaTech.D2.service.CardService;
import lombok.AllArgsConstructor;

import java.sql.SQLException;
import java.util.Objects;
import java.util.Scanner;

import static com.DecolaTech.D2.persistence.config.ConnectionConfig.getConnection;

@AllArgsConstructor
public class BoardMenu {

    private final BoardEntity entity;
    private final Scanner scanner = new Scanner(System.in).useDelimiter("\n");

    public void execute() {
        try {
            System.out.printf("Bem vindo ao board %s de boards, escolha a opção desejada:\n", entity.getId());
            var option = -1;

            while (option != 9) {
                System.out.println("1 - Criar um card");
                System.out.println("2 - Mover um card");
                System.out.println("3 - Bloquear um card");
                System.out.println("4 - Desbloquear um card");
                System.out.println("5 - Cancelar um card");
                System.out.println("6 - Ver board");
                System.out.println("7 - Ver colunas com cards");
                System.out.println("8 - Ver cards");
                System.out.println("9 - Voltar para o menu anterior um card");
                System.out.println("10 - Sair");
                option = scanner.nextInt();

                switch (option) {
                    case 1 -> createCard();
                    case 2 -> moveCardToNextColumn();
                    case 3 -> blockCard();
                    case 4 -> unblockCard();
                    case 5 -> cancelCard();
                    case 6 -> showBoard();
                    case 7 -> showColumn();
                    case 8 -> showCard();
                    case 9 -> System.out.println("Voltando para o menu anterior");
                    case 10 -> System.exit(0);
                    default -> System.out.println("Opção iválida, selecione uma opção do menu!");
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private void createCard() throws SQLException{
        var card = new CardEntity();
        System.out.println("Informe o título do card");
        card.setTitle(scanner.next());
        System.out.println("Informe a descrição do card");
        card.setDescription(scanner.next());
        card.setBoardColumn(entity.getInitialColumn());
        try(var connection = ConnectionConfig.getConnection()){
            new CardService(connection).insert(card);
        }
    }

    private void moveCardToNextColumn() throws SQLException {
        System.out.println("Informe o id do card que deseja mover para a próxima coluna");
        var cardId = scanner.nextLong();
        var boardColumnsInfo = entity.getBoardColumns().stream()
                .map(bc -> new BoardColumnInfoDTO(bc.getId(), bc.getOrder(),bc.getKind()))
                .toList();

        try(var connection = ConnectionConfig.getConnection()){
            new CardService(connection).moveToNextColumn(cardId, boardColumnsInfo);
        }catch (RuntimeException ex){
            System.out.println(ex.getMessage());
        }

    }

    private void blockCard() {
    }

    private void unblockCard() {
    }

    private void cancelCard() throws SQLException {
        System.out.println("Informe o id do card que deseja mover para a coluna de cancelamento");
        var cardId = scanner.nextLong();
        var cancelColumn = entity.getCancelColumn();
        var boardColumnsInfo = entity.getBoardColumns().stream()
                .map(bc -> new BoardColumnInfoDTO(bc.getId(), bc.getOrder(), bc.getKind()))
                .toList();
        try(var connection = getConnection()){
            new CardService(connection).cancel(cardId, cancelColumn.getId(), boardColumnsInfo);
        } catch (RuntimeException ex){
            System.out.println(ex.getMessage());
        }
    }

    private void showBoard() throws SQLException {
        try(var connection = ConnectionConfig.getConnection()){
           var optional = new BoardQueryService(connection).showBoardDetails(entity.getId());
           optional.ifPresent(b ->{
                System.out.printf("Board [%s,%s]\n", b.id(), b.name());
                b.columns().forEach(c -> {
                    System.out.printf("Coluna [%s] tipo [%S] tem %s cards\n", c.name(), c.kind(), c.cardsAmount());
                });
            });
        }
    }

    private void showColumn() throws SQLException {
        System.out.printf("Escolha uma coluna do board %s\n", entity.getName());
        var columnsIds = entity.getBoardColumns().stream().map(BoardColumnEntity::getId).toList();
        var selectedColumn = -1L;
        while(!columnsIds.contains(selectedColumn)){
            entity.getBoardColumns().forEach(c -> System.out.printf("%s - %s [%s]\n", c.getId(), c.getName(), c.getKind()));
            selectedColumn = scanner.nextLong();
        }
        try(var connection = ConnectionConfig.getConnection()){
           var column =  new BoardColumnQueryService(connection).findById(selectedColumn);
           column.ifPresent(co -> {
               System.out.printf("Coluna %s tipo %s\n", co.getName(), co.getKind());
               co.getCards().forEach(ca -> System.out.printf("Card %s - %s\nDescrição: %s\n",
                       ca.getId(), ca.getTitle(), ca.getDescription()));
           });
        }
    }

    private void showCard() throws SQLException {
        System.out.println("Informe o id do card que deseja visualizar");
        var selectedCardId = scanner.nextLong();
        try(var connection = ConnectionConfig.getConnection()){
            new CardQueryService(connection).findById(selectedCardId)
                    .ifPresentOrElse(
                            c -> {
                                System.out.printf("Card %s - %s.\n", c.id(), c.title());
                                System.out.printf("Descrição: %s\n", c.description());
                                System.out.println(c.blocked()
                                                 ? "Está bloqueado pelo motivo: " + Objects.toString(c.blockReason(), "Motivo não informado")
                                                 : "Não está bloqueado");
                                System.out.printf("Já foi bloqueado %s vezes\n", c.blocksAmount());
                                System.out.printf("Está no momento na coluna %s - %s\n", c.columnId(), c.columnName());
                            },
                            () -> System.out.printf("Não existe um card com id %s\n", selectedCardId));
        }
    }
}
