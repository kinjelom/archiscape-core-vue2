package net.archiscape.app.core.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.time.LocalDate;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.springframework.data.domain.Persistable;

/**
 * A ElementConfiguration.
 */
@Entity
@Table(name = "element_configuration")
public class ElementConfiguration implements Serializable, Persistable<String> {

    private static final long serialVersionUID = 1L;

    @NotNull
    @Size(max = 30)
    @Id
    @Column(name = "id", length = 30, nullable = false)
    private String id;

    @NotNull
    @Size(max = 50)
    @Column(name = "name", length = 50, nullable = false)
    private String name;

    @NotNull
    @Size(max = 2048)
    @Column(name = "documentation", length = 2048, nullable = false)
    private String documentation;

    @NotNull
    @Size(max = 50)
    @Column(name = "technology", length = 50, nullable = false)
    private String technology;

    @Column(name = "eol_date")
    private LocalDate eolDate;

    @Transient
    private boolean isPersisted;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(value = { "users" }, allowSetters = true)
    private Team team;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public String getId() {
        return this.id;
    }

    public ElementConfiguration id(String id) {
        this.setId(id);
        return this;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public ElementConfiguration name(String name) {
        this.setName(name);
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDocumentation() {
        return this.documentation;
    }

    public ElementConfiguration documentation(String documentation) {
        this.setDocumentation(documentation);
        return this;
    }

    public void setDocumentation(String documentation) {
        this.documentation = documentation;
    }

    public String getTechnology() {
        return this.technology;
    }

    public ElementConfiguration technology(String technology) {
        this.setTechnology(technology);
        return this;
    }

    public void setTechnology(String technology) {
        this.technology = technology;
    }

    public LocalDate getEolDate() {
        return this.eolDate;
    }

    public ElementConfiguration eolDate(LocalDate eolDate) {
        this.setEolDate(eolDate);
        return this;
    }

    public void setEolDate(LocalDate eolDate) {
        this.eolDate = eolDate;
    }

    @Transient
    @Override
    public boolean isNew() {
        return !this.isPersisted;
    }

    public ElementConfiguration setIsPersisted() {
        this.isPersisted = true;
        return this;
    }

    @PostLoad
    @PostPersist
    public void updateEntityState() {
        this.setIsPersisted();
    }

    public Team getTeam() {
        return this.team;
    }

    public void setTeam(Team team) {
        this.team = team;
    }

    public ElementConfiguration team(Team team) {
        this.setTeam(team);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ElementConfiguration)) {
            return false;
        }
        return id != null && id.equals(((ElementConfiguration) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ElementConfiguration{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", documentation='" + getDocumentation() + "'" +
            ", technology='" + getTechnology() + "'" +
            ", eolDate='" + getEolDate() + "'" +
            "}";
    }
}
