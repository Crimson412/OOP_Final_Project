package oop.main;

import oop.dao.*;
import oop.service.AppointmentManager;
import oop.model.*;

import java.util.Scanner;
import java.util.ArrayList;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * Main class to run appointment scheduling system.
 *
 * @author Rheggeth M.
 * @version Final Project, main
 * @bugs deleting the last patient/provider/appointment will make it such that the next created one has the same id
 * not a massive issue since we are now enforcing appointments to be deleted before its referenced patient/provider is
 */
public class Main {
    public static void main(String args[]) {
        // didn't decide to make all the method static since AppointmentManager was such a long name D:
        AppointmentManager manager = new AppointmentManager();
        Scanner in = new Scanner(System.in);
        char input = 'a';
        boolean loop = true;
        Patient pat;
        Provider pro;
        Appointment apt;

        Database.initializeDatabase();

        ArrayList<Patient> patients = PatientDAO.getAllPatients();
        if (patients.size() != 0) {
            pat = patients.get(patients.size() - 1);
            Patient.updateNumPatients(pat.getPatientID());
            manager.setPatientsList(patients);
        }
        ArrayList<Provider> providers = ProviderDAO.getAllProviders();
        if (providers.size() != 0) {
            pro = providers.get(providers.size() - 1);
            Provider.updateNumProviders(pro.getProviderID());
            manager.setProvidersList(providers);
        }
        ArrayList<Appointment> apts = AppointmentDAO.getAllAppointments();
        // Print result for debugging
        //for (int i = 0; i < apts.size(); i++) System.out.println(apts.get(i));
        if (apts.size() != 0) {
            apt = apts.get(apts.size() - 1);
            Appointment.updateNumAppointments(apt.getAppointmentID());
            manager.setAppointmentsList(apts);
        }

        System.out.println("Database finished loading");

        // Enter logic loop
        while (input != 'Q') {
            System.out.print("\nWhich action would you like to perform?\nCreate, Read, Update, or Delete [C, R, U, D, or Q to Quit]\n> ");
            input = in.next().charAt(0);
            input = Character.toUpperCase(input);
            loop = true;
            switch (input) {
                case 'C':
                    // create
                    while (loop) {
                        System.out.print("\nWhat would you like to create:\n1. Patient\n2. Provider\n3. Appointment\n4. Exit create menu\n> ");
                        input = in.next().charAt(0);
                        switch (input) {
                            case '1':
                                // create Patient
                                pat = manager.addPatient();
                                if (pat != null) PatientDAO.addPatient(pat);
                                break;
                            case '2':
                                // create Provider
                                pro = manager.addProvider();
                                if (pro != null) ProviderDAO.addProvider(pro);
                                break;
                            case '3':
                                // create Appointment
                                apt = manager.scheduleAppointment();
                                if (apt != null) AppointmentDAO.addAppointment(apt);
                                break;
                            case '4':
                                // go back a step
                                System.out.println("Leaving creation menu...");
                                loop = false;
                                break;
                            default:
                                System.out.println("Please only enter one of the numbers 1, 2, 3, or 4");
                        }
                    }
                    break;
                case 'R':
                    // read
                    while (loop) {
                        System.out.print("\nWhat would you like to sort by:\n1. List all Patients\n2. List all Providers\n" +
                            "3. List all Appointments\n4. List Appointments by Patient\n5. List Appointments by Provider\n" +
                            "6. List Appointment by date range\n7. List Appointment by status\n8. Exit read menu\n> ");
                        input = in.next().charAt(0);
                        switch (input) {
                            case '1':
                                // list all patients
                                manager.listPatients();
                                break;
                            case '2':
                                // list all providers
                                manager.listProviders();
                                break;
                            case '3':
                                // list all appointments
                                manager.listAppointments();
                                break;
                            case '4':
                                // list appointments by patient
                                manager.getAppointmentsByPatient();
                                break;
                            case '5':
                                // list appointments by provider
                                manager.getAppointmentsByProvider();
                                break;
                            case '6':
                                // list appointments by date range
                                manager.getAppointmentsByDateRange();
                                break;
                            case '7':
                                // list appointments by status
                                manager.getAppointmentsByStatus();
                                break;
                            case '8':
                                // go back a step
                                System.out.println("Leaving read menu...");
                                loop = false;
                                break;
                            default:
                                System.out.println("Please only enter one of the numbers 1-8");
                        }
                    }
                    break;
                case 'U':
                    // update
                    while (loop) {
                        System.out.print("\nWhat would you like to update:\n1. Reschedule an Appointment\n2. Update an Appointment Status" +
                            "\n3. Exit update menu\n> ");
                        input = in.next().charAt(0);
                        switch (input) {
                            case '1':
                                // reschedule appointment
                                manager.rescheduleAppointment();
                                break;
                            case '2':
                                // update appointment status
                                manager.updateAppointmentStatus();
                                break;
                            case '3':
                                // go back a step
                                System.out.println("Leaving update menu...");
                                loop = false;
                                break;
                            default:
                                System.out.println("Please only enter one of the numbers 1, 2, or 3");
                        }
                    }
                    break;
                case 'D':
                    // delete
                    while (loop) {
                        System.out.print("\nWhat would you like to delete:\n1. Patient\n2. Provider\n3. Appointment\n4. Exit delete menu\n> ");
                        input = in.next().charAt(0);
                        switch (input) {
                            case '1':
                                // delete patient
                                pat = manager.deletePatient();
                                if (pat != null) PatientDAO.deletePatient(pat.getPatientID());
                                break;
                            case '2':
                                // delete provider
                                pro = manager.deleteProvider();
                                if (pro != null) ProviderDAO.deleteProvider(pro.getProviderID());
                                break;
                            case '3':
                                // delete provider
                                apt = manager.deleteAppointment();
                                if (apt != null) AppointmentDAO.deleteAppointment(apt.getAppointmentID());
                                break;
                            case '4':
                                // go back a step
                                System.out.println("Leaving delete menu...");
                                loop = false;
                                break;
                            default:
                                System.out.println("Please only enter one of the numbers 1, 2, 3, or 4");
                        }
                    }
                    break;
                case 'Q':
                    System.out.println("Exiting...");
                    break;
                default:
                    System.out.println("Please only enter a valid character!");
            }
        }
    }
}
