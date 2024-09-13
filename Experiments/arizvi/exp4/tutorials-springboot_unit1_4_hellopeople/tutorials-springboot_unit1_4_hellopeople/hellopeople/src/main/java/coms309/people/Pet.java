package coms309.people;

public class Pet {
    private String firstName;

    private String address;

    private int age;

    public Pet(){

    }

    public Pet(String firstName, String address, String telephone, int age){
        this.firstName = firstName;
        this.address = address;
        this.age = age;
    }

    public String getFirstName() {
        return this.firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }



    public String getAddress() {
        return this.address;
    }

    public void setAddress(String address) {
        this.address = address;
    }



    public int getAge() {
        return this.age;
    }

    public void setAge(int age) {
        this.age = age;
    }
    @Override
    public String toString() {
        return firstName + " "
                + address + " ";
    }
}
