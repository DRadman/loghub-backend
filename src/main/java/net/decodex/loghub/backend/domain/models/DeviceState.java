package net.decodex.loghub.backend.domain.models;

import lombok.*;
import org.jetbrains.annotations.NotNull;

@AllArgsConstructor
@NoArgsConstructor
@ToString
@Getter
@Setter
@EqualsAndHashCode
public class DeviceState {
    private double cpuUtilization = 0;
    private double ramUsage = 0;
    private int freeRam = 0;

    @NotNull
    private String locale = "unknown";

    @NotNull
    private String timezone = "unknown";
}
