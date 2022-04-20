/**
 * 
 */
package com.savetheplanet.Main;

/**
 *
 */
public class ChanceCard {
	
	private static final int MINMOVEMENT = 1; 
	private static final int MAXMOVEMENT = 5;
	private static final int MINPAYMENT = 100;
	private static final int MAXPAYMENT= 3000;
	
	private RandomSquareAssignment assigned; 
	private int action;
	
	/**
	 * constructor for RandomSquareAssignment
	 * @param assigned
	 */
	public ChanceCard(RandomSquareAssignment assigned) {
		setAssigned(assigned);
	}
	
	public ChanceCard(RandomSquareAssignment assigned, int action) {
		setAssigned(assigned);
		setData(action);
	}
		
	/**
	 * @return the assigned
	 */
	public RandomSquareAssignment getAssigned() {
		return assigned;
	}

	/**
	 * Set with enum value (no null values accepted)
	 * 
	 * @param assigned
	 * @throws IllegalArgumentException
	 */
	public void setAssigned(RandomSquareAssignment assigned) throws IllegalArgumentException {
		if (assigned != null) {
			this.assigned = assigned;
		} else {
			throw new IllegalArgumentException("Must be assigned a Random Square");
		}
	
	}

	public int getAction() {
		return action;
	}

	/**
	 * set move
	 * 
	 * @param move
	 * @throws IllegalArgumentException
	 */
	public void setData(int action) throws IllegalArgumentException {
//		if (validateStat(move)) {
			if (validate(action)) {
			this.action = action;
		} else {
			throw new IllegalArgumentException("Value out of range");
		}
	}
	
	/**
	 * verify movement stat is within range
	 * 
	 * @param stat
	 * @return
	 */
	private boolean validate(int data) {

		if ((assigned.name().equalsIgnoreCase("pay") || assigned.name().equalsIgnoreCase("receive")) && (data >= MINPAYMENT && data <= MAXPAYMENT)) {
			return true;
		} else if ((assigned.name().equalsIgnoreCase("forward") || assigned.name().equalsIgnoreCase("back")) && (data >= MINMOVEMENT && data <= MAXMOVEMENT)) {
			return true;
		} else {
			return false;
		}
	
	}
	
	/**
	 * 
	 * @param card passed
	 * @throws IllegalArgumentException if invalid arg passed
	 */
	public void fullDetails(ChanceCard card) throws IllegalArgumentException {		
		switch(card.getAssigned()) {
			case BACK:
				System.out.println("OH NO! Move back "+action+" squares");
				break;
			case FORWARD:
				System.out.println("YES! Move forward "+action+" squares");
				break;			
			case PAY: 
				System.out.println("OH NO! You must pay £"+action);
				break;		
			case RECEIVE:
				System.out.println("YES! You receive £"+action);
				break;
			case ADMIN_CATCH_UP_DAY:
				System.out.println("Have a breather; catch up on some paperwork. Move to Admin Catch Up Day");
				break;
			case COLLECT_FUNDING:
				System.out.println("Top up the Bank! Move straight to Collect Funding and bank £500");
				break;	
			case EARTH_DAY:
				System.out.println("Everyone needs a break. Down tools. Relax. Move to Earth Day");
				break;
			case WORKER_HOLIDAY:
				System.out.println("Relax. Move to Worker Holiday");
				break;
			default:
				throw new IllegalArgumentException("What madness did you pass in? Validate Chance Card");
		}
	}
}

