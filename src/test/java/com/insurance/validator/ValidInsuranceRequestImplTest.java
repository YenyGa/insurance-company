package com.insurance.validator;

import com.insurance.domain.Insurance;
import com.insurance.domain.enumeration.InsuranceType;
import com.insurance.domain.enumeration.RiskType;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.Instant;

class ValidInsuranceRequestImplTest {
    private final ValidInsuranceRequestImpl validInsuranceRequestImpl = new ValidInsuranceRequestImpl();

    @Test
    void shouldBeValidForHighRisk() {
        Insurance insurance = new Insurance()
            .coveragePercentage(BigDecimal.valueOf(0.40))
            .startDate(Instant.now())
            .coveragePeriod(2L)
            .price(new BigDecimal(4000))
            .insuranceType(InsuranceType.EARTHQUAKE)
            .riskType(RiskType.HIGH);

        boolean result = validInsuranceRequestImpl.isValid(insurance, null);

        Assertions.assertTrue(result);
    }

    @Test
    void shouldNotBeValidForHighRisk() {
        Insurance insurance = new Insurance()
            .coveragePercentage(BigDecimal.valueOf(0.60))
            .startDate(Instant.now())
            .coveragePeriod(2L)
            .price(new BigDecimal(4000))
            .insuranceType(InsuranceType.EARTHQUAKE)
            .riskType(RiskType.HIGH);

        boolean result = validInsuranceRequestImpl.isValid(insurance, null);

        Assertions.assertFalse(result);
    }

    @Test
    void shouldBeValidForLowRisk() {
        Insurance insurance = new Insurance()
            .coveragePercentage(BigDecimal.valueOf(0.60))
            .startDate(Instant.now())
            .coveragePeriod(2L)
            .price(new BigDecimal(4000))
            .insuranceType(InsuranceType.EARTHQUAKE)
            .riskType(RiskType.LOW);

        boolean result = validInsuranceRequestImpl.isValid(insurance, null);

        Assertions.assertTrue(result);
    }
}
