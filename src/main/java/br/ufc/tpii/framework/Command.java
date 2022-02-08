package br.ufc.tpii.framework;

import br.ufc.tpii.helpers.Loggable;

public interface Command extends Loggable {
    /* Design Pattern: Command | https://pt.wikipedia.org/wiki/Command */
    void execute(); // App é Model
}
