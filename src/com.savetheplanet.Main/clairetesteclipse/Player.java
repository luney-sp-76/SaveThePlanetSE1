package clairetesteclipse;

public class Player {

    private String name;
    private int points;

    //constructor
    public Player(String name, int points){
        setName(name);
        setPoints(points);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getPoints() {
        return points;
    }

    public void setPoints(int points) {
        this.points = points;
    }

    @Override
    public String toString(){
        return String.format("%s: %s%n%s: %d","Name ",this.name = getName(),"Points", this.points = getPoints());
    }


}

