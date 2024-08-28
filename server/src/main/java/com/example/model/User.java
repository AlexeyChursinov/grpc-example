package com.example.model;

import jakarta.persistence.*;
import lombok.*;
import com.example.grpc.GRPCUserRequest;

@Entity
@Table(name = "users")
@NoArgsConstructor
@Getter
@Setter
@ToString
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    private String email;

    @ManyToOne
    @JoinColumn(name = "region_id", referencedColumnName = "id")
    @ToString.Exclude
    private Region region;

    public User(GRPCUserRequest request, Region region) {
        this.firstName = request.getFirstName();
        this.lastName = request.getLastName();
        this.email = request.getEmail();
        this.region = region;
    }

}
