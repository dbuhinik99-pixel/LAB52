package org.example.service;

import org.example.domain.Person;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

public class PersonService {
//содержит логику работы с коллекцией  _ это структура данных,
// которая хранит уникальные элементы и использует хэш-таблицу для их хранения
    private final Set<Person> collection = new HashSet<>();
    private final LocalDateTime initializationDate = LocalDateTime.now();

    public String info() {
        return "Тип коллекции: " + collection.getClass().getName() +
                "\nДата инициализации: " + initializationDate +
                "\nКоличество элементов: " + collection.size();
    }

    public Set<Person> show() {
        return collection;
    }

    public void add(Person person) {
        collection.add(person);
    }

    public boolean update(int id, Person newPerson) {

        Optional<Person> existing = collection.stream()
                .filter(p -> p.getId() == id)
                .findFirst();

        if (existing.isPresent()) {

            Person oldPerson = existing.get();
            collection.remove(oldPerson);

            // создаём нового человека с тем же id
            Person updated = new Person(
                    id,
                    newPerson.getName(),
                    newPerson.getCoordinates(),
                    newPerson.getHeight(),
                    newPerson.getBirthday(),
                    newPerson.getHairColor(),
                    newPerson.getNationality(),
                    newPerson.getLocation()
            );

            collection.add(updated);
            return true;
        }

        return false;
    }

    public boolean removeById(int id) {
        return collection.removeIf(p -> p.getId() == id);
    }

    public void clear() {
        collection.clear();
    }

    public Person getMin() {
        return collection.stream()
                .min(Person::compareTo)
                .orElse(null);
    }

    public boolean addIfMin(Person person) {
        Person min = getMin();
        if (min == null || person.compareTo(min) < 0) {
            collection.add(person);
            return true;
        }
        return false;
    }

    public int removeGreater(Person person) {
        int before = collection.size();
        collection.removeIf(p -> p.compareTo(person) > 0);
        return before - collection.size();
    }

    public int removeLower(Person person) {
        int before = collection.size();
        collection.removeIf(p -> p.compareTo(person) < 0);
        return before - collection.size();
    }

    public List<Person> filterStartsWithName(String prefix) {
        return collection.stream()
                .filter(p -> p.getName().startsWith(prefix))
                .collect(Collectors.toList());
    }

    public List<Person> printAscending() {
        return collection.stream()
                .sorted()
                .collect(Collectors.toList());
    }

    public List<Person> printDescending() {
        return collection.stream()
                .sorted(Comparator.reverseOrder())
                .collect(Collectors.toList());
    }

    public Set<Person> getCollection() {
        return collection;
    }
}