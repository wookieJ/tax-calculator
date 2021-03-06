package com.bartoszwalter.students.contracts;

import com.bartoszwalter.students.model.DescriptionAmount;

import java.text.DecimalFormat;
import java.util.LinkedHashMap;
import java.util.Map;

public class ContractOfEmployment extends Contract{

    private double costsOfObtaining;
    private double taxReducingAmount;
    private DecimalFormat formaterTwoDigitsAfterComa;
    private DecimalFormat formatterWholeNumber;

    public ContractOfEmployment(double costsOfObtaining, double taxReducingAmount)
    {
        this.costsOfObtaining = costsOfObtaining; // 111.25;
        this.taxReducingAmount = taxReducingAmount; // 46.33;
        formaterTwoDigitsAfterComa = new DecimalFormat("#.00");
        formatterWholeNumber = new DecimalFormat("#");
    }

    public Map<String, DescriptionAmount> calculate(double salary) {
        Map<String, DescriptionAmount> calculatedValuesAndDescriptions = new LinkedHashMap<String, DescriptionAmount>();

        calculatedValuesAndDescriptions.put("salary", new DescriptionAmount("Podstawa wymiaru składek ", salary, Double.toString(salary)));

        double retirementPayment = calculateRetirementPayment(salary);
        calculatedValuesAndDescriptions.put("retirement", new DescriptionAmount("Składka na ubezpieczenie emerytalne ", retirementPayment, formaterTwoDigitsAfterComa.format(retirementPayment)));

        double rentPayment = calculateRentPayment(salary);
        calculatedValuesAndDescriptions.put("rent", new DescriptionAmount("Składka na ubezpieczenie rentowe ", rentPayment, formaterTwoDigitsAfterComa.format(rentPayment)));

        double healthInsurance = calculateHealthInsurance(salary);
        calculatedValuesAndDescriptions.put("healthInsurance", new DescriptionAmount("Składka na ubezpieczenie chorobowe ", healthInsurance, formaterTwoDigitsAfterComa.format(healthInsurance)));

        double salaryWithoutInsurences = calculateWithoutInsurances(salary, retirementPayment, rentPayment, healthInsurance);
        calculatedValuesAndDescriptions.put("salaryWithoutInsurences", new DescriptionAmount("Podstawa wymiaru składki na ubezpieczenie zdrowotne: ", salaryWithoutInsurences, Double.toString(salaryWithoutInsurences)));

        double healthContribution7per = calculateInsuranceSeven(salaryWithoutInsurences);
        calculatedValuesAndDescriptions.put("healthContributionSeven", new DescriptionAmount("Składka na ubezpieczenie zdrowotne: 7% = ", healthContribution7per, formaterTwoDigitsAfterComa.format(healthContribution7per)));

        double healthContribution9per = calculateInsuranceNine(salaryWithoutInsurences);
        calculatedValuesAndDescriptions.put("healthContributionNine", new DescriptionAmount("Składka na ubezpieczenie zdrowotne: 9% = ", healthContribution9per, formaterTwoDigitsAfterComa.format(healthContribution9per)));

        calculatedValuesAndDescriptions.put("obtainingCosts", new DescriptionAmount("Koszty uzyskania przychodu w stałej wysokości ", costsOfObtaining, Double.toString(costsOfObtaining)));

        double taxedSalary = calculateTaxedSalary(salaryWithoutInsurences, costsOfObtaining);
        calculatedValuesAndDescriptions.put("taxedSalary", new DescriptionAmount("Podstawa opodatkowania ", taxedSalary, Double.toString(taxedSalary)));

        double taxedSalaryRounded = roundToWholeNumber(taxedSalary);
        calculatedValuesAndDescriptions.put("taxedSalaryRounded", new DescriptionAmount("Podstawa opodatkowania zaokrąglona ", taxedSalaryRounded, formatterWholeNumber.format(taxedSalaryRounded)));

        double taxPrepayment = calculateTax(taxedSalaryRounded);
        calculatedValuesAndDescriptions.put("taxPrepayment", new DescriptionAmount("Zaliczka na podatek dochodowy 18 % = ", taxPrepayment, Double.toString(taxPrepayment)));

        calculatedValuesAndDescriptions.put("taxReducing", new DescriptionAmount("Kwota wolna od podatku = ", taxReducingAmount, Double.toString(taxReducingAmount)));

        double fullTax = calculateFullTax(taxPrepayment, taxReducingAmount);
        calculatedValuesAndDescriptions.put("fullTax", new DescriptionAmount("Podatek potrącony = ", fullTax, formaterTwoDigitsAfterComa.format(fullTax)));

        double advancedPaymentTaxOffice = calculateAdvancePayment(taxPrepayment, healthContribution7per, taxReducingAmount);
        calculatedValuesAndDescriptions.put("taxOffice", new DescriptionAmount("Zaliczka do urzędu skarbowego = ", advancedPaymentTaxOffice, formaterTwoDigitsAfterComa.format(advancedPaymentTaxOffice)));

        double advancedPaymentTaxOfficeRounded = roundToWholeNumber(advancedPaymentTaxOffice);
        calculatedValuesAndDescriptions.put("taxOfficeRounded", new DescriptionAmount("Zaliczka do urzędu skarbowego po zaokrągleniu = ", advancedPaymentTaxOfficeRounded, formatterWholeNumber.format(advancedPaymentTaxOfficeRounded)));

        double salaryNetto = calculateSalaryNetto(salary, retirementPayment, rentPayment, healthInsurance, healthContribution9per,advancedPaymentTaxOfficeRounded);
        calculatedValuesAndDescriptions.put("salaryNetto", new DescriptionAmount("Pracownik otrzyma wynagrodzenie netto w wysokości = ", salaryNetto, formaterTwoDigitsAfterComa.format(salaryNetto)));

        return calculatedValuesAndDescriptions;
    }

    @Override
    public String getHeader() {
        return "UMOWA O PRACĘ";
    }
}
