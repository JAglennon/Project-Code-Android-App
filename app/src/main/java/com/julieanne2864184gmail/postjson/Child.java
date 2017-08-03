package com.julieanne2864184gmail.postjson;

import android.text.Editable;

import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;

import java.util.HashMap;
import java.util.Map;


/**
 * Created by julieanneglennon on 13/01/2017.
 */

//Properties that don't map to class fields are ignored when serializing to a class in Firebase.
@IgnoreExtraProperties
public class Child {
    public String ID;
    public String firstname;
    public String lastname;
    public String email;
    public Integer age;

    public Child() {
        // Default constructor required for calls to DataSnapshot.getValue(Child.class)
    }

    public Child(String id, String fname, String lname, String email, Integer Age) {
        this.ID = id;
        this.firstname = fname;
        this.lastname = lname;
        this.email = email;
        age = Age;
    }
    public String getName() {

        return firstname;
    }
    public void setID(String id){
        this.ID = id;
    }

    public String getID(){
        return ID;
    }
    public void setFirstname(String name) {
        this.firstname = name;
    }

    public void setLastname(String name) {
        this.lastname = name;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getEmail() {
        return email;
    }

    public String toString() {
        return "Child [name=" + firstname + " lastname=" + lastname +", age=" + age +", email=" + email + "]";
    }


}
