package org.example.domain;

import java.time.LocalDateTime;
import java.util.Date;


 //Класс Person представляет человека,который хранится в коллекции.
// Реализует Comparable для сортировки по height.

public class Person implements Comparable<Person> {

    private static int nextId = 1;

    private final int id; // > 0, уникальный, генерируется автоматически
    private final String name; // не null и не пустой
    private final Coordinates coordinates; // не null
    private final Date creationDate; // генерируется автоматически
    private final float height; // > 0
    private final LocalDateTime birthday; // может быть null
    private final Color hairColor; // может быть null
    private final Country nationality; // может быть null
    private final Location location; // может быть null

    /**
     * Конструктор для создания нового объекта (id генерируется автоматически).
     */
    public Person(String name,
                  Coordinates coordinates,
                  float height,
                  LocalDateTime birthday,
                  Color hairColor,
                  Country nationality,
                  Location location) {

        validate(name, coordinates, height);

        this.id = nextId++;
        this.creationDate = new Date();

        this.name = name;
        this.coordinates = coordinates;
        this.height = height;
        this.birthday = birthday;
        this.hairColor = hairColor;
        this.nationality = nationality;
        this.location = location;
    }


     //Конструктор для загрузки из файла (id восстанавливается).

    public Person(int id,
                  String name,
                  Coordinates coordinates,
                  float height,
                  LocalDateTime birthday,
                  Color hairColor,
                  Country nationality,
                  Location location) {

        validate(name, coordinates, height);

        this.id = id;

        // синхронизация nextId
        if (id >= nextId) {
            nextId = id + 1;
        }

        this.creationDate = new Date();
        this.name = name;
        this.coordinates = coordinates;
        this.height = height;
        this.birthday = birthday;
        this.hairColor = hairColor;
        this.nationality = nationality;
        this.location = location;
    }

    private void validate(String name,
                          Coordinates coordinates,
                          float height) {

        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Имя не может быть пустым.");
        }

        if (coordinates == null) {
            throw new IllegalArgumentException("Координаты не могут быть null.");
        }

        if (height <= 0) {
            throw new IllegalArgumentException("Рост должен быть больше 0.");
        }
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Coordinates getCoordinates() {
        return coordinates;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public float getHeight() {
        return height;
    }

    public LocalDateTime getBirthday() {
        return birthday;
    }

    public Color getHairColor() {
        return hairColor;
    }

    public Country getNationality() {
        return nationality;
    }

    public Location getLocation() {
        return location;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Person)) return false;
        Person person = (Person) o;
        return id == person.id;
    }

    @Override
    public int hashCode() {
        return Integer.hashCode(id);
    }

    @Override
    public int compareTo(Person other) {
        return Float.compare(this.height, other.height);
    }

    @Override
    public String toString() {
        return "Person{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", coordinates=" + coordinates +
                ", creationDate=" + creationDate +
                ", height=" + height +
                ", birthday=" + birthday +
                ", hairColor=" + hairColor +
                ", nationality=" + nationality +
                ", location=" + location +
                '}';
    }
}