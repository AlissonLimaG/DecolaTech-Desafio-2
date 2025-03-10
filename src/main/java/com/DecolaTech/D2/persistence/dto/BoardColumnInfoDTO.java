package com.DecolaTech.D2.persistence.dto;

import com.DecolaTech.D2.persistence.entity.BoardColumnKindEnum;

public record BoardColumnInfoDTO(Long id, int order, BoardColumnKindEnum kind) {
}
