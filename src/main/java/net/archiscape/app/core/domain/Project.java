package net.archiscape.app.core.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import javax.persistence.*;
import javax.validation.constraints.*;

/**
 * A Project.
 */
@Entity
@Table(name = "project")
public class Project implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Size(max = 50)
    @Column(name = "name", length = 50, nullable = false)
    private String name;

    @Column(name = "description")
    private String description;

    @Size(max = 100)
    @Column(name = "ext_project_id", length = 100)
    private String extProjectId;

    @NotNull
    @Column(name = "active", nullable = false)
    private Boolean active;

    @Size(max = 2048)
    @Column(name = "src_url", length = 2048)
    private String srcUrl;

    @Size(max = 2048)
    @Column(name = "content_store_url", length = 2048)
    private String contentStoreUrl;

    @ManyToOne(optional = false)
    @NotNull
    private Landscape landscape;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(value = { "users" }, allowSetters = true)
    private Team team;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Project id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public Project name(String name) {
        this.setName(name);
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return this.description;
    }

    public Project description(String description) {
        this.setDescription(description);
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getExtProjectId() {
        return this.extProjectId;
    }

    public Project extProjectId(String extProjectId) {
        this.setExtProjectId(extProjectId);
        return this;
    }

    public void setExtProjectId(String extProjectId) {
        this.extProjectId = extProjectId;
    }

    public Boolean getActive() {
        return this.active;
    }

    public Project active(Boolean active) {
        this.setActive(active);
        return this;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public String getSrcUrl() {
        return this.srcUrl;
    }

    public Project srcUrl(String srcUrl) {
        this.setSrcUrl(srcUrl);
        return this;
    }

    public void setSrcUrl(String srcUrl) {
        this.srcUrl = srcUrl;
    }

    public String getContentStoreUrl() {
        return this.contentStoreUrl;
    }

    public Project contentStoreUrl(String contentStoreUrl) {
        this.setContentStoreUrl(contentStoreUrl);
        return this;
    }

    public void setContentStoreUrl(String contentStoreUrl) {
        this.contentStoreUrl = contentStoreUrl;
    }

    public Landscape getLandscape() {
        return this.landscape;
    }

    public void setLandscape(Landscape landscape) {
        this.landscape = landscape;
    }

    public Project landscape(Landscape landscape) {
        this.setLandscape(landscape);
        return this;
    }

    public Team getTeam() {
        return this.team;
    }

    public void setTeam(Team team) {
        this.team = team;
    }

    public Project team(Team team) {
        this.setTeam(team);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Project)) {
            return false;
        }
        return id != null && id.equals(((Project) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Project{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", description='" + getDescription() + "'" +
            ", extProjectId='" + getExtProjectId() + "'" +
            ", active='" + getActive() + "'" +
            ", srcUrl='" + getSrcUrl() + "'" +
            ", contentStoreUrl='" + getContentStoreUrl() + "'" +
            "}";
    }
}
