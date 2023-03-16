package ua.navpil.reactordemo;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Table("users")
public class UserEntity {

    @Id
    private String id;
    private String name;

    private String manager;

    public UserEntity() {
    }

    public UserEntity(String id, String name, String manager) {
        this.id = id;
        this.name = name;
        this.manager = manager;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getManager() {
        return manager;
    }

    public void setManager(String manager) {
        this.manager = manager;
    }
}
