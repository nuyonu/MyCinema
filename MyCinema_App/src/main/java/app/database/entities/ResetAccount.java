package app.database.entities;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@EntityScan
@Document(collection = "Reset")
@Getter
@Setter
//@AllArgsConstructor
public class ResetAccount {
    @Indexed(unique = true)
    @NotNull
    private String email;
    @Indexed(unique = true)
    @NotNull
    private String code;
    @Indexed(expireAfterSeconds=1440)
    private LocalDateTime registeredDate;

    public ResetAccount(@NotNull String email, @NotNull String code) {
        this.email = email;
        this.code = code;
    }





}
