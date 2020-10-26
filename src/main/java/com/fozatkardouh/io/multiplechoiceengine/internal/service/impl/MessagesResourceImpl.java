package com.fozatkardouh.io.multiplechoiceengine.internal.service.impl;

import com.fozatkardouh.io.multiplechoiceengine.internal.service.api.MessagesResource;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.Locale;

@Component
@RequiredArgsConstructor
public class MessagesResourceImpl implements MessagesResource {

    private final Locale locale;
    private final MessageSource messageSource;
    private MessageSourceAccessor accessor;

    @PostConstruct
    private void init() {
        accessor = new MessageSourceAccessor(messageSource, locale);
    }

    @Override
    public String retrieveMessage(String messageKey) {
        return accessor.getMessage(messageKey);
    }

}
