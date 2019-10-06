package com.insurance.domain;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.Instant;

import com.insurance.domain.enumeration.InsuranceType;

import com.insurance.domain.enumeration.RiskType;

/**
 * A Insurance.
 */
@Entity
@Table(name = "insurance")
public class Insurance implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "description")
    private String description;

    @NotNull
    @Column(name = "coverage_percentage", nullable = false)
    private Integer coveragePercentage;

    @NotNull
    @Column(name = "start_date", nullable = false)
    private Instant startDate;

    @NotNull
    @Column(name = "coverage_period", nullable = false)
    private Integer coveragePeriod;

    @NotNull
    @Column(name = "price", precision = 21, scale = 2, nullable = false)
    private BigDecimal price;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "insurance_type", nullable = false)
    private InsuranceType insuranceType;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "risk_type", nullable = false)
    private RiskType riskType;

    @ManyToOne
    @JsonIgnoreProperties("insurances")
    private User user;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public Insurance name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public Insurance description(String description) {
        this.description = description;
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getCoveragePercentage() {
        return coveragePercentage;
    }

    public Insurance coveragePercentage(Integer coveragePercentage) {
        this.coveragePercentage = coveragePercentage;
        return this;
    }

    public void setCoveragePercentage(Integer coveragePercentage) {
        this.coveragePercentage = coveragePercentage;
    }

    public Instant getStartDate() {
        return startDate;
    }

    public Insurance startDate(Instant startDate) {
        this.startDate = startDate;
        return this;
    }

    public void setStartDate(Instant startDate) {
        this.startDate = startDate;
    }

    public Integer getCoveragePeriod() {
        return coveragePeriod;
    }

    public Insurance coveragePeriod(Integer coveragePeriod) {
        this.coveragePeriod = coveragePeriod;
        return this;
    }

    public void setCoveragePeriod(Integer coveragePeriod) {
        this.coveragePeriod = coveragePeriod;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public Insurance price(BigDecimal price) {
        this.price = price;
        return this;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public InsuranceType getInsuranceType() {
        return insuranceType;
    }

    public Insurance insuranceType(InsuranceType insuranceType) {
        this.insuranceType = insuranceType;
        return this;
    }

    public void setInsuranceType(InsuranceType insuranceType) {
        this.insuranceType = insuranceType;
    }

    public RiskType getRiskType() {
        return riskType;
    }

    public Insurance riskType(RiskType riskType) {
        this.riskType = riskType;
        return this;
    }

    public void setRiskType(RiskType riskType) {
        this.riskType = riskType;
    }

    public User getUser() {
        return user;
    }

    public Insurance user(User user) {
        this.user = user;
        return this;
    }

    public void setUser(User user) {
        this.user = user;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Insurance)) {
            return false;
        }
        return id != null && id.equals(((Insurance) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "Insurance{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", description='" + getDescription() + "'" +
            ", coveragePercentage=" + getCoveragePercentage() +
            ", startDate='" + getStartDate() + "'" +
            ", coveragePeriod=" + getCoveragePeriod() +
            ", price=" + getPrice() +
            ", insuranceType='" + getInsuranceType() + "'" +
            ", riskType='" + getRiskType() + "'" +
            "}";
    }
}
