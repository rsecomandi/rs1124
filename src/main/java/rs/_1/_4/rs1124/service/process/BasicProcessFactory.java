package rs._1._4.rs1124.service.process;

import rs._1._4.rs1124.service.process.checkout.CheckoutProcess;
import rs._1._4.rs1124.service.process.console.ConsoleProcess;

public class BasicProcessFactory {
    public enum ProcessType {
        CONSOLE,
        CHECKOUT
    }

    public static BasicProcess getNewProcess(ProcessType processType) {
        BasicProcess basicProcess = switch (processType) {
            case CONSOLE -> new ConsoleProcess();
            case CHECKOUT -> new CheckoutProcess();
        };
        basicProcess.initializeSteps();
        return basicProcess;
    }
}
