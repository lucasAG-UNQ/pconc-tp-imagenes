package Tasks;

import Exceptions.PoisonPillException;

public class PoisonPill extends Task {

    @Override
    public void run() throws PoisonPillException {
        throw new PoisonPillException();
    }

}
