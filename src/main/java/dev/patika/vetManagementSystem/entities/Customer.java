package dev.patika.vetManagementSystem.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.List;

@Entity
@Table(name = "customers")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Customer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "customer_id", columnDefinition = "serial")
    private long id; // customer_id

    @NotNull
    @NotEmpty
    @Column(name = "customer_name")
    private String name; // customer_name

    @NotNull
    @NotEmpty
    @Column(name = "customer_phone")
    private String phone; // customer_phone

    @NotNull
    @NotEmpty
    @Column(name = "customer_mail")
    private String mail; // customer_mail

    @NotNull
    @NotEmpty
    @Column(name = "customer_address")
    private String address; // customer_address

    @NotNull
    @NotEmpty
    @Column(name = "customer_city")
    private String city; // customer_city

    @OneToMany(mappedBy = "customer" , cascade = CascadeType.REMOVE)
    private List<Animal> animalList;

    @Override
    public String toString() {
        return "Customer{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", phone='" + phone + '\'' +
                ", mail='" + mail + '\'' +
                ", address='" + address + '\'' +
                ", city='" + city + '\'' +
                '}';
    }
}
