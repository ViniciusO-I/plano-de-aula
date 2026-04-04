package br.com.fiap.skill_hub.response;

import java.time.LocalDateTime;

public record GroupResponse(String name, String description, Integer maxNumbers,
                            LocalDateTime createdAt, String ownerName,
                            Integer currentNumbers) {


}


//private Long id;
//private String name;
//private String description;
//
//private Integer maxNumbers;  // regra limite  de membros por grupo
//
//private LocalDateTime createdAt;