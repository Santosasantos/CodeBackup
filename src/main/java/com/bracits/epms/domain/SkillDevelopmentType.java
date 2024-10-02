package com.bracits.epms.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;

/**
 * A SkillDevelopmentType.
 */
@Entity
@Table(name = "skill_development_type")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class SkillDevelopmentType implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "skilldevelopmentname", nullable = false)
    private String skilldevelopmentname;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public SkillDevelopmentType id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSkilldevelopmentname() {
        return this.skilldevelopmentname;
    }

    public SkillDevelopmentType skilldevelopmentname(String skilldevelopmentname) {
        this.setSkilldevelopmentname(skilldevelopmentname);
        return this;
    }

    public void setSkilldevelopmentname(String skilldevelopmentname) {
        this.skilldevelopmentname = skilldevelopmentname;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof SkillDevelopmentType)) {
            return false;
        }
        return getId() != null && getId().equals(((SkillDevelopmentType) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "SkillDevelopmentType{" +
            "id=" + getId() +
            ", skilldevelopmentname='" + getSkilldevelopmentname() + "'" +
            "}";
    }
}
