package com.savetheplanet.Main;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Player implements Serializable {

    String name;
    int funding;
    int totalValue;
    int turnsTaken;

    int location;
    List<FundableSquare> ownedSquares = new ArrayList<>();

    public Player(String name) {
        setName(name);
        funding = 500;
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

    public FundableSquare getLowestValueSquare() {

        int conserve = 0;
        int reduce = 0;
        int reuse = 0;
        int create = 0;

        for(int i=0; i<this.getOwnedSquares().size(); i++){
            switch(this.getOwnedSquares().get(i).getField()){
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
        if(conserve==1){
            return (FundableSquare) findSquare(this.getOwnedSquares(), 3).get(0);
        } else if(reduce>0 && reduce<3){
            return (FundableSquare) findSquare(this.getOwnedSquares(), 4).get(0);
        } else if(reuse>0 && reuse<3){
            return (FundableSquare) findSquare(this.getOwnedSquares(), 5).get(0);
        } else if(create==1){
            return (FundableSquare) findSquare(this.getOwnedSquares(), 6).get(0);
        }

        //get square from undeveloped area
        if(conserve==2){
            ArrayList<Object> results = findSquare(this.getOwnedSquares(), 3);
            if(results.get(1).equals(false)){
                return (FundableSquare) results.get(0);
            }
        }

        if(reduce==3){
            ArrayList<Object> results = findSquare(this.getOwnedSquares(), 4);
            if(results.get(1).equals(false)){
                return (FundableSquare) results.get(0);
            }
        }

        if(reuse==3){
            ArrayList<Object> results = findSquare(this.getOwnedSquares(), 5);
            if(results.get(1).equals(false)){
                return (FundableSquare) results.get(0);
            }
        }

        if(create==2){
            ArrayList<Object> results = findSquare(this.getOwnedSquares(), 6);
            if(results.get(1).equals(false)){
                return (FundableSquare) results.get(0);
            }
        }

        //get developed square
        if(conserve==2){
            return (FundableSquare) findSquare(this.getOwnedSquares(), 3).get(0);
        } else if(reduce==3){
            return (FundableSquare) findSquare(this.getOwnedSquares(), 4).get(0);
        } else if(reuse==3){
            return (FundableSquare) findSquare(this.getOwnedSquares(), 5).get(0);
        } else if(create==2){
            return (FundableSquare) findSquare(this.getOwnedSquares(), 6).get(0);
        }

        return null;

    }

    private ArrayList<Object> findSquare(List<FundableSquare> ownedSquares, int field) {
        boolean developed = false;
        ArrayList<Object> results = new ArrayList<Object>();
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

    /**
     * @return the location
     */

    public int getLocation() {
        return location;
    }

    /**
     * @param location the location to set
     */

    public void setLocation(int location) {
        this.location = location;
    }

    void setLocation() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }


    @Override
    public String toString() {
        return "Player{" +
                "name='" + name + '\'' +
                ", funding=" + funding +
                ", totalValue=" + totalValue +
                '}';
    }
}
