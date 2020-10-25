package com.fozatkardouh.io.multiplechoiceengine.internal.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class YamlChoiceNode {

    private String id;
    private String messageKey;
    private Boolean active;
    private List<YamlChoiceNode> availableSubChoices;

}
