package com.DecolaTech.D2.persistence.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

@Data
public class BoardEntity {

    private Long id;
    private String name;
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private List<BoardColumnEntity> boardColumns = new ArrayList<>();

    public BoardColumnEntity getInitialColumn(){
        return boardColumns.stream()
                .filter(bc -> bc.getKind().equals(BoardColumnKindEnum.INITIAL))
                .findFirst().orElseThrow();
    }
}
