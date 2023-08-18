package dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter@Setter@AllArgsConstructor@NoArgsConstructor
public class User {
    private String full_name;
    private String email;
    private String password;
    private String magic_link;
    private String created;
    private String updated;
}
