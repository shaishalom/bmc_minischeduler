package com.bmc.minischduler.model;

import com.bmc.minischduler.constant.CommandType;

public record ScheduleTaskDTO(
    String id,
    String name,
    CommandType type,
    String details,
    String time,
    String basedOn
) { }
