package com.atf.demo;

import jakarta.ejb.Remote;

@Remote
public interface PersonServiceRemote {
    void addPerson(String name);
}
