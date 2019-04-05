package ru.sua.specification;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CitizenListRequest {
    private String fullName;
    private String dulnumber;
    private String address;
}
