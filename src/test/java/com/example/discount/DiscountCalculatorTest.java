package com.example.discount;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class DiscountCalculatorTest {

    private final DiscountCalculator calculator =
            new DiscountCalculator();

    @Test
    void shouldApplyPremiumDiscountForLargePurchase() {

        double result =
                calculator.calculateFinalPrice(1000, true);

        assertEquals(800, result);
    }

    @Test
    void shouldApplyPremiumDiscount() {

        double result =
                calculator.calculateFinalPrice(100, true);

        assertEquals(90, result);
    }

    @Test
    void shouldApplyRegularDiscountForLargePurchase() {

        double result =
                calculator.calculateFinalPrice(1000, false);

        assertEquals(950, result);
    }

    @Test
    void shouldNotApplyDiscount() {

        double result =
                calculator.calculateFinalPrice(100, false);

        assertEquals(100, result);
    }

    @Test
    void shouldThrowExceptionForNegativeAmount() {

        assertThrows(
                InvalidPurchaseException.class,
                () -> calculator.calculateFinalPrice(-10, true)
        );
    }
}
