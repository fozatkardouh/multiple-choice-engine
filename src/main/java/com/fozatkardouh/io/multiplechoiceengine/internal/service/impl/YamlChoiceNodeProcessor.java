package com.fozatkardouh.io.multiplechoiceengine.internal.service.impl;

import com.fozatkardouh.io.multiplechoiceengine.api.model.Choice;
import com.fozatkardouh.io.multiplechoiceengine.internal.model.YamlChoiceNode;
import com.fozatkardouh.io.multiplechoiceengine.internal.service.api.MessagesResource;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.Constructor;

import java.io.InputStream;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class YamlChoiceNodeProcessor {

    protected final static String YAML_FILES_LOCATION_PATH = "yaml/";

    private final MessagesResource messagesResource;

    private final ThreadLocal<Yaml> yamlParser = ThreadLocal.withInitial(() -> new Yaml(new Constructor(YamlChoiceNode.class)));

    public Choice parseYamlChoiceNodeToChoice(String yamlFileName) {
        InputStream inputStream = this.getClass()
                                      .getClassLoader()
                                      .getResourceAsStream(YAML_FILES_LOCATION_PATH + yamlFileName);

        YamlChoiceNode parentYamlChoiceNode = yamlParser.get()
                                                        .load(inputStream);
        return mapToChoice(parentYamlChoiceNode);
    }

    private Choice mapToChoice(YamlChoiceNode yamlChoiceNode) {
        return Choice.builder()
                     .id(yamlChoiceNode.getId())
                     .message(messagesResource.retrieveMessage(yamlChoiceNode.getMessageKey()))
                     .active(yamlChoiceNode.getActive())
                     .availableSubChoices(yamlChoiceNode.getAvailableSubChoices()
                                                        .stream()
                                                        .filter(YamlChoiceNode::getActive)
                                                        .map(this::mapToChoice)
                                                        .collect(Collectors.toList()))
                     .build();
    }

}
