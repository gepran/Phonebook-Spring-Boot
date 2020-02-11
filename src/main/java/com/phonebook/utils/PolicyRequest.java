package com.phonebook.utils;

import lombok.Getter;
import lombok.Setter;
import org.codehaus.jackson.annotate.JsonProperty;

import java.time.ZonedDateTime;
import java.util.List;

@Getter
@Setter
public class PolicyRequest {
    @JsonProperty("Date")
    private ZonedDateTime date;

    @JsonProperty("Ids")
    private List<String> ids;

    @JsonProperty("FromId")
    private String fromId;

    @JsonProperty("ControlFlags")
    private int controlFlags;
}
