package com.example.web.dto;

import lombok.*;

@NoArgsConstructor
@Getter
@Setter
@ToString
@Builder
@AllArgsConstructor
public class RegionDto {

    private String regionName;
    private int regionCode;

}
