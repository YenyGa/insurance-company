package com.insurance.validator;

import com.insurance.domain.Insurance;
import com.insurance.domain.enumeration.RiskType;
import lombok.RequiredArgsConstructor;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.math.BigDecimal;
import java.util.Optional;

@RequiredArgsConstructor
public class ValidInsuranceRequestImpl implements ConstraintValidator<ValidInsuranceRequest, Insurance> {

    private static BigDecimal MAX_PERCENTAGE = BigDecimal.valueOf(0.50);

    @Override
    public void initialize(final ValidInsuranceRequest constraintAnnotation) {

    }

    @Override
    public boolean isValid(final Insurance dto, final ConstraintValidatorContext constraintValidatorContext) {
        return Optional.of(dto.getRiskType())
            .filter(t -> t.equals(RiskType.HIGH))
            .map(a -> validateCoverage(dto.getCoveragePercentage()))
            .orElse(true);
    }

    private static boolean validateCoverage(BigDecimal coverage) {
        return coverage.compareTo(MAX_PERCENTAGE) <= 0;
    }

}
