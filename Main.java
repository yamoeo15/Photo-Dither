/*
Emmanuel Yamoah
10/12/16
CSC 111
Professor Jennifer Burg
 */

                                                                            //Dithering Program//

/*

In this program, we used user-defined methods in order to alter pixels within an image and write out the image file with
 a dithered effect. I read in the file using for loops and arrays. Then used a switch statement with method calls in order to pass the call
 into the various method: random, pattern and error diffusion. Finally, I wrote out the program with a new name by inserting either RD (random dithering),
 ED (error diffusion) and PD (pattern diffusion) into the name of the new dithered image and write out the new image into the hard drive to be opened into an image
 processing software like PhotoShop.
 */




package com.company;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Random;
import java.util.Scanner;

public class Main {
        public static void main (String[]args) throws Exception {
        //Initializing of Variables
        int i; //index variable
        int j; //Second index variable
        Scanner scnr = new Scanner(System.in);
        int dithType;
        String name1 = "";
        byte b;
        String filename1 = "";
        String outputfile = "";
        String first = "";
        int width; //width variable
        int height; //height variable
        char userQuit = 'a';

while (userQuit != 'n') {
    //Greet Players and introduce Application
    System.out.println("Welcome to Image Dither! An Application that dithers an image of your choice!"); //Greeting
    System.out.println("Enter your name!"); //Prompting for name
    name1 = scnr.next();//scanner for user imput
    System.out.println("Welcome " + name1 + "," + " please insert a file that you would want to dither!"); //Prompting to insert file
    System.out.println("Enter your file!");
    filename1 = scnr.next();//scanner for user imput
    System.out.println("Enter the width of the image"); //Prompting to enter width
    width = scnr.nextInt();//scanner for user imput
    System.out.println("Enter the height of the image"); //Prompting to enter height
    height = scnr.nextInt();//scanner for user imput
    System.out.println("What type of Dithering would you want to do?"); //Asking for type of dither
    System.out.println("Enter 1 for random dithering, 2 for pattern dithering, and 3 to see Air diffusion dithering");
    dithType = scnr.nextInt(); //scanner for user imput
    int pixelsTemp[][] = new int[height][width]; //Array for image pixel values



    try { //Reading in the file
        FileInputStream file = new FileInputStream(filename1);


        for (int c = 0; c < height; c++) {
            for (int r = 0; r < width; r++) {
                b = (byte) file.read();
                pixelsTemp[c][r] = b & 0xff;

            }
        }
        file.close();
    } catch (IOException e) {
        System.out.println("Could not open file");
        System.exit(0);
    }

    first = filename1.substring(0, filename1.length() - 4);
    switch (dithType) {   //Switch calling the various methods
        case 1:
            randDith(pixelsTemp, width, height); //Method call for random dithering
            outputfile = first.concat("RD.raw");//concatinating "RD.raw" into new image file
            break;
        case 2:
            patternDith(pixelsTemp, width, height); //Method call for pattern dithering
            outputfile = first.concat("PD.raw");//concatinating "PD.raw" into new image file
            break;
        case 3:
            errorDith(pixelsTemp, width, height); //Method call for error Dithering
            outputfile = first.concat("ED.raw"); //concatinating "ED.raw" into new image file
            break;
    }
    try { //Writing out file
        FileOutputStream fileOs = new FileOutputStream(outputfile);
        for (i = 0; i < height; i++) {
            for (j = 0; j < width; j++) {
                fileOs.write((byte) pixelsTemp[i][j] & 0xff);
            }
        }
        fileOs.close();
    } catch (IOException e) {
        System.out.println("Could not write out file");
        System.exit(0);
    }

    //Prompt alerting the user that dithering is complete
    System.out.println("Dithering Complete! Open your file in PhotoShop or any other image processing application for your results!");
    System.out.println("Would you like to Dither another image? y for Yes and n for No");
    userQuit = scnr.next().charAt(0); //Forcing scanner to get first letter of the response

}
    }
    public static void randDith(int[][] pixelsTemp, int c, int r) { //Defining the random dithering method
        int temp[][] = new int[r][c];

        Random randGen = new Random(); //random number generator
        for (int i = 0; i < r; i++) {
            for (int j = 0; j < c; j++) { //If statement used in order to round values up or down based on random number
                int n = randGen.nextInt(256);
                if (pixelsTemp[i][j] > n) {
                    pixelsTemp[i][j] = 255;

                } else {
                    pixelsTemp[i][j] = 0;
                }
            }

        }

    }


    public static void patternDith(int[][] pixelsTemp, int c, int r) { //Defining pattern dithering method
        int[][] mask = {{8, 3, 4}, {6, 1, 2}, {7, 8, 9}};
        int i; //iterating variable
        int j; //iterating variable
        int x; //rows for mask
        int y; // column for mask
        j = 0;
        for (i = 0; i < r; i++) { //Standardizes pixel values between  0 - 9
            for (j = 0; j < c; j++) {
                pixelsTemp[i][j] = (int) (pixelsTemp[i][j] / 25.6);
            }
        }
        for (i = 0; i < r - 3; i = i + 3) { //First for loop compares the image to the mask
            for (j = 0; j < c - 3; j = j + 3) {
                for (x = 0; x < 3; x++) { //Second for loop is the mask moving and changing values based on comparison to the original image.
                    for (y = 0; y < 3; y++) {
                        if (pixelsTemp[i + x][j + y] < mask[x][y]) { //if else statement used to change values
                            pixelsTemp[i + x][j + y] = 0;
                        } else {
                            pixelsTemp[i + x][j + y] = 255;
                        }
                    }
                }
            }
        }
    }


    public static void errorDith(int[][] pixelsTemp, int c, int r) { //Defining error Diffusion method
        int i; //iterating variable
        int j; //iterating variable
        int toDistribute = 0;
        for (i = 0; i < r - 2; i++) {
            for (j = 0; j < c - 3; j++) {
                if (pixelsTemp[i][j + 1] > 127) { //compares pixels that are less that 127
                    toDistribute = pixelsTemp[i][j + 1] - 255;
                    pixelsTemp[i][j + 2] = (int) (pixelsTemp[i][j + 2] + toDistribute * (double) (7 / 16.0)); //adds fraction of difference or "error" to surrounding pixels
                    if (pixelsTemp[i][j + 2] < 0) {
                        pixelsTemp[i][j + 2] = 0;
                    }
                    pixelsTemp[i + 1][j] = (int) (pixelsTemp[i + 1][j] + toDistribute * (double) (3 / 16.0));//adds fraction of difference or "error" to surrounding pixels
                    if (pixelsTemp[i + 1][j] < 0) {
                        pixelsTemp[i + 1][j] = 0;
                    }
                    pixelsTemp[i + 1][j + 1] = (int) (pixelsTemp[i + 1][j + 1] + toDistribute * (double) (5 / 16.0));//adds fraction of difference or "error" to surrounding pixels
                    if (pixelsTemp[i + 1][j + 1] < 0) {
                        pixelsTemp[i + 1][j + 1] = 0;
                    }
                    pixelsTemp[i + 1][j + 2] = (int) (pixelsTemp[i + 1][j + 2] + toDistribute * (double) (1 / 16.0));//adds fraction of difference or "error" to surrounding pixels
                    if (pixelsTemp[i + 1][j + 2] < 0) {
                        pixelsTemp[i + 1][j + 2] = 0;
                    }
                } else if (pixelsTemp[i][j + 1] <= 127) { //Check if this is how you round down
                    toDistribute = pixelsTemp[i][j + 1];
                    pixelsTemp[i][j + 2] = (int) (pixelsTemp[i][j + 2] + toDistribute * (double) (7 / 16.0));//subtracts fraction of difference or "error" to surrounding pixels
                    if (pixelsTemp[i][j + 2] > 255) {
                        pixelsTemp[i][j + 2] = 255;
                    }
                    pixelsTemp[i + 1][j] = (int) (pixelsTemp[i + 1][j] + toDistribute * (double) (3 / 16.0));//subtracts fraction of difference or "error" to surrounding pixels
                    if (pixelsTemp[i + 1][j] > 255) {
                        pixelsTemp[i + 1][j] = 255;
                    }
                    pixelsTemp[i + 1][j + 1] = (int) (pixelsTemp[i + 1][j + 1] + toDistribute * (double) (5 / 16.0));//subtracts fraction of difference or "error" to surrounding pixels
                    if (pixelsTemp[i + 1][j + 1] > 255) {
                        pixelsTemp[i + 1][j + 1] = 255;
                    }
                    pixelsTemp[i + 1][j + 2] = (int) (pixelsTemp[i + 1][j + 2] + toDistribute * (double) (1 / 16.0));//subtracts fraction of difference or "error" to surrounding pixels
                    if (pixelsTemp[i + 1][j + 2] > 255) {
                        pixelsTemp[i + 1][j + 2] = 255;
                    }
                }

            }
        }
        for (i = 0; i < r -2 ; i++) { //Caps the pixels to not go beyond 255 and makes sure it does not go under 0
            for (j = 0; j < c- 3; j++) {
                if (pixelsTemp[i][j] < 128) {
                    pixelsTemp[i][j] = 0;

                } else {
                    pixelsTemp[i][j] = 255;
                }
            }
        }
    }
}











