package corporation

class Employee {

    String firstname

    String lastname

    Department departament

    static constraints = {
        firstname()
        lastname()
        departament nullable: true
    }

    @Override
    String toString() {
        "$firstname $lastname"
    }
}
