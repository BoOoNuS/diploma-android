package ua.nure.heating_system_mobile.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

/**
 * The user model.
 *
 * @author Stanislav_Vorozhka
 */
@Data
@NoArgsConstructor
@RequiredArgsConstructor
public class User {

    private long id;
    @NonNull
    private String login;
    @NonNull
    private String password;
    @NonNull
    private String fullName;
    @NonNull
    private UserRole role;
    @NonNull
    private boolean blocked;
}