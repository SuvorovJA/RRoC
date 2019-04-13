package ru.sua.rroc.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

//import java.util.Date;

/**
 *
 * поле 'dob' отключено намеренно, для визуального различения Dto и Domain в выдаче контроллера
 * в тестах данное поле не используется, исключая операции Create и Update.
 * для этих операций на контроллер тест передаёт Citizen Domain объект (что в некотором роде нарушение Dto-изоляции)
 *
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CitizenDTO {
    private long id;
    private String fullName;
//    private Date dob;
    private String address;
    private String dulnumber;
}
