package com.atf.demo;

import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

@Stateless
public class PersonService implements PersonServiceRemote {

    @PersistenceContext(unitName = "PersonPU")
    private EntityManager em;
    public void addPerson(String name) {
        Person person = new Person(name);
        System.out.println("Adding person: " + person);
        try {
            em.persist(person);
        }catch (Exception e) {
            System.out.println("Found error:"+e.getMessage());
        }
        em.flush();
    }
}
