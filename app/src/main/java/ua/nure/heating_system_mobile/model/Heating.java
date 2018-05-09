package ua.nure.heating_system_mobile.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

/**
 * The heating model.
 *
 * @author Stanislav_Vorozhka
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@RequiredArgsConstructor
public class Heating {

    private long id;
    @NonNull
    private String host;
    @NonNull
    private int port;
    @NonNull
    private String description;
}