package models;

import play.*;
import play.db.jpa.*;
import javax.persistence.*;
import java.util.*;

@Entity
public class Address {
    
    @Id
    public String id;

    public String street;
    public City city;
    public int number;
    
    public int[] bikes;
    
    @Transient
    public List<Dog> dogs;
    
}

