package org.firstinspires.ftc.teamcode.feature.control;

public class Coefficients {
    public static class SlideCoefficients {
        public double kP,  kI, kD, kG;

        public SlideCoefficients(double kP, double kI, double kD, double kG) {
            this.kP = kP;
            this.kI = kI;
            this.kD = kD;
            this.kG = kG;
        }
    }
}
