package ticketmachine;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.*;

class TicketMachineTest {
	private static final int PRICE = 50; // Une constante

	private TicketMachine machine; // l'objet à tester

	@BeforeEach
	public void setUp() {
		machine = new TicketMachine(PRICE); // On initialise l'objet à tester
	}

	@Test
	// S1 : le prix affiché correspond à l’initialisation.
	void priceIsCorrectlyInitialized() {
		// Paramètres : valeur attendue, valeur effective, message si erreur
		assertEquals(PRICE, machine.getPrice(), "Initialisation incorrecte du prix");
	}

	@Test
	// S2 : la balance change quand on insère de l’argent
	void insertMoneyChangesBalance() {
		machine.insertMoney(10);
		machine.insertMoney(20);
		assertEquals(10 + 20, machine.getBalance(), "La balance n'est pas correctement mise à jour");
	}

	@Test
    // S3 : on n’imprime pas le ticket si le montant inséré est insuffisant
	void cannotPrintTicketIfInsufficientBalance() {
		machine.insertMoney(PRICE - 10); // On insère moins que le prix du ticket
		assertFalse(machine.printTicket(), "Le ticket a été imprimé malgré une balance insuffisante");
	}

	@Test
    // S4 : on imprime le ticket si le montant inséré est suffisant
	void printTicketWithSufficientBalance() {
		machine.insertMoney(PRICE); // Insérer un montant suffisant pour acheter un ticket
		assertTrue(machine.printTicket(), "Le ticket n'a pas été imprimé malgré un montant suffisant");
	}

	@Test
    // S5 : Quand on imprime un ticket la balance est décrémentée du prix du ticket
	void balanceIsDecrementedAfterPrintingTicket() {
		machine.insertMoney(PRICE + 20); // On insère plus que le prix du ticket
		machine.printTicket();
		assertEquals(20, machine.getBalance(), "La balance n'a pas été correctement " +
				"décrémentée après l'impression du ticket");
	}

	@Test
    // S6 : le montant collecté est mis à jour quand on imprime un ticket (pas avant)
	void totalIsUpdatedAfterPrintingTicket() {
		machine.insertMoney(PRICE);
		machine.printTicket();
		assertEquals(PRICE, machine.getTotal(), "Le montant collecté n'a pas été mis à jour" +
				" après l'impression du ticket");
	}

	@Test
    // S7 : refund() rend correctement la monnaie
	void refundReturnsCorrectBalance() {
		machine.insertMoney(PRICE + 30); // On insère plus que le prix du ticket
		int refundedAmount = machine.refund();
		assertEquals(PRICE + 30, refundedAmount, "Le montant rendu par refund() est incorrect");
	}

	@Test
    // S8 : refund() remet la balance à zéro
	void refundResetsBalanceToZero() {
		machine.insertMoney(PRICE + 30); // On insère plus que le prix du ticket
		machine.refund();
		assertEquals(0, machine.getBalance(), "La balance n'a pas été remise à zéro après refund()");
	}

	@Test
    // S9 : on ne peut pas insérer un montant négatif
	void cannotInsertNegativeAmount() {
		assertThrows(IllegalArgumentException.class, () -> {
			machine.insertMoney(-10);
		}, "L'insertion d'un montant négatif aurait dû lever une exception");
	}

	@Test
    // S10 : on ne peut pas créer de machine qui délivre des tickets dont le prix est négatif
	void cannotCreateMachineWithNegativeTicketPrice() {
		assertThrows(IllegalArgumentException.class, () -> {
			new TicketMachine(-PRICE);
		}, "La création d'une machine avec un prix de ticket négatif aurait dû lever une exception");
	}

}
