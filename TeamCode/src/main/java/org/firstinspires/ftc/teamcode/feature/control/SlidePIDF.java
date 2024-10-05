package org.firstinspires.ftc.teamcode.feature.control;

import androidx.annotation.NonNull;

import org.firstinspires.ftc.teamcode.wrapper.CachedMotor;

import dev.frozenmilk.dairy.core.Feature;
import dev.frozenmilk.dairy.core.dependency.Dependency;
import dev.frozenmilk.dairy.core.dependency.lazy.Yielding;
import dev.frozenmilk.dairy.core.wrapper.Wrapper;

public class SlidePIDF implements Feature {
    // give this lower priority than subsystem updates
    private Dependency<?> dependencies = Yielding.INSTANCE;
    final private Coefficients.SlideCoefficients coefficients;
    private boolean isEnabled = true;
    private CachedMotor actuator1, actuator2;
    private double target = 0.0;

    public SlidePIDF(CachedMotor actuator1, CachedMotor actuator2, Coefficients.SlideCoefficients coefficients) {
        this.coefficients = coefficients;
        this.actuator1 = actuator1;
        this.actuator2 = actuator2;
    }

    // register this feature
    {
        register();
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

    @Override
    public void cleanup(@NonNull Wrapper opmode) {
        this.actuator1 = null;
        this.actuator2 = null;
        deregister();
    }

    private void update() {
        if (!isEnabled) return;
        double error = target - actuator1.__IMPL.getCurrentPosition();
        double response = error * coefficients.kP * error + coefficients.kG; // TODO: do the magical integral and derivative stuff
        actuator1.setPower(response);
        actuator2.setPower(response);
    }

    private void setTarget(double target) {
        this.target = target;
    }

    private boolean setEnabled(boolean enabled) {
        return (isEnabled = enabled);
    }

    @Override
    public void preUserInitLoopHook(@NonNull Wrapper opMode) {
       update();
    }

    @Override
    public void preUserLoopHook(@NonNull Wrapper opMode) {
        update();
    }
}
