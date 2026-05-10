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

## Project Description
This project is a demonstration of a simple Appointment scheduling program.
Allowing users to create patients, providers, and then schedule appointments
for them. Appointments must have one patient and provider to be created, and
its start time can not occur during or after the end time. Patients are allowed
to have overlapping appointments, but provider cannot which is enforced in this
model. This program also allows the user to update the appointments by changing
its status, or by rescheduling the start and end time. The data is stored in an
SQLite databse client.db. The program allows for listing patients provider and
appointments, as well as deleting them. Our current implementation doesn't 
allow the deletion of patients and providers if any of their appointments have
not been deleted first. 

## System architecture overview
The lib folder here stores the jdbc .jar files to run this program on a linux
machine. There is currently not support for windows or mac. However all one
would need to do it find the respective jar files for these operating systems
and replace them with the linux jar file in lib/. After running the prgoram 
with the simple bash scrip run.sh an out/ directory will appear. This is the
javac output folder where all the .class files will be stored. There is no need
to create this folder beforehand as the bash script will handle that for you.
Then within our src/oop file structure there are 4 different packages. The dao/
directory contains all the SQLite logic as well as the generated database file
client.db (you also dont need to create this file beforehand). inside the main/
directory is the single program Main.java which is what is executed and ran for
this program. model/ has the base classes that describe the Patient, Provider,
Appointment, and an enum class AppointmentStatus. Lastly the service/ directory
is the brains of this program which houses the AppointmentManager.java file.
The methods in this file are called by Main.java and handle all the logic for
this program.

## Database setup instructions
The setup for the database is entirely hands off. As aforementioned, if you are
using something other than Linux then you will want to visit the sqlite-jdbc
github: https://github.com/xerial/sqlite-jdbc. From there you can just navigate
your way to the release pages and download the latest jdbc.jar file for your
operating system. Then go to the lib/ directory of this project and replace the
natives-linux jar file with yours. This program has been entirely ran off a
linux terminal. And I am not an expert in maven or gradle setups, hence the
bash script to run this. Once the program runs, the .db file will be created
automatically from the DAO class files when the Main class initializes the
database.

## How to run the program
Perhaps the easiest part, navigate to the top level of this project directory
on your terminal, then simply execute the bash script using ./run.sh (if this
file does not have execute permissions you will need to use chmod to give it
them). Since the interface of this program is on the command line you will then
start to see the prompts for input appear on your screen afterwards.

## Example usage
Here is the terminal output for a sample run where we create a patient and
appointment, there is also some patients and providers from previous runs:

rheggeth@Violet:~/School/CompSci/OOP/OOP_Final_Project$ ./run.sh 
Database finished loading

Which action would you like to perform?
Create, Read, Update, or Delete [C, R, U, D, or Q to Quit]
> c

What would you like to create:
1. Patient
2. Provider
3. Appointment
4. Exit create menu
> 1
Enter the patients name: Harry
Enter the patients DOB: 03/26/2001
Enter any contact information: harry@gmail.com
Created: Patient [ID: 900000005, Name: Harry, DOB: 03/26/2001, Contact: harry@gmail.com]

What would you like to create:
1. Patient
2. Provider
3. Appointment
4. Exit create menu
> 3
Enter the patients ID number: 900000005
Enter the providers ID number: 800000002
Please enter the start time: 2
Please enter the end time: 4
Please enter a reason: Harry needs his meds.
Created Appointment [ID: 700000003, Patient: Harry, Provider: Pharma, Start: 2, End: 4, Status: SCHEDULED, Reason: Harry needs his meds.]

What would you like to create:
1. Patient
2. Provider
3. Appointment
4. Exit create menu
> 4
Leaving creation menu...

Which action would you like to perform?
Create, Read, Update, or Delete [C, R, U, D, or Q to Quit]
> r

What would you like to sort by:
1. List all Patients
2. List all Providers
3. List all Appointments
4. List Appointments by Patient
5. List Appointments by Provider
6. List Appointment by date range
7. List Appointment by status
8. Exit read menu
> 1
Patient [ID: 900000003, Name: Jim, DOB: 06/06/2006, Contact: jim@gmail.com]
Patient [ID: 900000004, Name: Marly, DOB: 11/30/2010, Contact: marly@gmail.com]
Patient [ID: 900000005, Name: Harry, DOB: 03/26/2001, Contact: harry@gmail.com]

What would you like to sort by:
1. List all Patients
2. List all Providers
3. List all Appointments
4. List Appointments by Patient
5. List Appointments by Provider
6. List Appointment by date range
7. List Appointment by status
8. Exit read menu
> 2
Provider [ID: 800000002, Name: Pharma, Specialty: Perscriptions, Location: Pharma Ave]
Provider [ID: 800000003, Name: Eye Doctor, Specialty: Eye care, Location: Eye Ave]

What would you like to sort by:
1. List all Patients
2. List all Providers
3. List all Appointments
4. List Appointments by Patient
5. List Appointments by Provider
6. List Appointment by date range
7. List Appointment by status
8. Exit read menu
> 3
Appointment [ID: 700000002, Patient: Jim, Provider: Eye Doctor, Start: 1, End: 5, Status: COMPLETED, Reason: Jim has eye issues.]
Appointment [ID: 700000003, Patient: Harry, Provider: Pharma, Start: 2, End: 4, Status: SCHEDULED, Reason: Harry needs his meds.]

What would you like to sort by:
1. List all Patients
2. List all Providers
3. List all Appointments
4. List Appointments by Patient
5. List Appointments by Provider
6. List Appointment by date range
7. List Appointment by status
8. Exit read menu
> 8
Leaving read menu...

Which action would you like to perform?
Create, Read, Update, or Delete [C, R, U, D, or Q to Quit]
> q
Exiting...
rheggeth@Violet:~/School/CompSci/OOP/OOP_Final_Project$

## Authors
-Rheggeth (L.Marez)
-Noah W.
-Luis Franco
