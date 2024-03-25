package com.bmc.minischduler.model;

import com.bmc.minischduler.constant.CommandType;
import com.fasterxml.jackson.annotation.JsonProperty;


public record SchedulerTask(
        @JsonProperty("id") String id,
        @JsonProperty("name") String name,
        @JsonProperty("type") CommandType type,
        @JsonProperty("details") String details,
        @JsonProperty("time") String time,
        @JsonProperty("basedOn") String basedOn
) {
}
