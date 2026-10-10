package io.bootify.my_app.berth_occupancy;

import io.bootify.my_app.berth_allocation.BerthAllocation;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;


@Entity
@Table(indexes = @Index(columnList = "allocation_id"))
@Getter
@Setter
public class BerthOccupancy {

    @Id
    @Column(nullable = false, updatable = false)
    private String tripId;

    @Column(nullable = false)
    private String berthId;

    @Column(nullable = false)
    private String segmentIdx;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "allocation_id", nullable = false)
    private BerthAllocation allocation;

}
