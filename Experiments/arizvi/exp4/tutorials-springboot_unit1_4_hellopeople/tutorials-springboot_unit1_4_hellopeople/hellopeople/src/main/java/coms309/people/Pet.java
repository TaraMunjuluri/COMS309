package coms309.people;

public class Pet {
    private String firstName;

    private String color;

    private String type;

    public Pet(){

    }

    public Pet(String firstName, String color, String type){
        this.firstName = firstName;
        this.color = color;
        this.type = type;
    }

    public String getFirstName() {
        return this.firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }



    public  String getColor() {
        return this.color;
    }

    public void setColor(String color) {
        this.color = color;
    }



    public  String getType() {
        return this.type;
    }

    public void setType(String type) {
        this.type = type;
    }
    @Override
    public String toString() {
        return firstName + " "
                + color + " ";
    }
}
