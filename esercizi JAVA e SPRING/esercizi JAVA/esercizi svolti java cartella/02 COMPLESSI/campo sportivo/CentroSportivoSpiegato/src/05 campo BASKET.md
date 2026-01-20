nella classe campo basket vado a definire quelle caratteristiche (tramite attributi) o quei comportamenti tramite i metodi che si riferiscono SOLO al campo da basket

ricomincia da qui scrivi che quando appare errore qui sotto
Implicit super constructor Campo() is undefined for default constructor. Must define an explicit constructor

bisogna usare super come qui sotto

## SUPER

visto che nella classe astratta campo ho definito il costruttore, nella classe che implementa classe astratta vado ad utilizzare super

public CampoBasket() {
super(TipoCampo.BASKET);
}

## sovrascrivi metodo

visto che nella classe astratta ho definito il metodo inizializza disponibilita senza dire cosa deve accadere quando lo richiamo, nella classe campo basket vado a dire cosa deve accadere quando campo basket richiama questo metodo
@Override
protected void inizializzaDisponibilita() {
