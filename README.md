# OOP_Final_Project

## Checkpoint 1.5
Implemented the three base classes: Patient.java, Provider.java, and 
Appointment.java as well as the AppointmentStatus Enum class. We also
implemented our AppointmentManager class and wrote all the functions which we
defined in our UML diagram from checkpoint 1.0. Finally, we created a Test.java
class which we can use to test our appointment scheduling. So far all of our 
business rules are enforced but just throw java exceptions and end the entire
program. We will have to replace these in the future for more graceful errors.
Our reschedule method was implemented poorly and needs to be done better, we
also need to implement a cleaner user interface for entering the dates for this
program. i.e. some drop down selection of dates that will then convert it into
a unix timestamp on its own. A similar approach can be done for the date of
birth as well, which is currently a string. Our get patients by parameter
methods currently only print the matching appointments and nothing more, I'm
sure there will actual returns and implementation of these functions as the
project progresses. Lastly, we have yet to implement any deleting methods for
our program: i.e. deleting patients, providers, or appointments. Same goes for
a cancel appointment method. The structure currently has the manager class
effectively being a static class that stores an array list of appointments
patients and providers. This could be changed to be a non static class
but each manager only manages one patient, or provider, or appointment etc.
You can use Test.java to test the current implementation, input.txt is an
example input I fed into the program, but for the life of me I can't figure
out how to pipe that input into the program without using scanner on the 
text file itself, so you will probably have to manually compile Test.java
and manually input the contents of input.txt
- Rheggeth
