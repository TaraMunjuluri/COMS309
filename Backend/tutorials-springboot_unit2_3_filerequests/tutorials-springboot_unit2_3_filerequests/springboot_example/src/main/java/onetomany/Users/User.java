////package onetomany.Users;
////
////import java.sql.Blob;
////import java.util.ArrayList;
////import java.util.Date;
////import java.util.List;
////import java.util.Optional;
////
////import javax.persistence.CascadeType;
////import javax.persistence.Entity;
////import javax.persistence.GeneratedValue;
////import javax.persistence.GenerationType;
////import javax.persistence.Id;
////import javax.persistence.JoinColumn;
////import javax.persistence.Lob;
////import javax.persistence.OneToMany;
////import javax.persistence.OneToOne;
////
////import com.fasterxml.jackson.annotation.JsonIgnore;
////
////import onetomany.Laptops.Laptop;
////import onetomany.Phones.Phone;
////
////@Entity
////public class User {
////
////    /*
////     * The annotation @ID marks the field below as the primary key for the table created by springboot
////     * The @GeneratedValue generates a value if not already present, The strategy in this case is to start from 1 and increment for each table
////     */
////    @Id
////    @GeneratedValue(strategy = GenerationType.IDENTITY)
////    private int id;
////
//////    new methods for demo 2
////    private String username;
////    private String password;
////
////    // new methods for demo 2
////
////    private String name;
////    private String emailId;
////    private Date joiningDate;
////    private boolean ifActive;
////    private String extenstion;
////
////    @JsonIgnore
////    @Lob
////    private Blob avtar;
////
////    /*
////     * @OneToOne creates a relation between the current entity/table(Laptop) with the entity/table defined below it(User), the cascade option tells springboot
////     * to create the child entity if not present already (in this case it is laptop)
////     * @JoinColumn specifies the ownership of the key i.e. The User table will contain a foreign key from the laptop table and the column name will be laptop_id
////     */
////    @OneToOne(cascade = CascadeType.ALL)
////    @JoinColumn(name = "laptop_id")
////    private Laptop laptop;
////
////     /*
////     * @OneToMany tells springboot that one instance of User can map to multiple instances of Phone OR one user row can map to multiple rows of the phone table
////     */
////    @OneToMany
////    private List<Phone> phones;
////
////     // =============================== Constructors ================================== //
////
////
////    public User(String name, String emailId, Date joiningDate) {
////        this.name = name;
////        this.emailId = emailId;
////        this.joiningDate = joiningDate;
////        this.ifActive = true;
////        phones = new ArrayList<>();
////    }
////
////    public User() {
////        phones = new ArrayList<>();
////    }
////
////
////    // =============================== Getters and Setters for each field ================================== //
////
////    // Getters and Setters for username and password (demo 2)
////    public String getUsername() {
////        return username;
////    }
////
////    public void setUsername(String username) {
////        this.username = username;
////    }
////
////    public String getPassword() {
////        return password;
////    }
////
////    public void setPassword(String password) {
////        this.password = password;
////    }
////    // Getters and Setters for username and password (demo 2)
////
////    public int getId(){
////        return id;
////    }
////
////    public void setId(int id){
////        this.id = id;
////    }
////
////    public String getName(){
////        return name;
////    }
////
////    public void setName(String name){
////        this.name = name;
////    }
////
////    public String getEmailId(){
////        return emailId;
////    }
////
////    public void setEmailId(String emailId){
////        this.emailId = emailId;
////    }
////
////    public Date getJoiningDate(){
////        return joiningDate;
////    }
////
////    public void setJoiningDate(Date joiningDate){
////        this.joiningDate = joiningDate;
////    }
////
////    public boolean getIsActive(){
////        return ifActive;
////    }
////
////    public void setIfActive(boolean ifActive){
////        this.ifActive = ifActive;
////    }
////
////    public Laptop getLaptop(){
////        return laptop;
////    }
////
////    public void setLaptop(Laptop laptop){
////        this.laptop = laptop;
////    }
////
////    public boolean isIfActive() {
////        return ifActive;
////    }
////
////    public List<Phone> getPhones() {
////        return phones;
////    }
////
////    public void setPhones(List<Phone> phones) {
////        this.phones = phones;
////    }
////
////    public void addPhones(Phone phone){
////        this.phones.add(phone);
////    }
////
////    public Blob getAvtar() {
////        return avtar;
////    }
////
////    public void setAvtar(Blob avtar) {
////        this.avtar = avtar;
////    }
////
////    public String getExtenstion() {
////        return extenstion;
////    }
////
////    public void setExtenstion(String extenstion) {
////        this.extenstion = extenstion;
////    }
////
////}
////
////
//package onetomany.Users;
//
//import onetomany.Laptops.Laptop;
//import onetomany.Phones.Phone;
//
//import javax.persistence.*;
//import java.sql.Blob;
//import java.util.ArrayList;
//import java.util.Date;
//import java.util.List;
//
//@Entity
//public class User {
//
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private int id;
//
//    private String username;
//    private String password;
//    private String name;
//    private String emailId;
//    private Date joiningDate;
//    private boolean ifActive;
//    private String extension;
//
//    @Lob
//    private Blob avatar;
//
//    @OneToOne(cascade = CascadeType.ALL)
//    @JoinColumn(name = "laptop_id")
//    private Laptop laptop;
//
//    @OneToMany
//    private List<Phone> phones = new ArrayList<>();
//
//    // Constructors
//    public User(String name, String emailId, Date joiningDate) {
//        this.name = name;
//        this.emailId = emailId;
//        this.joiningDate = joiningDate;
//        this.ifActive = true;
//    }
//
//    public User() {
//    }
//
//    // Getters and Setters
//    public int getId() {
//        return id;
//    }
//
//    public void setId(int id) {
//        this.id = id;
//    }
//
//    public String getUsername() {
//        return username;
//    }
//
//    public void setUsername(String username) {
//        this.username = username;
//    }
//
//    public String getPassword() {
//        return password;
//    }
//
//    public void setPassword(String password) {
//        this.password = password;
//    }
//
//    public String getName() {
//        return name;
//    }
//
//    public void setName(String name) {
//        this.name = name;
//    }
//
//    public String getEmailId() {
//        return emailId;
//    }
//
//    public void setEmailId(String emailId) {
//        this.emailId = emailId;
//    }
//
//    public Date getJoiningDate() {
//        return joiningDate;
//    }
//
//    public void setJoiningDate(Date joiningDate) {
//        this.joiningDate = joiningDate;
//    }
//
//    public boolean getIsActive() {
//        return ifActive;
//    }
//
//    public void setIfActive(boolean ifActive) {
//        this.ifActive = ifActive;
//    }
//
//    public Laptop getLaptop() {
//        return laptop;
//    }
//
//    public void setLaptop(Laptop laptop) {
//        this.laptop = laptop;
//    }
//
//    public List<Phone> getPhones() {
//        return phones;
//    }
//
//    public void setPhones(List<Phone> phones) {
//        this.phones = phones;
//    }
//
//    public Blob getAvatar() {
//        return avatar;
//    }
//
//    public void setAvatar(Blob avatar) {
//        this.avatar = avatar;
//    }
//
//    public String getExtension() {
//        return extension;
//    }
//
//    public void setExtension(String extension) {
//        this.extension = extension;
//    }
//
//    // Removed Project-related code
//
//    // public List<Project> getProjects() {
//    //     return projects;
//    // }
//
//    // public void setProjects(List<Project> projects) {
//    //     this.projects = projects;
//    // }
//
//    // public void addProject(Project project) {
//    //     this.projects.add(project);
//    // }
//}

package onetomany.Users;

import onetomany.Laptops.Laptop;
import onetomany.Phones.Phone;

import javax.persistence.*;
import java.sql.Blob;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String username;
    private String password;
    private String name;
    private String emailId;
    private Date joiningDate;
    private boolean ifActive;
    private String extension;

    @JsonIgnore
    @Lob
    private Blob avatar;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "laptop_id")
    private Laptop laptop;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinTable(name = "user_phones",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "phone_id"))
    private List<Phone> phones = new ArrayList<>();

    // Constructors
    public User(String name, String emailId, Date joiningDate) {
        this.name = name;
        this.emailId = emailId;
        this.joiningDate = joiningDate;
        this.ifActive = true;
    }

    public User() {
    }

    // Getters and Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmailId() {
        return emailId;
    }

    public void setEmailId(String emailId) {
        this.emailId = emailId;
    }

    public Date getJoiningDate() {
        return joiningDate;
    }

    public void setJoiningDate(Date joiningDate) {
        this.joiningDate = joiningDate;
    }

    public boolean getIsActive() {
        return ifActive;
    }

    public void setIfActive(boolean ifActive) {
        this.ifActive = ifActive;
    }

    public Laptop getLaptop() {
        return laptop;
    }

    public void setLaptop(Laptop laptop) {
        this.laptop = laptop;
    }

    public List<Phone> getPhones() {
        return phones;
    }

    public void setPhones(List<Phone> phones) {
        this.phones = phones;
    }

    public Blob getAvatar() {
        return avatar;
    }

    public void setAvatar(Blob avatar) {
        this.avatar = avatar;
    }

    public String getExtension() {
        return extension;
    }

    public void setExtension(String extension) {
        this.extension = extension;
    }

    public void addPhone(Phone phone) {
        this.phones.add(phone);
    }
}