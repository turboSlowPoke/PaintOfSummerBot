package dbservices.entyties;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "addresses")
public class Address {
    @Id @Column(name = "id")
    private long id;
    @Column(name = "flat")
    private int flat;
    @Column(name = "floor")
    private int floor;
    @Column(name = "buildnumber")
    private int buildNumber;

    public Address() {
    }


    public void setApartment(Integer flat) {
        this.flat = flat;

        int floorsInHouse=18;
        int buildNumbersOnFloor=18;


        int tempfloor = flat/floorsInHouse;
        if (flat%floorsInHouse==0)
            floor=tempfloor+1;
        else
            floor=tempfloor+2;

        int endFlatOnPreviousFloor = tempfloor*floorsInHouse;

        if (endFlatOnPreviousFloor==flat)
            buildNumber=buildNumbersOnFloor;
        else
            buildNumber = flat-endFlatOnPreviousFloor;

    }

}
