package com.farukkaradeniz.log4jexperiments;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Model {
    private String uid;
    private String username;
    private String email;
}
