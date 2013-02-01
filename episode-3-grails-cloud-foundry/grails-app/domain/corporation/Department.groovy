package corporation

class Department {

    String name

    static hasMany = [
            employees : Employee
    ]

    static constraints = {
    }

    @Override
    String toString() {
        name
    }

    }
