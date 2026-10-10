package io.bootify.my_app.berth;

import io.bootify.my_app.berth_allocation.BerthAllocation;
import io.bootify.my_app.coach.Coach;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import java.util.HashSet;
import java.util.Set;
import lombok.Getter;
import lombok.Setter;


@Entity
@Table(
        uniqueConstraints = @UniqueConstraint(columnNames = {"coach_id", "number"})
)
@Getter
@Setter
public class Berth {

    @Id
    @Column(nullable = false, updatable = false)
    private String id;

    @Column(nullable = false)
    private String number;

    @Column(nullable = false)
    private String kind;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "coach_id", nullable = false)
    private Coach coach;

    @OneToMany(mappedBy = "berth")
    private Set<BerthAllocation> berthBerthAllocations = new HashSet<>();

}
