package com.fozatkardouh.io.multiplechoiceengine.api.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Choice {

    private String id;
    private String message;
    private Boolean active;
    private List<Choice> availableSubChoices;

}
