package org.example.orchidsstore.Entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Table(name = "orchids")
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Orchid {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long orchidId;

    private boolean isNatural;

    @Column(name = "description", columnDefinition = "NVARCHAR(255)")
    private String orchidDescription;

    @Column(name = "orchidName", columnDefinition = "NVARCHAR(255)")
    private String orchidName;

    @Column(name = "orchidUrl", columnDefinition = "NVARCHAR(255)")
    private String orchidUrl;

    private double price;

    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;

    @OneToMany(mappedBy = "orchid")
    private List<OrderDetail> orderDetails;
}
