package com.savetheplanet.Main;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

class PlayerTest {

    Player p1;
    FundableSquare s1, s2, s3, s4, s5;
    List<FundableSquare> ownedSquares;
    String validNameLower, validNameUpper, validNameMid;
    String invalidNameLengthLower, invalidNameLengthUpper;
    String invalidName1, invalidName2, invalidName3;
    int startingFunding;

    @BeforeEach
    void setUp() {
        validNameLower = "ii";
        validNameUpper = "iiiiIiiiiIiiiiIiiiiIiiiiIiiiiI";
        validNameMid = "iiiiIiiiiIiiiiI";
        invalidNameLengthLower = "i";
        invalidNameLengthUpper = "iiiiIiiiiIiiiiIiiiiIiiiiIiiiiIi";
        invalidName1 = "dog boy";
        invalidName2 = "$$bigmoney";
        invalidName3 = "le-ah";
        startingFunding = 300;

        p1 = new Player(validNameMid);
        s1 = new FundableSquare("Led Light bulbs", 3, new String[]{"Conserve", "3", "2", "200", "250", "1", "30|50|100|200|350"});
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

    @Test
    void fundanbleSquareOwnership() {
        p1.addOwnedSquare(s1);
        assertEquals(p1.getOwnedSquares().get(0), s1);
    }

    @Test
    void getLowestValueSquare_uncontrolledArea() {

        s1 = new FundableSquare("Water Tap Timers", 3, new String[]{"Conserve", "3", "2", "200", "250", "1", "30|50|100|200|350"});
        s2 = new FundableSquare("Led Light bulbs", 3, new String[]{"Conserve", "3", "2", "200", "250", "1", "30|50|100|200|350"});
        s2.setDevLevel(2);
        s3 = new FundableSquare("Wind Turbines", 6, new String[]{"Create", "6", "2", "400", "500", "1", "30|50|100|200|350"});
        s4 = new FundableSquare("Biogas", 6, new String[]{"Create", "6", "2", "400", "500", "1", "30|50|100|200|350"});
        s5 = new FundableSquare("Public Transport", 4, new String[]{"Reduce", "4", "3", "300", "400", "1", "30|50|100|200|350"});

        p1.addOwnedSquare(s1);
        p1.addOwnedSquare(s2);
        p1.addOwnedSquare(s3);
        p1.addOwnedSquare(s4);
        p1.addOwnedSquare(s5);

        assertEquals(p1.getLowestValueSquare(), s5);
    }

    @Test
    void getLowestValueSquare_controlledUndevelopedArea() {

        s1 = new FundableSquare("Water Tap Timers", 3, new String[]{"Conserve", "3", "2", "200", "250", "1", "30|50|100|200|350"});
        s2 = new FundableSquare("Led Light bulbs", 3, new String[]{"Conserve", "3", "2", "200", "250", "1", "30|50|100|200|350"});
        s2.setDevLevel(2);
        s3 = new FundableSquare("Wind Turbines", 6, new String[]{"Create", "6", "2", "400", "500", "1", "30|50|100|200|350"});
        s4 = new FundableSquare("Biogas", 6, new String[]{"Create", "6", "2", "400", "500", "1", "30|50|100|200|350"});

        p1.addOwnedSquare(s1);
        p1.addOwnedSquare(s2);
        p1.addOwnedSquare(s3);
        p1.addOwnedSquare(s4);

        assertEquals(p1.getLowestValueSquare(), s4);
    }

    @Test
    void getLowestValueSquare_controlledDevelopedArea() {

        s1 = new FundableSquare("Water Tap Timers", 3, new String[]{"Conserve", "3", "2", "200", "250", "1", "30|50|100|200|350"});
        s2 = new FundableSquare("Led Light bulbs", 3, new String[]{"Conserve", "3", "2", "200", "250", "1", "30|50|100|200|350"});
        s2.setDevLevel(2);

        p1.addOwnedSquare(s1);
        p1.addOwnedSquare(s2);

        assertEquals(p1.getLowestValueSquare(), s2);
    }
}
