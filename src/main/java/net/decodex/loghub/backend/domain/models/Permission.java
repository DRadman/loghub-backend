package net.decodex.loghub.backend.domain.models;

import lombok.*;
import net.decodex.loghub.backend.enums.PermissionValue;
import net.decodex.loghub.backend.enums.ResourceType;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@ToString
@Getter
@Setter
@EqualsAndHashCode
public class Permission {
    @NotNull
    private ResourceType type;

    @NotNull
    private List<PermissionValue> values = new ArrayList<>();
}
