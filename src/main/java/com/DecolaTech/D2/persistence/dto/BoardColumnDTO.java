package com.DecolaTech.D2.persistence.dto;

import com.DecolaTech.D2.persistence.entity.BoardColumnKindEnum;
import lombok.AllArgsConstructor;

public record BoardColumnDTO(
        Long id,
        String name,
        BoardColumnKindEnum kind,
        int cardsAmount
) {
}
