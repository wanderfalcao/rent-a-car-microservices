package br.com.infnet.wander.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.v3.oas.annotations.media.Schema;

import lombok.*;
import javax.persistence.*;
import javax.validation.constraints.*;
import java.math.BigInteger;
import java.util.*;

@Entity
@Table(	name = "users",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = "username"),
                @UniqueConstraint(columnNames = "email")
        })
@AllArgsConstructor
@NoArgsConstructor
@Data
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonSerialize(using = ToStringSerializer.class)
    private BigInteger id;

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

    @Pattern(regexp = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$")
    @Size(max = 64)
    @Schema(name = "email", required = true)
    @JsonProperty("email")
    private String email;

    @Schema(name = "password", required = true)
    @JsonProperty("password")
    private String password;

    @OneToMany(fetch = FetchType.LAZY)
    @JoinTable(	name = "user_roles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id"))
    private Set<Role> roles = new HashSet<>();

    public Set<Role> getRoles() {
        return roles;
    }

    public void setRoles(Set<Role> roles) {
        this.roles = roles;
    }

    public User(String firstname, String lastname, String email, String encode) {
        this.firstname = firstname;
        this.lastname = lastname;
        this.email = email;
        this.password = encode;
    }
}