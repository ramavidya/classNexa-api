package com.enigma.ClassNexa.model.request;

import com.opencsv.bean.CsvBindByName;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UploadCsvRequest {

    @CsvBindByName(column = "email")
    private String email;

    @CsvBindByName(column = "name")
    private String name;

    @CsvBindByName(column = "gender")
    private String gender;

    @CsvBindByName(column = "address")
    private String address;

    @CsvBindByName(column = "phone_number")
    private String phoneNumber;

}
