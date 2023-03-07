package csc1035.project2.DatabaseTables;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "tblUser")
public class TblUser {
    @Id
    @Column(name = "Username", nullable = false, length = 50)
    private String username;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) { this.username = username; }
}