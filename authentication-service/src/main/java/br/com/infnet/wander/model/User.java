package br.com.infnet.wander.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Entity
@Table(	name = "users",
        uniqueConstraints = @UniqueConstraint(columnNames = "email")
        )
@AllArgsConstructor
@NoArgsConstructor
@Data
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonSerialize(using = ToStringSerializer.class)
    private Long id;

    @Pattern(regexp = "^([\\w. ]+)$")
    @Size(max = 64)
    @Schema(name = "firstname", required = true)
    @JsonProperty("firstname")
    private String firstname;

    @Pattern(regexp = "^([\\w. ]+)$")
    @Size(max = 64)
    @Schema(name = "lastname", required = true)
    @JsonProperty("lastname")
    private String lastname;

    @Size(max = 64)
    @Schema(name = "email", required = true)
    @Email
    @JsonProperty("email")
    private String email;

    @Schema(name = "password", required = true)
    @JsonProperty("password")
    private String password;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "role_id")
    private Role role ;

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public User(String firstname, String lastname, String email, String encode) {
        this.firstname = firstname;
        this.lastname = lastname;
        this.email = email;
        this.password = encode;
    }

}