package net.archiscape.app.core.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import javax.persistence.*;
import javax.validation.constraints.*;
import net.archiscape.app.core.domain.enumeration.C4ElementType;
import org.springframework.data.domain.Persistable;

/**
 * A LandscapeElement.
 */
@Entity
@Table(name = "landscape_element")
public class LandscapeElement implements Serializable, Persistable<String> {

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

    @Size(max = 50)
    @Column(name = "technology", length = 50)
    private String technology;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "c_4_type", nullable = false)
    private C4ElementType c4type;

    @Transient
    private boolean isPersisted;

    @ManyToOne(optional = false)
    @NotNull
    private Landscape landscape;

    @ManyToOne
    @JsonIgnoreProperties(value = { "team" }, allowSetters = true)
    private ElementConfiguration elementConfiguration;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public String getId() {
        return this.id;
    }

    public LandscapeElement id(String id) {
        this.setId(id);
        return this;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public LandscapeElement name(String name) {
        this.setName(name);
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDocumentation() {
        return this.documentation;
    }

    public LandscapeElement documentation(String documentation) {
        this.setDocumentation(documentation);
        return this;
    }

    public void setDocumentation(String documentation) {
        this.documentation = documentation;
    }

    public String getTechnology() {
        return this.technology;
    }

    public LandscapeElement technology(String technology) {
        this.setTechnology(technology);
        return this;
    }

    public void setTechnology(String technology) {
        this.technology = technology;
    }

    public C4ElementType getc4type() {
        return this.c4type;
    }

    public LandscapeElement c4type(C4ElementType c4type) {
        this.setc4type(c4type);
        return this;
    }

    public void setc4type(C4ElementType c4type) {
        this.c4type = c4type;
    }

    @Transient
    @Override
    public boolean isNew() {
        return !this.isPersisted;
    }

    public LandscapeElement setIsPersisted() {
        this.isPersisted = true;
        return this;
    }

    @PostLoad
    @PostPersist
    public void updateEntityState() {
        this.setIsPersisted();
    }

    public Landscape getLandscape() {
        return this.landscape;
    }

    public void setLandscape(Landscape landscape) {
        this.landscape = landscape;
    }

    public LandscapeElement landscape(Landscape landscape) {
        this.setLandscape(landscape);
        return this;
    }

    public ElementConfiguration getElementConfiguration() {
        return this.elementConfiguration;
    }

    public void setElementConfiguration(ElementConfiguration elementConfiguration) {
        this.elementConfiguration = elementConfiguration;
    }

    public LandscapeElement elementConfiguration(ElementConfiguration elementConfiguration) {
        this.setElementConfiguration(elementConfiguration);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof LandscapeElement)) {
            return false;
        }
        return id != null && id.equals(((LandscapeElement) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "LandscapeElement{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", documentation='" + getDocumentation() + "'" +
            ", technology='" + getTechnology() + "'" +
            ", c4type='" + getc4type() + "'" +
            "}";
    }
}
