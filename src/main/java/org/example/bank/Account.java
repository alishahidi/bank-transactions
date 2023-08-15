package org.example.bank;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@AllArgsConstructor
@ToString
public class Account {
    private Integer id;
    private String name;
    private Long balance;
    private Long firstBalance;
}
