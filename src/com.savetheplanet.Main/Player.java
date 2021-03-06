package com.savetheplanet.Main;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Jaszon & Sophie & Paul
 */
public class Player implements Serializable {

    String name;
    int funding;
    int totalValue;
    int turnsTaken;
    int location;
    List<String> titles = new ArrayList<>();
    List<FundableSquare> ownedSquares = new ArrayList<>();

    public Player(String name) {
        setName(name);
        funding = 300;
        location = 0;
        totalValue = 0;
    }

    public void calcTotalValue() {
        this.totalValue = getOwnedSquares().stream().mapToInt(FundableSquare::getValue).sum();
    }

    public int getTotalValue() {
        return totalValue;
    }

    public List<FundableSquare> getOwnedSquares() {
        return ownedSquares;
    }

    /**
     * Sophie
     *
     * Method to get a player's lowest value square for use in the 'liquidate' method.
     * Properties are returned in the following order, where applicable:
     * property from uncontrolled area, undeveloped properties from controlled areas, developed properties.
     *
     * @return FundableSquare
     */
    public FundableSquare getLowestValueSquare() {
        int conserve = 0;
        int reduce = 0;
        int reuse = 0;
        int create = 0;

        for (int i = 0; i < this.getOwnedSquares().size(); i++) {
            switch (this.getOwnedSquares().get(i).getField()) {
                case 3:
                    conserve++;
                    break;
                case 4:
                    reduce++;
                    break;
                case 5:
                    reuse++;
                    break;
                case 6:
                    create++;
                    break;
                default:
            }
        }

        //get square from uncontrolled area
        if (conserve == 1) {
            return (FundableSquare) findSquare(this.getOwnedSquares(), 3).get(0);
        } else if (reduce > 0 && reduce < 3) {
            return (FundableSquare) findSquare(this.getOwnedSquares(), 4).get(0);
        } else if (reuse > 0 && reuse < 3) {
            return (FundableSquare) findSquare(this.getOwnedSquares(), 5).get(0);
        } else if (create == 1) {
            return (FundableSquare) findSquare(this.getOwnedSquares(), 6).get(0);
        }

        //get square from undeveloped area
        if (conserve == 2) {
            ArrayList<Object> results = findSquare(this.getOwnedSquares(), 3);
            if (results.get(1).equals(false)) {
                return (FundableSquare) results.get(0);
            }
        }

        if (reduce == 3) {
            ArrayList<Object> results = findSquare(this.getOwnedSquares(), 4);
            if (results.get(1).equals(false)) {
                return (FundableSquare) results.get(0);
            }
        }

        if (reuse == 3) {
            ArrayList<Object> results = findSquare(this.getOwnedSquares(), 5);
            if (results.get(1).equals(false)) {
                return (FundableSquare) results.get(0);
            }
        }

        if (create == 2) {
            ArrayList<Object> results = findSquare(this.getOwnedSquares(), 6);
            if (results.get(1).equals(false)) {
                return (FundableSquare) results.get(0);
            }
        }

        //get developed square
        if (conserve == 2) {
            return (FundableSquare) findSquare(this.getOwnedSquares(), 3).get(0);
        } else if (reduce == 3) {
            return (FundableSquare) findSquare(this.getOwnedSquares(), 4).get(0);
        } else if (reuse == 3) {
            return (FundableSquare) findSquare(this.getOwnedSquares(), 5).get(0);
        } else if (create == 2) {
            return (FundableSquare) findSquare(this.getOwnedSquares(), 6).get(0);
        }

        return null;
    }

    /**
     * Sophie
     *
     * Method to return square for use in 'getLowestValueSquare' method.
     *
     * @param ownedSquares
     * @param field
     * @return ArrayList<Object> - returns a list of squares, with an associated boolean flag indicating if the square has been developed.
     */
    private ArrayList<Object> findSquare(List<FundableSquare> ownedSquares, int field) {
        boolean developed = false;
        ArrayList<Object> results = new ArrayList<>();
        FundableSquare undevelopedSquare = null;
        for (FundableSquare square : ownedSquares) {
            if (square.getField() == field) {
                if (square.getDevLevel() > 0) {
                    results.add(square);
                    results.add(true);
                    return results;
                } else if (square.getDevLevel() == 0) {
                    undevelopedSquare = square;
                }
            }
        }
        results.add(undevelopedSquare);
        results.add(developed);
        return results;
    }

    public void addOwnedSquare(FundableSquare square) {
        this.ownedSquares.add(square);

    }

    public int getFunding() {
        return funding;
    }

    public void setFunding(int funding) {
        this.funding = funding;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) throws IllegalArgumentException {
        name = name.trim();
        if (validateName(name)) {
            this.name = name;
        } else {
            throw new IllegalArgumentException("Name format error");
        }
    }

    /**
     * Jaszon
     *
     * @param name from Player
     * @return boolean, true if the name passes the requirements.
     * @throws IllegalArgumentException if the requirements are not met an IAE is thrown. This is handled in Players.validateName();
     */
    private boolean validateName(String name) throws IllegalArgumentException {
        if (name.matches("^.*[^a-zA-Z\\d].*$"))
            throw new IllegalArgumentException("Name format error. Name contains illegal characters. Alphanumeric only, no spaces.");
        if (name.length() < 2 || name.length() > 30)
            throw new IllegalArgumentException("Name format error. Length must be between 2 and 30 characters.");

        return true;
    }

    public int getTurnsTaken() {
        return turnsTaken;
    }

    public void setTurnsTaken(int turnsTaken) {
        this.turnsTaken = turnsTaken;
    }

    public int getLocation() {
        return location;
    }

    public void setLocation(int location) {
        this.location = location;
    }

    public List<String> getTitles() {
        return titles;
    }

    public void printTitles() {

        for (String s : titles) {
            System.out.print(s + ", ");
        }
    }

    public void addTitle(String title) {
        this.titles.add(title);
    }

    public void removeTitle(String title) {
        this.titles.remove(title);
    }
} //class
