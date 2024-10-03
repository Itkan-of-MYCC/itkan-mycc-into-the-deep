package org.firstinspires.ftc.teamcode.feature;

import androidx.annotation.NonNull;

import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;

import org.firstinspires.ftc.teamcode.wrapper.CachedMotor;

import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import dev.frozenmilk.dairy.core.FeatureRegistrar;
import dev.frozenmilk.dairy.core.dependency.Dependency;
import dev.frozenmilk.dairy.core.wrapper.Wrapper;
import dev.frozenmilk.mercurial.Mercurial;
import dev.frozenmilk.mercurial.bindings.BoundGamepad;
import dev.frozenmilk.mercurial.commands.Lambda;
import dev.frozenmilk.mercurial.subsystems.Subsystem;
import dev.frozenmilk.mercurial.subsystems.SubsystemObjectCell;
import kotlin.annotation.MustBeDocumented;

public class MecanumDrive implements Subsystem {
    // Add mercurial as a dependency
    private Dependency<?> dependencies = Subsystem.DEFAULT_DEPENDENCY;
    public static final MecanumDrive INSTANCE = new MecanumDrive();

    // MOTORS
    private final SubsystemObjectCell<CachedMotor> leftFront = subsystemCell(() ->
            new CachedMotor(FeatureRegistrar.getActiveOpMode().hardwareMap, "leftFront"));
    private final SubsystemObjectCell<CachedMotor> rightFront = subsystemCell(() ->
            new CachedMotor(FeatureRegistrar.getActiveOpMode().hardwareMap, "rightFront"));
    private final SubsystemObjectCell<CachedMotor> rightBack = subsystemCell(() ->
            new CachedMotor(FeatureRegistrar.getActiveOpMode().hardwareMap, "rightBack"));
    private final SubsystemObjectCell<CachedMotor> leftBack = subsystemCell(() ->
            new CachedMotor(FeatureRegistrar.getActiveOpMode().hardwareMap, "leftBack"));


    private MecanumDrive() {

    }

    @NonNull
    @Override
    public Dependency<?> getDependency() {
        return dependencies;
    }

    @Override
    public void setDependency(@NonNull Dependency<?> dependency) {
        this.dependencies = dependency;
    }

    @Retention(RetentionPolicy.RUNTIME)
    @Target(java.lang.annotation.ElementType.TYPE)
    @MustBeDocumented
    @Inherited
    public @interface Attach {
    }

    @Override
    public void cleanup(@NonNull Wrapper opmode) {
        leftFront.invalidate();
        rightFront.invalidate();
        leftBack.invalidate();
        rightBack.invalidate();
    }

    /****** GETTERS */
    public CachedMotor getLeftFront() { return leftFront.get(); }
    public CachedMotor getRightFront() { return rightFront.get(); }
    public CachedMotor getLeftBack() { return leftBack.get(); }
    public CachedMotor getRightBack() { return rightBack.get(); }

    /******** MERCURIAL HOOKS */
    @Override
    public void preUserInitLoopHook(@NonNull Wrapper opmode) {
        getLeftBack().setDirection(DcMotorSimple.Direction.REVERSE);
        getRightBack().setDirection(DcMotorSimple.Direction.REVERSE);
    }

    /******* COMMANDS */
    // drives the robot
    public Lambda robotCentricDriveCommand() {
        BoundGamepad gamepad1 = Mercurial.gamepad1();
        return new Lambda("mecanum-drive-robot-centric")
                .setInit(() -> {

                })
                .setExecute(() -> {
                    double x = gamepad1.leftStickX().state();
                    double y = gamepad1.leftStickY().state();
                    double rx = gamepad1.rightStickX().state();
                    double denominator = Math.max(Math.abs(y) + Math.abs(x) + Math.abs(rx), 1);
                    double frontLeftPower = (y + x + rx) / denominator;
                    double backLeftPower = (y - x + rx) / denominator;
                    double frontRightPower = (y - x - rx) / denominator;
                    double backRightPower = (y + x - rx) / denominator;

                    getLeftFront().setPower(frontLeftPower);
                    getLeftBack().setPower(backLeftPower);
                    getRightFront().setPower(frontRightPower);
                    getRightBack().setPower(backRightPower);
                })
                .setFinish(() -> false);
    }
}