package clairetesteclipse;

public class Player {

    private String name;
    private int pounds;

    //constructor
    public Player(String name, int pounds){
        setName(name);
        setPounds(pounds);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getPounds() {
        return pounds;
    }

    public void setPounds(int pounds) {
        this.pounds = pounds;
    }

    @Override
    public String toString(){
        return String.format("%s: %s%n%s: %d","Name ",this.name = getName(),"Pounds", this.pounds = getPounds());
    }


}

