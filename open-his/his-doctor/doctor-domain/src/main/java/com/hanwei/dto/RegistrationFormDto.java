package com.hanwei.dto;

import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;


@ApiModel(value = "com-hanwei-dto-RegistrationFormDto")
@Data
@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
public class RegistrationFormDto extends BaseDto {

    private static final long serialVersionUID = -7986714203916737199L;
    private PatientDto patientDto;
    private RegistrationDto registrationDto;
}

