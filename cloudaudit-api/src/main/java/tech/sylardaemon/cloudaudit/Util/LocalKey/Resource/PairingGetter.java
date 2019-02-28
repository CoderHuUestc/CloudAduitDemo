package tech.sylardaemon.cloudaudit.Util.LocalKey.Resource;

import it.unisa.dia.gas.jpbc.Pairing;
import it.unisa.dia.gas.plaf.jpbc.pairing.PairingFactory;

public class PairingGetter {
    public static final Pairing PAIRING = PairingFactory.getPairing("a.properties");
}
