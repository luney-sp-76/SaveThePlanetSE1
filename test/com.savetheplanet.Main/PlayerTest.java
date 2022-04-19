package com.savetheplanet.Main;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class PlayerTest {

    Player p1;

    String validNameLower, validNameUpper, validNameMid;
    String invalidNameLengthLower, invalidNameLengthUpper;
    String invalidName1, invalidName2, invalidName3;
    int startingFunding;

    @BeforeEach
    void setUp()  {
        p1 = new Player();
        validNameLower = "ii";
        validNameUpper = "iiiiIiiiiIiiiiIiiiiIiiiiIiiiiI";
        validNameMid = "iiiiIiiiiIiiiiI";
        invalidNameLengthLower = "i";
        invalidNameLengthUpper = "iiiiIiiiiIiiiiIiiiiIiiiiIiiiiIi";
        invalidName1 = "dog boy";
        invalidName2 = "$$bigmoney";
        invalidName3 = "le-ah";
        startingFunding = 300;
    }

    @Test
    void nameGetterSetterValid() {
        p1.setName(validNameLower);
        assertEquals(validNameLower, p1.getName());
    }

    @Test
    void nameLengthValid() {
        p1.setName(validNameUpper);
        assertEquals(validNameUpper, p1.getName());

        p1.setName(validNameMid);
        assertEquals(validNameMid, p1.getName());

        p1.setName(validNameLower);
        assertEquals(validNameLower, p1.getName());
    }

    @Test
    void nameLengthInvalid() {
        IllegalArgumentException iAE = assertThrows(IllegalArgumentException.class, () -> p1.setName(invalidNameLengthLower));


        assertEquals("Name format error. Length must be between 2 and 30 characters.", iAE.getMessage());

        iAE = assertThrows(IllegalArgumentException.class, () -> p1.setName(invalidNameLengthUpper));
        assertEquals("Name format error. Length must be between 2 and 30 characters.", iAE.getMessage());
    }

    @Test
    void nameInvalidFormat() {

        IllegalArgumentException iAE = assertThrows(IllegalArgumentException.class, () -> p1.setName(invalidName1));
        assertEquals("Name format error. Name contains illegal characters. Alphanumeric only, no spaces.", iAE.getMessage());

        iAE = assertThrows(IllegalArgumentException.class, () -> p1.setName(invalidName2));
        assertEquals("Name format error. Name contains illegal characters. Alphanumeric only, no spaces.", iAE.getMessage());

        iAE = assertThrows(IllegalArgumentException.class, () -> p1.setName(invalidName3));
        assertEquals("Name format error. Name contains illegal characters. Alphanumeric only, no spaces.", iAE.getMessage());
    }

    @Test
    void fundingStartAmount() {
        assertEquals(startingFunding, 300);
    }
}