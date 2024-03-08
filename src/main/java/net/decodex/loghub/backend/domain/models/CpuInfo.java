package net.decodex.loghub.backend.domain.models;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@ToString
@Getter
@Setter
@EqualsAndHashCode
public class CpuInfo {
    private String model = "unknown";
    private int seed = 0; // in Mhz
    private int cores = 0;
    private int threads = 0;
}
