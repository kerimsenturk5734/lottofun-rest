[plantuml, uml-example, png]
----
@startuml
class User {
- id: int
- name: String
- email: String
+ register()
+ login()
  }

class Ticket {
- id: int
- number: String
- date: Date
+ validate()
  }

User "1" -- "*" Ticket : owns >
@enduml
----