import java.util.Scanner;
import java.util.concurrent.CountDownLatch;

public class Hospital {


public static void main(String[] args) throws InterruptedException {

    Scanner input = new Scanner(System.in);


    System.out.print("Enter waiting room capacity: ");


    int capacity = input.nextInt();

    System.out.print("Enter number of doctors: ");
    int numberOfDoctors = input.nextInt();

    

    System.out.print("Enter number of patients: ");
    int numberOfPatients = input.nextInt();

    
    WaitingRoom waitingRoom = new WaitingRoom(capacity);





    TreatmentRoomPool treatmentRoomPool =
            new TreatmentRoomPool(2);

   
    CountDownLatch completedPatients =
            new CountDownLatch(numberOfPatients);    
    Thread[] doctors = new Thread[numberOfDoctors];
    for (int i = 0; i < numberOfDoctors; i++) {

        Doctor doctor = new Doctor(
                i + 1,
                waitingRoom,
                treatmentRoomPool,
                completedPatients
        );


        doctors[i] = new Thread(doctor);
    }


    Thread[] patients = new Thread[numberOfPatients];

    for (int i = 0; i < numberOfPatients; i++) {

        Patient patient = new Patient(
                i + 1,
                waitingRoom
        );

        patients[i] = new Thread(patient);
    }



    System.out.println("Starting doctors and patients...");

 
    for (Thread doctor : doctors) {
        doctor.start();
    }

   


    for (Thread patient : patients) {
        patient.start();
    }

    System.out.println("All threads started!");

    for (Thread patient : patients) {
        patient.join();
    }

    completedPatients.await();

    System.out.println("\nAll patients have been treated.");


    for (Thread doctor : doctors) {
        doctor.interrupt();
    }


    for (Thread doctor : doctors) {
        doctor.join();
    }


    

    System.out.println("Simulation completed.");

    input.close();
}



}
