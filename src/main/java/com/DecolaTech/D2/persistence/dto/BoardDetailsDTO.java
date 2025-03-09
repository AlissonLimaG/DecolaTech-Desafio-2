package com.DecolaTech.D2.persistence.dto;

import lombok.AllArgsConstructor;

import java.util.List;

public record BoardDetailsDTO(
        Long id,
        String name,
        List<BoardColumnDTO> columns
) {
}
