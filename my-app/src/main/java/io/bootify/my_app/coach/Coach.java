package io.bootify.my_app.coach;

import io.bootify.my_app.berth.Berth;
import io.bootify.my_app.train.Train;
import io.bootify.my_app.travel_class.TravelClass;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
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
        uniqueConstraints = @UniqueConstraint(columnNames = {"train_number_id", "code"}),
        indexes = @Index(columnList = "train_number_id,class_code_id")
)
@Getter
@Setter
public class Coach {

    @Id
    @Column(nullable = false, updatable = false)
    private String id;

    @Column(nullable = false, length = 4)
    private String code;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "train_number_id", nullable = false)
    private Train trainNumber;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "class_code_id", nullable = false)
    private TravelClass classCode;

    @OneToMany(mappedBy = "coach")
    private Set<Berth> coachBerths = new HashSet<>();

}
