package com.savetheplanet.Main;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import com.savetheplanet.Main.ChanceCard;
import com.savetheplanet.Main.RandomSquareAssignment;

class TestChanceCard {

		// test data
		int validMovementLow, validMovementMid, validMovementHigh;
		int invalidMovementLow, invalidMovementHigh;
		int validPaymentLow, validPaymentMid, validPaymentHigh;
		int invalidPaymentLow, invalidPaymentHigh;
		ChanceCard chanceCardSingleArg;
		ChanceCard chanceCardDoubleArgWithPayment;
		ChanceCard chanceCardDoubleArgWithMovement;
	
	
	@BeforeEach
	void setUp() throws Exception {
		validMovementLow = 1; 
		validMovementMid = 3; 
		validMovementHigh = 5;
		invalidMovementLow = 0;
		invalidMovementHigh = 7;
		validPaymentLow = 100; 
		validPaymentMid = 1500; 
		validPaymentHigh = 3000;
		invalidPaymentLow = 99; 
		invalidPaymentHigh = 3001;
		chanceCardSingleArg = new ChanceCard(RandomSquareAssignment.ADMIN_CATCH_UP_DAY);
		chanceCardDoubleArgWithMovement = new ChanceCard(RandomSquareAssignment.FORWARD, validMovementHigh);
		chanceCardDoubleArgWithPayment = new ChanceCard(RandomSquareAssignment.PAY, validPaymentLow);
		//if default constructor included in class, could also declare empty object for convenience in testing 
		//(and to ensure values are set to something different than they started at while testing)
	}

	@Test
	void testChanceCardValid() {
		//Happy Path test that values set by constructor as expected
		ChanceCard testConstOneArg = new ChanceCard(RandomSquareAssignment.ADMIN_CATCH_UP_DAY);
		assertNotNull(testConstOneArg);
		ChanceCard testConstTwoArgsWithMovement = new ChanceCard(RandomSquareAssignment.FORWARD, validMovementHigh);
		assertNotNull(testConstTwoArgsWithMovement);
		ChanceCard testConstTwoArgsWithPayment = new ChanceCard(RandomSquareAssignment.RECEIVE, validPaymentLow);
		assertNotNull(testConstTwoArgsWithPayment);
		assertEquals(RandomSquareAssignment.ADMIN_CATCH_UP_DAY, chanceCardSingleArg.getAssigned());
		assertEquals(RandomSquareAssignment.FORWARD, chanceCardDoubleArgWithMovement.getAssigned());
		assertEquals(validMovementHigh, chanceCardDoubleArgWithMovement.getAction());
		assertEquals(validPaymentLow, chanceCardDoubleArgWithPayment.getAction());
	}
	
	@Test
	void testChanceCardInvalid() {
		//Unhappy Path test each arg of constructor in turn to ensure exception thrown as expected
		//verifies that constructor actually linked to setters, not just setting directly

		assertThrows(IllegalArgumentException.class, () -> {
			new ChanceCard(null);
		});
		assertThrows(IllegalArgumentException.class, () -> {
			new ChanceCard(RandomSquareAssignment.FORWARD, invalidMovementHigh);
		});
		assertThrows(IllegalArgumentException.class, () -> {
			new ChanceCard(RandomSquareAssignment.RECEIVE, invalidPaymentHigh);
		});
	
	}
	
	@Test
	void testGetSetRandomSquare() {
		//happy path
		chanceCardSingleArg.setAssigned(RandomSquareAssignment.ADMIN_CATCH_UP_DAY);
		assertEquals(RandomSquareAssignment.ADMIN_CATCH_UP_DAY, chanceCardSingleArg.getAssigned());
		
		//unhappy path
		Exception exp = assertThrows(IllegalArgumentException.class, () -> {
					chanceCardSingleArg.setAssigned(null);
		});
		assertEquals("Must be assigned a Random Square", exp.getMessage());
	}
	@Test
	void testGetSetDataPaymentLow() {
		chanceCardDoubleArgWithPayment.setData(validPaymentLow);
		assertEquals(validPaymentLow, chanceCardDoubleArgWithPayment.getAction());		
	}
	@Test
	void testGetSetDataPaymentMid() {
		chanceCardDoubleArgWithPayment.setData(validPaymentMid);
		assertEquals(validPaymentMid, chanceCardDoubleArgWithPayment.getAction());		
	}
	@Test
	void testGetSetDataPaymentHigh() {
		chanceCardDoubleArgWithPayment.setData(validPaymentHigh);
		assertEquals(validPaymentHigh, chanceCardDoubleArgWithPayment.getAction());		
	}
	
	@Test
	void testGetSetDataPaymentInvalid() {
		Exception exp = assertThrows(IllegalArgumentException.class, () -> {
			chanceCardDoubleArgWithPayment.setData(invalidPaymentLow); 
		});
		assertEquals("Value out of range", exp.getMessage());
		
		exp = assertThrows(IllegalArgumentException.class, () -> {
			chanceCardDoubleArgWithPayment.setData(invalidPaymentHigh);
		});
		assertEquals("Value out of range", exp.getMessage());
		
	}
	
	@Test
	void testGetSetDataMovementLow() {
		chanceCardDoubleArgWithMovement.setData(validMovementLow);
		assertEquals(validMovementLow, chanceCardDoubleArgWithMovement.getAction());		
	}
	@Test
	void testGetSetDataMovementMid() {
		chanceCardDoubleArgWithMovement.setData(validMovementMid);
		assertEquals(validMovementMid, chanceCardDoubleArgWithMovement.getAction());		
	}
	@Test
	void testGetSetDataMovementHigh() {
		chanceCardDoubleArgWithMovement.setData(validMovementHigh);
		assertEquals(validMovementHigh, chanceCardDoubleArgWithMovement.getAction());		
	}
	
	@Test
	void testGetSetDataMovementInvalid() {
		Exception exp = assertThrows(IllegalArgumentException.class, () -> {
			chanceCardDoubleArgWithMovement.setData(invalidMovementLow); 
		});
		assertEquals("Value out of range", exp.getMessage());
		
		exp = assertThrows(IllegalArgumentException.class, () -> {
			chanceCardDoubleArgWithMovement.setData(invalidMovementHigh);
		});
		assertEquals("Value out of range", exp.getMessage());
		
	}
	
	@Test
	void testSwitch() {
		chanceCardSingleArg.getAssigned();
			String expected = "Have a breather; catch up on some paperwork. Move to Admin Catch Up Day";
			assertEquals(expected, "Have a breather; catch up on some paperwork. Move to Admin Catch Up Day");
		}

}

